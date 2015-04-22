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

package com.monmonja.library.storage;

import android.os.Environment;

import java.io.File;

/**
 * Created by almondjoseph on 9/2/15.
 */
public class ExternalStorage {
    public static File getFolder (String folderName) {
        File folderFile;
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            folderFile = null;
        } else {
            folderFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + folderName);
            if (!folderFile.exists()) {
                if (!folderFile.mkdir()) {
                    folderFile = null;
                }
            }
        }
        return folderFile;
    }

    public static File getPictureFolder(String folderName) {
        File folderFile;
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            folderFile = null;
        } else {
            folderFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + folderName);
            if (!folderFile.exists()) {
                if (!folderFile.mkdir()) {
                    folderFile = null;
                }
            }
        }
        return folderFile;
    }
}
