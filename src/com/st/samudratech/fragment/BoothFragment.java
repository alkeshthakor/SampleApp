package com.st.samudratech.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.st.samudratech.R;

public class BoothFragment extends Fragment {
	
  @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	  View view=inflater.inflate(R.layout.fragment_booth_layout, container,false);
      
      return view;
	 
}
}
