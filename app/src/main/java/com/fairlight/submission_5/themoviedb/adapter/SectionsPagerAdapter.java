package com.fairlight.submission_5.themoviedb.adapter;

import android.content.Context;

import com.fairlight.submission_5.themoviedb.R;
import com.fairlight.submission_5.themoviedb.ui.favorites.TabbedFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab_text_1,
            R.string.tab_text_2
    };

    public SectionsPagerAdapter(Context mContext, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //fix after leaving favorite fragment in bottom nav view : instantiate fragment of index 0 first
        Fragment fragment = TabbedFragment.newInstance(0);

        switch (position) {
            case 0:
                fragment = TabbedFragment.newInstance(0);
                break;
            case 1:
                fragment = TabbedFragment.newInstance(1);
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}
