package com.kekexun.soochat.activity.main;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;


public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private List<Fragment> fragments = null;
	private FragmentManager fm;

	public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fm = fm;
		this.fragments = fragments;
	}
	
	@Override
	public Fragment getItem(int location) {
		return fragments.get(location);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}
