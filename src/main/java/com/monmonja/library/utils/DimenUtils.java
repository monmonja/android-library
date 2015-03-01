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
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;

/**
 * Created by almondjoseph on 9/2/15.
 */
public class DimenUtils {
    public static float dpFromPx(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pixelsToSp(Context context, float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
    }

    public static int calculateWidthFromFontSize(String testString, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(testString, 0, testString.length(), bounds);
        return (int) Math.ceil(bounds.width()) + 4;
    }

    public static int calculateHeightFromFontSize(String testString, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(testString, 0, testString.length(), bounds);

        return (int) Math.ceil( bounds.height());
    }


}
