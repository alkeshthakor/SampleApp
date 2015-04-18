package com.st.samudratech.fragment;

import com.alkesh.sampleapp.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BoothFragment extends Fragment {
	
  @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	  View view=inflater.inflate(R.layout.fragment_home_layout, container,false);
      
      return view;
	 
}
}
