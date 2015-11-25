package com.kekexun.soochat.activity.main.addrbook;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kekexun.soochat.activity.BaseFragment;
import com.kekexun.soochat.activity.R;

public class AddrBookFragment extends BaseFragment {
	
	private List groups;
	
	public AddrBookFragment() {
		
	}
	
	public AddrBookFragment(List groups) {
		this.groups = groups;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_main_addrbook, null);
	}
	
}
