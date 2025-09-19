package com.app_neighbrsnook.registration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    public SmsBroadcastReceiverListener smsBroadcastReceiverListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SmsRetriever.SMS_RETRIEVED_ACTION)) {
            Bundle extras = intent.getExtras();
            if (extras == null) {
                if (smsBroadcastReceiverListener != null) {
                    smsBroadcastReceiverListener.onFailure();
                }
                return;
            }
            Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (smsRetrieverStatus.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get the consent intent
                    Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                    if (smsBroadcastReceiverListener != null) {
                        smsBroadcastReceiverListener.onSuccess(consentIntent);
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    if (smsBroadcastReceiverListener != null) {
                        smsBroadcastReceiverListener.onFailure();
                    }
                    break;
                default:
                    if (smsBroadcastReceiverListener != null) {
                        smsBroadcastReceiverListener.onFailure();
                    }
                    break;
            }
        }
    }

    public interface SmsBroadcastReceiverListener {
        void onSuccess(Intent intent);
        void onFailure();
    }
}