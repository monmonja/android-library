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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.monmonja.library.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by almondjoseph on 6/10/14.
 */
public class SocialNetworkAdapter extends RecyclerView.Adapter <SocialNetworkAdapter.SocialNetworkViewHolder> {

    private static ArrayList<SocialNetwork> socialNetworks;
    private final Context mContext;
    private ArrayList<SocialNetwork> mHasSocialNetworks;
    private ArrayList<SocialNetwork> mNotInstalledSocialNetworks;

    public String mAppExtra = "";
    private String imagePath = "";

    public SocialNetworkAdapter (Context context) {
        mContext = context;
        makeList();
    }

    private void makeList() {
        socialNetworks = new ArrayList<SocialNetwork>();
        mHasSocialNetworks = new ArrayList<SocialNetwork>();
        mNotInstalledSocialNetworks = new ArrayList<SocialNetwork>();

        checkSocialNetworkInstalled(new SocialNetwork("Instagram", "com.instagram.android", R.drawable.icon_instagram));
        checkSocialNetworkInstalled(new SocialNetwork("Twitter", "com.twitter.android", R.drawable.icon_twitter));
        checkSocialNetworkInstalled(new SocialNetwork("Facebook", "com.facebook.katana", R.drawable.icon_facebook));
        checkSocialNetworkInstalled(new SocialNetwork("Google+", "com.google.android.apps.plus", R.drawable.icon_gplus));
        checkSocialNetworkInstalled(new SocialNetwork("Tumblr", "com.tumblr", R.drawable.icon_tumblr));

        for (SocialNetwork socialNetwork: mHasSocialNetworks) {
            socialNetworks.add(socialNetwork);
        }
        for (SocialNetwork socialNetwork: mNotInstalledSocialNetworks) {
            socialNetworks.add(socialNetwork);
        }
    }

    private void checkSocialNetworkInstalled(SocialNetwork socialNetwork) {
        if (isPackageExisted(socialNetwork.packageName)) {
            mHasSocialNetworks.add(socialNetwork);
        } else {
            mNotInstalledSocialNetworks.add(socialNetwork);
        }
    }

    @Override
    public SocialNetworkViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_social_network, viewGroup, false);
        return new SocialNetworkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SocialNetworkViewHolder viewHolder, int position) {
        SocialNetwork socialNetwork = socialNetworks.get(position);
        viewHolder.iconImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick(Integer.parseInt(v.getTag().toString()));
            }
        });
        viewHolder.iconImageButton.setTag(position);
        viewHolder.iconImageButton.setImageResource(socialNetwork.iconResId);
        if (socialNetwork.isPackageExist == null) {
            socialNetwork.isPackageExist = isPackageExisted(socialNetwork.packageName);
        }

        viewHolder.iconImageButton.setEnabled(socialNetwork.enableButtons);
        if (socialNetwork.enableButtons) {
            viewHolder.iconImageButton.setColorFilter(null);
        } else {
            viewHolder.iconImageButton.setColorFilter(Color.argb(150, 0, 0, 0));

        }

        if (socialNetwork.isPackageExist) {
            viewHolder.iconImageButton.setAlpha(1f);
        } else {
            viewHolder.iconImageButton.setAlpha(0.5f);
        }

        viewHolder.nameTextView.setText(socialNetwork.name);
    }

    private void itemClick(int position) {
        SocialNetwork socialNetwork = socialNetworks.get(position);

        if (socialNetwork.isPackageExist) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            if (!mAppExtra.isEmpty()) {
                intent.putExtra(Intent.EXTRA_TEXT, mAppExtra);
            }
            if (imagePath != null && !imagePath.isEmpty()) {
                File media = new File(imagePath);
                Uri uri = Uri.fromFile(media);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
            }
            intent.setPackage(socialNetwork.packageName);
            mContext.startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + socialNetwork.packageName));
            mContext.startActivity(intent);
        }
    }

    public boolean isPackageExisted(String targetPackage){
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo info=pm.getPackageInfo(targetPackage,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return socialNetworks.size();
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setEnableButtons(boolean bool) {
        for (int i = 0; i < socialNetworks.size(); i++) {
            socialNetworks.get(i).enableButtons = bool;
        }
        notifyDataSetChanged();
    }

    public class SocialNetworkViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final ImageButton iconImageButton;

        public SocialNetworkViewHolder(View itemView) {
            super(itemView);
            iconImageButton = (ImageButton) itemView.findViewById(R.id.icon_image_button);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
        }
    }

    public void setAppExtra(String appExtra) {
        this.mAppExtra = appExtra;
    }
}
