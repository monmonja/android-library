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

package com.monmonja.library.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.monmonja.library.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by almondjoseph on 17/10/14.
 */
public class MonmonjaAppsView extends RelativeLayout implements View.OnClickListener {
    private ImageView mAppLogoImageView;
    private TextView mAppDescriptionTxt;
    private TextView mAppNameTxt;
    private ImageButton mPlayStoreBtn;
    private String mPlayStoreUri;
    private ImageButton mAppStoreBtn;
    private String mAppStoreUri;

    public MonmonjaAppsView(Context context) {
        super(context);
        init(null);
    }

    private void init (AttributeSet attrs) {
        inflate(getContext(), R.layout.monmonja_apps_view, this);

        mAppLogoImageView = (ImageView) findViewById(R.id.app_logo);
        mAppNameTxt = (TextView) findViewById(R.id.app_name);
        mAppDescriptionTxt = (TextView) findViewById(R.id.app_description);

        mPlayStoreBtn = (ImageButton) findViewById(R.id.playstore_image_button);
        mPlayStoreBtn.setOnClickListener(this);
        mAppStoreBtn = (ImageButton) findViewById(R.id.appstore_image_button);
        mAppStoreBtn.setOnClickListener(this);
    }

    public void setName (String name) {
        mAppNameTxt.setText(name);
    }


    public void setDescription(String description) {
        mAppDescriptionTxt.setText(description);
    }

    public void setIconPath(String iconPath) {
        new GetBitmapAsync(iconPath).execute();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.playstore_image_button) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mPlayStoreUri));
            v.getContext().startActivity(intent);
        } else if (v.getId() == R.id.appstore_image_button) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mAppStoreUri));
            v.getContext().startActivity(intent);
        }
    }

    private class GetBitmapAsync extends AsyncTask <Void, Void, Bitmap> {
        private String mUrl;
        public GetBitmapAsync(String url) {
            mUrl = url;
        }
        @Override
        protected Bitmap doInBackground(Void... params) {
            return getBitmapFromURL(mUrl);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mAppLogoImageView.setImageBitmap(bitmap);
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPlayStoreUri(String playStoreUri) {
        mPlayStoreUri = playStoreUri;
        mPlayStoreBtn.setVisibility(VISIBLE);
    }

    public void setAppStoreUri(String appStoreUri) {
        mAppStoreUri = appStoreUri;
        mAppStoreBtn.setVisibility(VISIBLE);
    }
}
