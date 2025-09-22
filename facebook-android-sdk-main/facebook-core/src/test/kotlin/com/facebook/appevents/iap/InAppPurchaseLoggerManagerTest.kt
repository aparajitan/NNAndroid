/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.appevents.iap

import android.content.Context
import android.content.SharedPreferences
import com.facebook.FacebookPowerMockTestCase
import com.facebook.FacebookSdk
import java.util.concurrent.Executor
import org.assertj.core.api.Assertions
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import java.util.concurrent.ConcurrentHashMap

@PrepareForTest(FacebookSdk::class)
class InAppPurchaseLoggerManagerTest : FacebookPowerMockTestCase() {
    private val mockExecutor: Executor = FacebookSerialExecutor()
    private lateinit var mockNewCachePrefs: SharedPreferences
    private lateinit var mockOldCachePrefs: SharedPreferences
    private var mockNewCachedMap: MutableMap<String, Long> = ConcurrentHashMap()
    private lateinit var editor: SharedPreferences.Editor
    private val packageName = "sample.packagename"
    private val TIME_OF_LAST_LOGGED_PURCHASE_KEY = "TIME_OF_LAST_LOGGED_PURCHASE"
    private val TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY = "TIME_OF_LAST_LOGGED_SUBSCRIPTION"

    @Before
    fun init() {
        PowerMockito.mockStatic(FacebookSdk::class.java)
        whenever(FacebookSdk.getExecutor()).thenReturn(mockExecutor)
        whenever(FacebookSdk.isInitialized()).thenReturn(true)
        val context: Context = mock()
        whenever(FacebookSdk.getApplicationContext()).thenReturn(context)

        PowerMockito.spy(InAppPurchaseLoggerManager::class.java)
        mockNewCachePrefs = mock()
        whenever(
            context.getSharedPreferences(
                eq("com.facebook.internal.iap.IAP_CACHE_GPBLV2V7"),
                any<Int>()
            )
        ).thenReturn(mockNewCachePrefs)
        editor = mock()
        whenever(mockNewCachePrefs.edit()).thenReturn(editor)
        whenever(editor.putLong(any(), any())).thenAnswer {
            mockNewCachedMap[it.getArgument(0)] = it.getArgument(1)
            return@thenAnswer editor
        }
        whenever(editor.putString(any(), any())).thenAnswer {
            mockNewCachedMap[it.getArgument(0)] = it.getArgument(1)
            return@thenAnswer editor
        }
        whenever(
            mockNewCachePrefs.getLong(
                eq(TIME_OF_LAST_LOGGED_PURCHASE_KEY),
                any()
            )
        ).thenAnswer {
            return@thenAnswer mockNewCachedMap.getOrDefault(it.getArgument(0), it.getArgument(1))
        }
        whenever(
            mockNewCachePrefs.getLong(
                eq(TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY),
                any()
            )
        ).thenAnswer {
            return@thenAnswer mockNewCachedMap.getOrDefault(it.getArgument(0), it.getArgument(1))
        }
        whenever(editor.putStringSet(any(), any())).thenReturn(editor)
        doNothing().whenever(editor).apply()

        mockOldCachePrefs = mock()
        whenever(
            context.getSharedPreferences(
                eq("com.facebook.internal.iap.PRODUCT_DETAILS"),
                any<Int>()
            )
        ).thenReturn(mockOldCachePrefs)
        whenever(mockOldCachePrefs.contains(eq("PURCHASE_DETAILS_SET"))).thenReturn(true)
    }

    private fun putMapInOldCache(map: MutableMap<String, Long>) {
        val newValues = hashSetOf<String>()

        for (key in map.keys) {
            newValues.add("token;" + map[key])
        }
        whenever(mockOldCachePrefs.getStringSet(eq("PURCHASE_DETAILS_SET"), any())).thenReturn(
            newValues
        )
    }

    @Test
    fun testCacheDeDupPurchaseOnFirstTimeLogging() {
        mockNewCachedMap.clear()
        // Construct purchase details map
        val mockPurchaseDetailsMap: MutableMap<String, JSONObject> = mutableMapOf()
        val purchaseDetailJson1 =
            JSONObject(
                "{\"productId\":\"espresso\",\"purchaseToken\":\"purchase_token\",\"purchaseTime\":1600000000000,\"developerPayload\":null,\"packageName\":\"sample.packagename\"}"
            )
        mockPurchaseDetailsMap["espresso"] = purchaseDetailJson1

        // Construct cached purchase map
        val cachedPurchaseMap =
            mutableMapOf(
                "purchase_token" to
                        1_600_000_000L
            )
        putMapInOldCache(cachedPurchaseMap)
        InAppPurchaseLoggerManager.migrateOldCacheHistory()
        // Test duplicate purchase event can be successfully removed from purchase details map
        val cachedMap = InAppPurchaseLoggerManager.cacheDeDupPurchase(
            mockPurchaseDetailsMap,
            false,
        )
        Assertions.assertThat(cachedMap).isEmpty()

        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_PURCHASE_KEY])
            .isEqualTo(1736528400000L)
        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY])
            .isEqualTo(1736528400000L)

    }

    @Test
    fun testCacheDeDupPurchaseOnFirstTimeLoggingAndNewPurchase() {
        mockNewCachedMap.clear()
        // Construct purchase details map
        val mockPurchaseDetailsMap: MutableMap<String, JSONObject> = mutableMapOf()
        val purchaseDetailJson1 =
            JSONObject(
                "{\"productId\":\"espresso\",\"purchaseToken\":\"token123\",\"purchaseTime\":1736528400001,\"developerPayload\":null,\"packageName\":\"sample.packagename\"}"
            )
        mockPurchaseDetailsMap["espresso"] = purchaseDetailJson1

        // Construct cached purchase map
        val lastClearedTime = 1_600_000_000L
        val cachedPurchaseMap =
            mutableMapOf(
                "otherpurchasetoken" to
                        lastClearedTime
            )

        putMapInOldCache(cachedPurchaseMap)
        InAppPurchaseLoggerManager.migrateOldCacheHistory()
        val cachedMap = InAppPurchaseLoggerManager.cacheDeDupPurchase(
            mockPurchaseDetailsMap,
            false,
        )
        Assertions.assertThat(cachedMap).isNotEmpty()

        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_PURCHASE_KEY])
            .isEqualTo(1736528400001)
        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY])
            .isEqualTo(1736528400000)
    }

    @Test
    fun testCacheDeDupPurchaseOnFirstTimeLoggingAndInvalidCacheHistory() {
        mockNewCachedMap.clear()
        // Construct purchase details map
        val mockPurchaseDetailsMap: MutableMap<String, JSONObject> = mutableMapOf()
        val purchaseDetailJson1 =
            JSONObject(
                "{\"productId\":\"espresso\",\"purchaseToken\":\"token123\",\"purchaseTime\":1736528400001,\"developerPayload\":null,\"packageName\":\"sample.packagename\"}"
            )
        mockPurchaseDetailsMap["espresso"] = purchaseDetailJson1

        // Construct cached purchase map
        val lastClearedTime = 1_740_000_000_000L
        val cachedPurchaseMap =
            mutableMapOf(
                "otherpurchasetoken" to
                        lastClearedTime
            )
        putMapInOldCache(cachedPurchaseMap)
        InAppPurchaseLoggerManager.migrateOldCacheHistory()

        val cachedMap = InAppPurchaseLoggerManager.cacheDeDupPurchase(
            mockPurchaseDetailsMap,
            false,
        )
        Assertions.assertThat(cachedMap).isNotEmpty()

        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_PURCHASE_KEY])
            .isEqualTo(1736528400001)
        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY])
            .isEqualTo(1736528400000)
    }

    @Test
    fun testCacheDeDupPurchaseNotFirstTimeLogging() {
        mockNewCachedMap.clear()
        mockNewCachedMap[TIME_OF_LAST_LOGGED_PURCHASE_KEY] = 1630000000000

        // Construct purchase details map
        val mockPurchaseDetailsMap: MutableMap<String, JSONObject> = mutableMapOf()
        val purchaseDetailJson1 =
            JSONObject(
                "{\"productId\":\"espresso\",\"purchaseToken\":\"token123\",\"purchaseTime\":1620000000001,\"developerPayload\":null,\"packageName\":\"sample.packagename\"}"
            )
        mockPurchaseDetailsMap["espresso"] = purchaseDetailJson1

        InAppPurchaseLoggerManager.migrateOldCacheHistory()
        // Test duplicate purchase event can be successfully removed from purchase details map
        val cachedMap = InAppPurchaseLoggerManager.cacheDeDupPurchase(
            mockPurchaseDetailsMap,
            false,
        )
        Assertions.assertThat(cachedMap).isEmpty()

        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_PURCHASE_KEY])
            .isEqualTo(1736528400000)
        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY])
            .isEqualTo(1736528400000)
    }

    @Test
    fun testCacheDeDupPurchaseNotFirstTimeLoggingAndNewPurchase() {
        mockNewCachedMap.clear()

        mockNewCachedMap[TIME_OF_LAST_LOGGED_PURCHASE_KEY] = 1620000000000

        // Construct purchase details map
        val mockPurchaseDetailsMap: MutableMap<String, JSONObject> = mutableMapOf()
        val purchaseDetailJson1 =
            JSONObject(
                "{\"productId\":\"espresso\",\"purchaseToken\":\"token123\",\"purchaseTime\":1630000000000000,\"developerPayload\":null,\"packageName\":\"sample.packagename\"}"
            )
        mockPurchaseDetailsMap["espresso"] = purchaseDetailJson1

        InAppPurchaseLoggerManager.migrateOldCacheHistory()
        // Test duplicate purchase event can be successfully removed from purchase details map
        val cachedMap = InAppPurchaseLoggerManager.cacheDeDupPurchase(
            mockPurchaseDetailsMap,
            false,
        )
        Assertions.assertThat(cachedMap).isNotEmpty()

        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_PURCHASE_KEY])
            .isEqualTo(1630000000000000)
        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY])
            .isEqualTo(1736528400000)
    }

    @Test
    fun testCacheDeDupSubscriptionOnFirstTimeLogging() {
        mockNewCachedMap.clear()
        mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY] = 0
        // Construct purchase details map
        val mockPurchaseDetailsMap: MutableMap<String, JSONObject> = mutableMapOf()
        val purchaseDetailJson1 =
            JSONObject(
                "{\"productId\":\"espresso\",\"purchaseToken\":\"token123\",\"purchaseTime\":1600000000000,\"developerPayload\":null,\"packageName\":\"sample.packagename\"}"
            )
        mockPurchaseDetailsMap["espresso"] = purchaseDetailJson1

        // Construct cached purchase map
        val lastClearedTime = 1_600_000_000L
        val cachedPurchaseMap =
            mutableMapOf(
                "purchaseToken=" to
                        lastClearedTime
            )
        putMapInOldCache(cachedPurchaseMap)
        InAppPurchaseLoggerManager.migrateOldCacheHistory()

        // Test duplicate purchase event can be successfully removed from purchase details map
        val cachedMap = InAppPurchaseLoggerManager.cacheDeDupPurchase(
            mockPurchaseDetailsMap,
            true,
        )
        Assertions.assertThat(cachedMap).isEmpty()

        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_PURCHASE_KEY])
            .isEqualTo(1736528400000)
        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY])
            .isEqualTo(1736528400000)
    }

    @Test
    fun testCacheDeDupSubscriptionOnFirstTimeLoggingAndNewPurchase() {
        mockNewCachedMap.clear()
        mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY] = 0
        // Construct purchase details map
        val mockPurchaseDetailsMap: MutableMap<String, JSONObject> = mutableMapOf()
        val purchaseDetailJson1 =
            JSONObject(
                "{\"productId\":\"espresso\",\"purchaseToken\":\"token123\",\"purchaseTime\":1736528400001,\"developerPayload\":null,\"packageName\":\"sample.packagename\"}"
            )
        mockPurchaseDetailsMap["espresso"] = purchaseDetailJson1

        // Construct cached purchase map
        val lastClearedTime = 1_600_000_000L
        val cachedPurchaseMap =
            mutableMapOf(
                "otherpurchasetoken" to
                        lastClearedTime
            )
        putMapInOldCache(cachedPurchaseMap)
        InAppPurchaseLoggerManager.migrateOldCacheHistory()
        val cachedMap = InAppPurchaseLoggerManager.cacheDeDupPurchase(
            mockPurchaseDetailsMap,
            true,
        )
        Assertions.assertThat(cachedMap).isNotEmpty()

        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_PURCHASE_KEY])
            .isEqualTo(1736528400000)
        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY])
            .isEqualTo(1736528400001)
    }

    @Test
    fun testCacheDeDupSubscriptionOnFirstTimeLoggingAndNewerPurchasesInOldCache() {
        mockNewCachedMap.clear()
        mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY] = 0
        // Construct purchase details map
        val mockPurchaseDetailsMap: MutableMap<String, JSONObject> = mutableMapOf()
        val purchaseDetailJson1 =
            JSONObject(
                "{\"productId\":\"espresso\",\"purchaseToken\":\"token123\",\"purchaseTime\":1736528400001,\"developerPayload\":null,\"packageName\":\"sample.packagename\"}"
            )
        mockPurchaseDetailsMap["espresso"] = purchaseDetailJson1

        // Construct cached purchase map
        val lastClearedTime = 1_800_000_000L
        val cachedPurchaseMap =
            mutableMapOf(
                "otherpurchasetoken" to
                        lastClearedTime
            )
        putMapInOldCache(cachedPurchaseMap)
        InAppPurchaseLoggerManager.migrateOldCacheHistory()
        val cachedMap = InAppPurchaseLoggerManager.cacheDeDupPurchase(
            mockPurchaseDetailsMap,
            true,
        )
        Assertions.assertThat(cachedMap).isEmpty()

        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_PURCHASE_KEY])
            .isEqualTo(1800000000000)
        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY])
            .isEqualTo(1800000000000)
    }

    @Test
    fun testCacheDeDupSubscriptionOnFirstTimeLoggingAndNewPurchasesInOldCache() {
        mockNewCachedMap.clear()
        mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY] = 0
        // Construct purchase details map
        val mockPurchaseDetailsMap: MutableMap<String, JSONObject> = mutableMapOf()
        val purchaseDetailJson1 =
            JSONObject(
                "{\"productId\":\"espresso\",\"purchaseToken\":\"token123\",\"purchaseTime\":1835113600001,\"developerPayload\":null,\"packageName\":\"sample.packagename\"}"
            )
        mockPurchaseDetailsMap["espresso"] = purchaseDetailJson1

        // Construct cached purchase map
        val lastClearedTime = 1_800_000_000L
        val cachedPurchaseMap =
            mutableMapOf(
                "otherpurchasetoken" to
                        lastClearedTime
            )
        putMapInOldCache(cachedPurchaseMap)
        InAppPurchaseLoggerManager.migrateOldCacheHistory()
        val cachedMap = InAppPurchaseLoggerManager.cacheDeDupPurchase(
            mockPurchaseDetailsMap,
            true,
        )
        Assertions.assertThat(cachedMap).isNotEmpty()

        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY])
            .isEqualTo(1835113600001)
        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_PURCHASE_KEY])
            .isEqualTo(1800000000000)
    }

    @Test
    fun testCacheDeDupSubscriptionOnFirstTimeLoggingAndInvalidValueInOldCache() {
        mockNewCachedMap.clear()
        mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY] = 0
        // Construct purchase details map
        val mockPurchaseDetailsMap: MutableMap<String, JSONObject> = mutableMapOf()
        val purchaseDetailJson1 =
            JSONObject(
                "{\"productId\":\"espresso\",\"purchaseToken\":\"token123\",\"purchaseTime\":1736528400001,\"developerPayload\":null,\"packageName\":\"sample.packagename\"}"
            )
        mockPurchaseDetailsMap["espresso"] = purchaseDetailJson1

        // Construct cached purchase map
        val lastClearedTime = 1_800L
        val cachedPurchaseMap =
            mutableMapOf(
                "otherpurchasetoken" to
                        lastClearedTime
            )
        putMapInOldCache(cachedPurchaseMap)
        InAppPurchaseLoggerManager.migrateOldCacheHistory()
        val cachedMap = InAppPurchaseLoggerManager.cacheDeDupPurchase(
            mockPurchaseDetailsMap,
            true,
        )
        Assertions.assertThat(cachedMap).isNotEmpty()

        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_PURCHASE_KEY])
            .isEqualTo(1736528400000)
        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY])
            .isEqualTo(1736528400001)

    }

    @Test
    fun testCacheDeDupSubscriptionNotFirstTimeLogging() {
        mockNewCachedMap.clear()
        mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY] = 1620000000002

        // Construct purchase details map
        val mockPurchaseDetailsMap: MutableMap<String, JSONObject> = mutableMapOf()
        val purchaseDetailJson1 =
            JSONObject(
                "{\"productId\":\"espresso\",\"purchaseToken\":\"token123\",\"purchaseTime\":1620000000001,\"developerPayload\":null,\"packageName\":\"sample.packagename\"}"
            )
        mockPurchaseDetailsMap["espresso"] = purchaseDetailJson1

        InAppPurchaseLoggerManager.migrateOldCacheHistory()
        // Test duplicate purchase event can be successfully removed from purchase details map
        val cachedMap = InAppPurchaseLoggerManager.cacheDeDupPurchase(
            mockPurchaseDetailsMap,
            true,
        )
        Assertions.assertThat(cachedMap).isEmpty()

        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_PURCHASE_KEY])
            .isEqualTo(1736528400000)
        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY])
            .isEqualTo(1736528400000)
    }

    @Test
    fun testCacheDeDupSubscriptionNotFirstTimeLoggingAndNewPurchase() {
        mockNewCachedMap.clear()

        mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY] = 1620000000000

        // Construct purchase details map
        val mockPurchaseDetailsMap: MutableMap<String, JSONObject> = mutableMapOf()
        val purchaseDetailJson1 =
            JSONObject(
                "{\"productId\":\"espresso\",\"purchaseToken\":\"token123\",\"purchaseTime\":1620000000000001,\"developerPayload\":null,\"packageName\":\"sample.packagename\"}"
            )
        mockPurchaseDetailsMap["espresso"] = purchaseDetailJson1

        InAppPurchaseLoggerManager.migrateOldCacheHistory()
        // Test duplicate purchase event can be successfully removed from purchase details map
        val cachedMap = InAppPurchaseLoggerManager.cacheDeDupPurchase(
            mockPurchaseDetailsMap,
            true,
        )
        Assertions.assertThat(cachedMap).isNotEmpty()

        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_SUBSCRIPTION_KEY])
            .isEqualTo(1620000000000001)
        Assertions.assertThat(mockNewCachedMap[TIME_OF_LAST_LOGGED_PURCHASE_KEY])
            .isEqualTo(1736528400000)
    }


    @Test
    fun testConstructLoggingReadyMap() {
        mockNewCachedMap.clear()
        val mockPurchaseDetailsMap: MutableMap<String, JSONObject> = mutableMapOf()
        val mockSkuDetailsMap: MutableMap<String, JSONObject> = HashMap()
        val skuDetailJson =
            JSONObject(
                "{\"productId\":\"coffee\",\"type\":\"inapp\",\"price\":\"$2.99\",\"price_amount_micros\":2990000,\"price_currency_code\":\"USD\",\"title\":\"coffee\",\"description\":\"Basic coffee \",\"skuDetailsToken\":\"detailsToken=\"}"
            )
        mockSkuDetailsMap["espresso"] = skuDetailJson

        // Test logging ready events can be added into map
        var newPurchaseDetailString =
            "{\"productId\":\"espresso\",\"purchaseToken\":\"token123\",\"purchaseTime\":%s,\"developerPayload\":null,\"packageName\":\"sample.packagename\"}"
        val currTime = System.currentTimeMillis()
        newPurchaseDetailString = String.format(newPurchaseDetailString, currTime)
        val newPurchaseDetailStringJson = JSONObject(newPurchaseDetailString)
        mockPurchaseDetailsMap.clear()
        mockPurchaseDetailsMap["espresso"] = newPurchaseDetailStringJson
        val result2 =
            InAppPurchaseLoggerManager.constructLoggingReadyMap(
                mockPurchaseDetailsMap, mockSkuDetailsMap,
                packageName
            )
        Assertions.assertThat(result2).isNotEmpty
        val expectedResult = String.format(
            "{\"productId\":\"espresso\",\"purchaseToken\":\"token123\",\"purchaseTime\":%s,\"developerPayload\":null,\"packageName\":\"sample.packagename\"}",
            currTime
        )
        Assertions.assertThat(result2.containsKey(expectedResult))
            .isTrue()
    }
}
