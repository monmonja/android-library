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

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.monmonja.library.R;

/**
 * Created by almondjoseph on 5/9/14.
 */
public class EditLabelText extends LinearLayout {
    private final Context mContext;

    private TextView labelTxt;
    private EditText editText;

    private String mHintText;

    private int mFocusColor;
    private int mBlurColor;


    public EditLabelText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private void init (AttributeSet attrs) {
        TypedArray attributesFromXmlLayout = mContext.obtainStyledAttributes(attrs, R.styleable.EditLabelText);

        mHintText = attributesFromXmlLayout.getString(R.styleable.EditLabelText_hint);
        mFocusColor = attributesFromXmlLayout.getColor(R.styleable.EditLabelText_focusColor, android.R.color.holo_blue_bright);
        mBlurColor = attributesFromXmlLayout.getColor(R.styleable.EditLabelText_blurColor, android.R.color.darker_gray);
        int characterLimit = attributesFromXmlLayout.getInteger(R.styleable.EditLabelText_characterLimit, -1);
        int lines = attributesFromXmlLayout.getInteger(R.styleable.EditLabelText_lines, 1);

        String label = attributesFromXmlLayout.getString(R.styleable.EditLabelText_label);

        inflate(getContext(), R.layout.monmonja_edit_label_text, this);
        editText = (EditText) findViewById(R.id.edit_text);
        editText.setHint(mHintText);
        editText.setOnFocusChangeListener(getFocusChangeListener());
        if (lines > 1) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            editText.setLines(lines);
        }
        if (characterLimit > -1) {
            InputFilter[] filters = new InputFilter[1];
            filters[0] = new InputFilter.LengthFilter(characterLimit);
            editText.setFilters(filters);
        }

        labelTxt = (TextView) findViewById(R.id.label_textview);
        labelTxt.setText(label);
        labelTxt.setPadding(editText.getPaddingLeft(), 0, 0, 0);
        labelTxt.setTextColor(mBlurColor);

    }

    private OnFocusChangeListener getFocusChangeListener() {
        return new OnFocusChangeListener() {
            ValueAnimator mToBlurAnimation = getFocusAnimation(mFocusColor, mBlurColor);
            ValueAnimator mToFocusAnimation = getFocusAnimation(mBlurColor, mFocusColor);

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ValueAnimator lColorAnimation = hasFocus ? mToFocusAnimation : mToBlurAnimation;
                lColorAnimation.setDuration(300);
                lColorAnimation.start();
            }

            private ValueAnimator getFocusAnimation(int fromColor, int toColor) {
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        labelTxt.setTextColor((Integer) animator.getAnimatedValue());
                    }
                });
                return colorAnimation;
            }
        };
    }

    public String getValue() {
        return editText.getText().toString();
    }

    public void setValue(String value) {
        editText.setText(value);
    }
}
