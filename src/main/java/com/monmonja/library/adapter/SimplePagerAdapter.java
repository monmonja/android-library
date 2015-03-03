package com.monmonja.library.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.monmonja.library.views.IFragmentInPager;

import java.util.List;
import java.util.Vector;

/**
 * Created by almondjoseph on 3/3/15.
 */
public class SimplePagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragments;

    public SimplePagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new Vector<Fragment>();
    }


    public void addItem(Fragment frag) {
        this.fragments.add(frag);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (getItem(position) instanceof IFragmentInPager) {
            return ((IFragmentInPager) getItem(position)).getTitle();
        } else {
            return "Implement IFragmentInPager";
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int pos) {
        final Fragment frag = (Fragment) super.instantiateItem(container, pos);
        this.fragments.set(pos, frag);

        return frag;
    }

    @Override
    public Fragment getItem(int pos) {
        return this.fragments.get(pos);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
