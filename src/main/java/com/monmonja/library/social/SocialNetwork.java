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

package com.monmonja.library.social;

import android.net.Uri;

/**
 * Created by almondjoseph on 6/10/14.
 */
public class SocialNetwork {
    public String name;
    public String packageName;
    public int iconResId;
    public Boolean isPackageExist;
    public boolean enableButtons = true;

    public SocialNetwork (String name, String packageName, int iconResId) {
        this.name = name;
        this.packageName = packageName;
        this.iconResId = iconResId;
    }
}
