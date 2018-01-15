package com.example.dimazatolokin.filemanager.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    private List<Fragment> fragments = new ArrayList<>();

    public MainPagerAdapter(Context context, FragmentManager fragmentManager, List<Fragment> fragments) {
        super(fragmentManager);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /*@Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.category);
        } else {
            return context.getString(R.string.calendar);
        }
    }*/

    /*@Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragments.put(position, fragment);
        return fragment;
    }*/

    public Fragment getLeftFragment() {
        return fragments.get(0);
    }

    public Fragment getRightFragment() {
        return fragments.get(1);
    }
}
