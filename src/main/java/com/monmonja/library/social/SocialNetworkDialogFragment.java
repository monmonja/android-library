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

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.monmonja.library.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by almondjoseph on 22/11/14.
 */
public class SocialNetworkDialogFragment extends DialogFragment implements View.OnClickListener {
    public static final String TAG = "SocialNetworkDialogFragment";
    protected static final int RESULT_SELECT_PHOTO = 0;
    protected static final int REQUEST_IMAGE_CAPTURE = 1;

    private String shareText = "";
    private RecyclerView mRecyclerView;
    private Button mTakePhotoBtn;
    private Button mPickPhotoBtn;
    private TextView mLoadingTxt;

    protected String mTempTakePicturePath;
    private SocialNetworkAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = super.onCreateView(inflater, container, savedInstanceState);
        getDialog().setTitle("Share");

        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_social_network_dialog, container, false);
        }
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.social_recycler_view);

        mLoadingTxt = (TextView) view.findViewById(R.id.loading_image);

        mTakePhotoBtn = (Button) view.findViewById(R.id.take_photo_btn);
        mTakePhotoBtn.setOnClickListener(this);

        mPickPhotoBtn = (Button) view.findViewById(R.id.pick_photo_btn);
        mPickPhotoBtn.setOnClickListener(this);
        setupRecyclerView();
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new SocialNetworkAdapter(getActivity());
        mAdapter.setAppExtra(shareText);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mTakePhotoBtn)) {
            dispatchTakePictureIntent();
        } else if (v.equals(mPickPhotoBtn)) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_SELECT_PHOTO);
        }
    }

    /** * Start the camera by dispatching a camera intent. */
    protected void dispatchTakePictureIntent() {
        PackageManager packageManager = getActivity().getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                File tempFile = getTempCaptureImgPath();
                mTempTakePicturePath = tempFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException ex) {

            }

        }
    }

    public File getTempCaptureImgPath() throws IOException {
        String imageFileName = "tmp_capture";
        return new File(Environment.getExternalStorageDirectory() + File.separator + imageFileName + ".snr");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SELECT_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    mAdapter.setEnableButtons(false);
                    mLoadingTxt.setVisibility(View.VISIBLE
                    );
                    new GetBitmapAsync(data.getData()).execute();
                }
                break;

            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    mAdapter.setImagePath(mTempTakePicturePath);
                }
        }
    }

    private class GetBitmapAsync extends AsyncTask<Void, Void, String> {
        private Uri mImagePath;
        public GetBitmapAsync(Uri imagePath) {
            mImagePath = imagePath;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                if (mImagePath.toString().contains("content://")) {
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(mImagePath);
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream, null, null);

                    OutputStream outputStream = getActivity().getContentResolver().openOutputStream(Uri.fromFile(getTempCaptureImgPath()));
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                    imageStream.close();
                    outputStream.close();
                    bitmap.recycle();
                    return getTempCaptureImgPath().toString();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            if (path != null) {
                mAdapter.setImagePath(path);
            }
            mLoadingTxt.setVisibility(View.GONE);
            mAdapter.setEnableButtons(true);
        }
    }
}
