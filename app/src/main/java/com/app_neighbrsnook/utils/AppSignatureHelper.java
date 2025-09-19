package com.app_neighbrsnook.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppSignatureHelper extends ContextWrapper {

    public AppSignatureHelper(Context context) {
        super(context);
    }

    public List<String> getAppSignatures() {
        ArrayList<String> appCodes = new ArrayList<>();
        try {
            String packageName = getPackageName();
            PackageManager pm = getPackageManager();
            Signature[] signatures;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES);
                if (packageInfo.signingInfo != null) {
                    signatures = packageInfo.signingInfo.getApkContentsSigners();
                } else {
                    signatures = packageInfo.signatures;
                }
            } else {
                PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
                signatures = packageInfo.signatures;
            }

            for (Signature signature : signatures) {
                String hash = getHash(packageName, signature.toCharsString());
                if (hash != null) appCodes.add(hash);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appCodes;
    }

    private static String getHash(String packageName, String signature) {
        try {
            String appInfo = packageName + " " + signature;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(appInfo.getBytes(StandardCharsets.UTF_8));
            byte[] hashSignature = md.digest();
            byte[] trimmed = Arrays.copyOfRange(hashSignature, 0, 9);
            String base64Hash = Base64.encodeToString(trimmed, Base64.NO_PADDING | Base64.NO_WRAP);
            if (base64Hash.length() >= 11) {
                return base64Hash.substring(0, 11);
            } else {
                return base64Hash;
            }
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
