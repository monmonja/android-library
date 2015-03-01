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

package com.monmonja.library.analytics;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by almondjoseph on 23/10/14.
 */
public class GoogleAnalyticsBase extends AnalyticsBase {
    private static Tracker mTracker;
    private final String mPropertyId;


    public GoogleAnalyticsBase(Application application, AnalyticsConfig analyticsConfig) {
        super(application, analyticsConfig);
        mPropertyId = analyticsConfig.getAccountId();
    }

    @Override
    public void trackAppEntrance() {
        trackScreen("App Entrance");
    }

    @Override
    public void trackScreen(String screenName) {
        // Get tracker.
        Tracker t = getTracker();

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public void eventTrack(int categoryId, int actionId, int labelId) {
        // Get tracker.
        Tracker t = getTracker();

        // Send a screen view.
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(getResources().getString(categoryId))
                .setAction(getResources().getString(actionId))
                .setLabel(getResources().getString(labelId))
                .build());
    }

    @Override
    public void eventTrack(int categoryId, int actionId, String labelId) {
        // Get tracker.
        Tracker t = getTracker();

        // Send a screen view.
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(getResources().getString(categoryId))
                .setAction(getResources().getString(actionId))
                .setLabel(labelId)
                .build());
    }

    @Override
    public void eventTrack(String categoryId, String actionId, String labelId) {
        // Get tracker.
        Tracker t = getTracker();

        // Send a screen view.
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(categoryId)
                .setAction(actionId)
                .setLabel(labelId)
                .build());
    }

    synchronized Tracker getTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(mApplication);
            mTracker = analytics.newTracker(mPropertyId);
        }
        return mTracker;
    }

}