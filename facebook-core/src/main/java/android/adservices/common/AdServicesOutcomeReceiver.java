/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package android.adservices.common;

import androidx.annotation.NonNull;

public interface AdServicesOutcomeReceiver<R, E extends Throwable> {
    void onResult(R result);
    default void onError(@NonNull E error) {}
}
