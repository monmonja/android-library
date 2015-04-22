package com.monmonja.library.views;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * http://stackoverflow.com/questions/3495890/how-can-i-put-a-listview-into-a-scrollview-without-it-collapsing
 */
public class StretchedLinearLayout extends LinearLayout {
    private static final String TAG = "StretchedListView";
    private final DataSetObserver mDataSetObserver;
    private ListAdapter mAdapter;
    private String mEmptyText = "No data";


    public StretchedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        this.mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                syncDataFromAdapter();
                super.onChanged();
            }

            @Override
            public void onInvalidated() {
                syncDataFromAdapter();
                super.onInvalidated();
            }
        };
    }

    public String getEmptyText() {
        return mEmptyText;
    }

    public void setEmptyText(String emptyText) {
        this.mEmptyText = emptyText;
    }

    public void setEmptyText(int resId) {
        this.mEmptyText = getResources().getString(resId);
    }

    public void setAdapter(ListAdapter adapter) {
        this.mAdapter = adapter;
        if (adapter != null) {
            this.mAdapter.registerDataSetObserver(mDataSetObserver);
        } else {
            this.mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        syncDataFromAdapter();
    }

    public ListAdapter getAdapter() {
        return mAdapter;
    }

    private void syncDataFromAdapter() {
        removeAllViews();
        if (mAdapter != null) {
            int count = mAdapter.getCount();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    View view = mAdapter.getView(i, null, this);
                    addView(view);
                }
            } else {
                Resources r = getContext().getResources();
                int marginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, r.getDisplayMetrics());

                TextView textView = new TextView(getContext());
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = marginPx;
                layoutParams.rightMargin = marginPx;
                textView.setLayoutParams(layoutParams);
                textView.setText(mEmptyText);
                addView(textView);
            }
        }
    }


    public int getCount() {
        return mAdapter != null ? mAdapter.getCount() : 0;
    }
}