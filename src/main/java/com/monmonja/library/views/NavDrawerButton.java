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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.monmonja.library.R;

/**
 * Created by almondjoseph on 5/9/14.
 */
public class NavDrawerButton extends LinearLayout {
    private final Context mContext;

    protected ImageView mImageView;
    protected TextView mTextView;

    public NavDrawerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private void init (AttributeSet attrs) {
        TypedArray attributesFromXmlLayout = mContext.obtainStyledAttributes(attrs, R.styleable.NavDrawerItem);

        Drawable iconDrawable = attributesFromXmlLayout.getDrawable(R.styleable.NavDrawerItem_navIcon);
        String label = attributesFromXmlLayout.getString(R.styleable.NavDrawerItem_navLabel);

        inflate(getContext(), R.layout.navdrawer_button, this);
        mImageView = (ImageView) findViewById(R.id.icon_image_view);
        mImageView.setImageDrawable(iconDrawable);

        mTextView = (TextView) findViewById(R.id.text_view);
        mTextView.setText(label);



    }


}
