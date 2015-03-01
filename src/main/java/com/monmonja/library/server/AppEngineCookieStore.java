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

package com.monmonja.library.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by almondjoseph on 20/10/14.
 *
 * http://stackoverflow.com/questions/16680701/using-cookies-with-android-volley-library
 *
 * http://alvinalexander.com/java/jwarehouse/commons-httpclient-4.0.3/httpclient/src/test/java/org/apache/http/impl/client/TestBasicCookieStore.java.shtml
 *
 * http://www.java2s.com/Tutorial/Java/0320__Network/CookieStore.htm
 */
public class AppEngineCookieStore implements CookieStore {
    private Map<URI, List<HttpCookie>> map = new HashMap<URI, List<HttpCookie>>();

    public static String COOKIE_NAME_SESSION  = "session";
    public static String PREF_COOKIE_ID  = "cookie_id";
    private final Context mContext;

    public AppEngineCookieStore(Context context) {
        mContext = context;
    }

    public void add(URI uri, HttpCookie cookie) {
        List<HttpCookie> cookies = map.get(uri);
        if (cookies == null) {
            cookies = new ArrayList<HttpCookie>();
            map.put(uri, cookies);
        }
        cookies.add(cookie);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (cookie.getName().equals(COOKIE_NAME_SESSION)) {
            editor.putString(PREF_COOKIE_ID, cookie.getValue());
            editor.apply();
        }
    }

    public List<HttpCookie> get(URI uri) {
        List<HttpCookie> cookies = map.get(uri);
        if (cookies == null) {
            cookies = new ArrayList<HttpCookie>();

            // add session from pref
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String sessionFromPref = sharedPreferences.getString(PREF_COOKIE_ID, "");
            if (!sessionFromPref.isEmpty()) {
                cookies.add(new HttpCookie(COOKIE_NAME_SESSION, sessionFromPref));
            }

            map.put(uri, cookies);
        }

        return cookies;
    }

    public List<HttpCookie> getCookies() {
        Collection<List<HttpCookie>> values = map.values();
        List<HttpCookie> result = new ArrayList<HttpCookie>();
        for (List<HttpCookie> value : values) {
            result.addAll(value);
        }

        return result;
    }

    public List<URI> getURIs() {
        Set<URI> keys = map.keySet();
        return new ArrayList<URI>(keys);

    }

    public boolean remove(URI uri, HttpCookie cookie) {
        List<HttpCookie> cookies = map.get(uri);
        if (cookies == null) {
            return false;
        }
        return cookies.remove(cookie);
    }

    public boolean removeAll() {
        map.clear();
        return true;
    }
}



//    @Override
//    public synchronized void addCookie(Cookie cookie) {
//        super.addCookie(cookie);
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        if (cookie.getName().equals(COOKIE_NAME_SESSION)) {
//            editor.putString(PREF_COOKIE_ID, cookie.getValue());
//            editor.commit();
//        }
//    }
//
//    @Override
//    public synchronized List<Cookie> getCookies() {
//        List<Cookie> cookies = new ArrayList<Cookie>();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//        String sessionFromPref = sharedPreferences.getString(PREF_COOKIE_ID, "");
//        if (!sessionFromPref.isEmpty()) {
//            cookies.add(new BasicClientCookie(COOKIE_NAME_SESSION, sessionFromPref));
//        }
//        return super.getCookies();
//    }
