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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by almondjoseph on 8/2/15.
 */
public class PhotoUtils {

    public static File getTempFile(Context context) {
        if (!context.getExternalFilesDir("temp").exists()) {
            context.getExternalFilesDir("temp").mkdir();
        }
        return new File(context.getExternalFilesDir("temp"), "temp.png");
    }

    public static Bitmap getBitmapScaleWithHeight(Context context, Uri sourceImageUri, int height) throws FileNotFoundException {
        Bitmap selectedBitmap = null;
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        if (sourceImageUri.toString().contains("content://")) {
            final InputStream imageStream = context.getContentResolver().openInputStream(sourceImageUri);
            BitmapFactory.decodeStream(imageStream, null, bmpFactoryOptions);
        } else {
            BitmapFactory.decodeFile(sourceImageUri.getPath(), bmpFactoryOptions);
        }

        bmpFactoryOptions.inSampleSize = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        bmpFactoryOptions.inJustDecodeBounds = false;

        if (sourceImageUri.toString().contains("content://")) {
            final InputStream imageStream = context.getContentResolver().openInputStream(sourceImageUri);
            selectedBitmap = BitmapFactory.decodeStream(imageStream, null, bmpFactoryOptions);
        } else {
            selectedBitmap = BitmapFactory.decodeFile(sourceImageUri.getPath(), bmpFactoryOptions);
        }
        return selectedBitmap;
    }
}
