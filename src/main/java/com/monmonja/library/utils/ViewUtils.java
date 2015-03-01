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

import android.content.Context;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.monmonja.library.R;
import com.monmonja.library.views.MonmonjaAppsView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by almondjoseph on 18/10/14.
 */
public class ViewUtils {
    public static final String MONMONJA_APPS_URL = "http://monmonja-labs-hrd.appspot.com/monmonja-apps-json";

    public static void makeToast (Context context, int resId) {
        TypedValue tv = new TypedValue();
        int offsetY = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            offsetY = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }

        Toast toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = toast.getView();
        view.setBackgroundColor(context.getResources().getColor(R.color.toast_background));
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextColor(context.getResources().getColor(android.R.color.white));
        toast.show();
    }



    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static class GetMonmonjaApps extends AsyncTask<String, Void, JSONArray> {
        private final Context mContext;
        private final LinearLayout mAppsLinearLayout;

        public GetMonmonjaApps(Context context, LinearLayout appsLinearLayout) {
            mContext = context;
            mAppsLinearLayout = appsLinearLayout;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            String response;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(MONMONJA_APPS_URL);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                return new JSONArray(response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result)
        {
            super.onPostExecute(result);

            if (result != null) {
                try {
                    mAppsLinearLayout.removeAllViews();
                    for (int i = 0;i < result.length(); i++) {
                        JSONObject jsonObject = result.getJSONObject(i);
                        MonmonjaAppsView monmonjaAppsView = new MonmonjaAppsView(mContext);
                        monmonjaAppsView.setName(jsonObject.getString("name"));
                        monmonjaAppsView.setDescription(jsonObject.getString("description"));
                        monmonjaAppsView.setIconPath(jsonObject.getString("icon"));


                        JSONObject market = jsonObject.getJSONObject("market");
                        JSONObject android = market.optJSONObject("android");
                        if (android != null) {
                            monmonjaAppsView.setPlayStoreUri(android.getString("link"));
                        }
                        JSONObject ios = market.optJSONObject("ios");
                        if (ios != null) {
                            monmonjaAppsView.setAppStoreUri(ios.getString("link"));
                        }
                        mAppsLinearLayout.addView(monmonjaAppsView);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
//                Toast.makeText(Calendar.this, "Network Problem", Toast.LENGTH_LONG).show();
            }
        }

    }
}
