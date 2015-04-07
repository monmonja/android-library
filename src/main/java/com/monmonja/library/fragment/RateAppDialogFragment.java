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

package com.monmonja.library.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.monmonja.library.R;
import com.monmonja.library.utils.PlayServiceUtils;

/**
 * Created by almondjoseph on 18/11/14.
 */
public class RateAppDialogFragment extends DialogFragment implements View.OnClickListener {
    public static final String TAG = "RateAppDialogFragment";

    public String mAlertTitle;
    public String mAlertBody;
    private Button mDisableRateBtn;
    public String mMarketUri;
    private Button mRateBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = super.onCreateView(inflater, container, savedInstanceState);
        getDialog().setTitle(mAlertTitle);

        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_rate_app_dialog, container, false);
        }
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView bodyTextView = (TextView) view.findViewById(R.id.body_text_view);
        bodyTextView.setText(mAlertBody);

        mDisableRateBtn = (Button) view.findViewById(R.id.no_rate_btn);
        mDisableRateBtn.setOnClickListener(this);

        mRateBtn = (Button) view.findViewById(R.id.rate_btn);
        mRateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (v.equals(mDisableRateBtn)) {
            editor.putBoolean(PlayServiceUtils.PREF_RATE_APP_ENABLE, false);
            editor.apply();
            dismiss();
        } else if (v.equals(mRateBtn)) {
            editor.putBoolean(PlayServiceUtils.PREF_RATE_APP_ENABLE, false);
            editor.apply();
            dismiss();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mMarketUri));
            v.getContext().startActivity(intent);
        }
    }
}
