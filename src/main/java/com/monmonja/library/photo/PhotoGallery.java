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

package com.monmonja.library.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by almondjoseph on 12/2/15.
 */
public class PhotoGallery {
    public static final int REQUEST_SELECT_PHOTO = 1601;

    public static void dispatchPhotoPicker (Context context) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(photoPickerIntent, REQUEST_SELECT_PHOTO);
        }
    }
}
