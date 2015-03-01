/*
 * Copyright 2014 Monmonja. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.monmonja.library.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentActivity;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.monmonja.library.fragment.RateAppDialogFragment;

import java.util.List;

/**
 * Created by almondjoseph on 10/6/14.
 */
public class PlayServiceUtils {
    public static String PREF_RATE_APP = "pref_rate_app_pass_by_count";
    public static String PREF_RATE_APP_ENABLE = "pref_rate_app_pass_by";

    public static void rateAppDialog(FragmentActivity activity, int passByThenAlertCount, String alertTitle, String alertBody, String marketUri) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        int rateAppBy = sharedPreferences.getInt(PREF_RATE_APP, 1);
        boolean rateAppByEnable = sharedPreferences.getBoolean(PREF_RATE_APP_ENABLE, true);

        if (rateAppByEnable) {
            if (rateAppBy % passByThenAlertCount == 0) {
                RateAppDialogFragment fragment = new RateAppDialogFragment();
                fragment.mAlertTitle = alertTitle;
                fragment.mAlertBody = alertBody;
                fragment.mMarketUri = marketUri;
                fragment.show(activity.getSupportFragmentManager(), RateAppDialogFragment.TAG);
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(PREF_RATE_APP, ++rateAppBy);
                editor.apply();
            }
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean(PREF_RATE_APP_ENABLE, true); // debug mode
        editor.apply();
    }

    public static AdView createAdView(Activity activity, String adUnitId, AdSize adSize, LinearLayout admobView) {
        return createAdView(activity, adUnitId, adSize, admobView, null);
    }

    public static AdView createAdView(Activity activity, String adUnitId, AdSize adSize, LinearLayout admobView, String keywords) {
        // Create an ad.
        final AdView adView = new AdView(activity);
        adView.setAdSize(adSize);
        adView.setAdUnitId(adUnitId);

        // Add the AdView to the view hierarchy. The view will have no size
        // until the ad is loaded.
        admobView.addView(adView);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest.Builder request = new AdRequest.Builder();
        if (keywords != null) {
            final String[] keys = keywords.split(",");
            final int total = keys.length;
            for (int i = 0; i < total; i ++) {
                request.addKeyword(keys[i]);
            }
        }
        // Add your debug devices here, adb logcat Ads:V *:S
        request.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        request.addTestDevice("71C3880FBC9613F0105CC31B143E9B4E"); // oneplus one - almond
        request.addTestDevice("7D029ED128C55FC1FD82213D1957E376"); // mi3 - mom
        request.addTestDevice("10C5F1C7812038544A75DC84EDF4493B"); // nexus 7 - almond

        adView.loadAd(request.build());

        return adView;
    }

    public static boolean isSpeechRecognitionActivityPresented(Activity callerActivity) {
        try {
            // getting an instance of package manager
            PackageManager pm = callerActivity.getPackageManager();
            // a list of activities, which can process speech recognition Intent
            List activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

            if (activities.size() != 0) {    // if list not empty
                return true;                // then we can recognize the speech
            }
        } catch (Exception e) {

        }

        return false; // we have no activities to recognize the speech
    }
}
