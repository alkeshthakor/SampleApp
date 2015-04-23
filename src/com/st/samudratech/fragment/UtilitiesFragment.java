package com.st.samudratech.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.st.samudratech.R;
import com.st.samudratech.util.IpAddressValidator;

public class UtilitiesFragment extends Fragment {

	    private Button saveConfiguration;
	    
	    private EditText serverIPEditText;
	    private EditText serverInstanceEditText;
	    private EditText userNameEditText;
	    private EditText passwordEditText;
	    
	    IpAddressValidator ipAddressFormatValidator;
	    
  @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	  View view=inflater.inflate(R.layout.fragment_utilities_layout, container,false);
      
	  ipAddressFormatValidator = new IpAddressValidator();
		
	  saveConfiguration=(Button)view.findViewById(R.id.saveServerconfigButton);
	  
	  //saveConfiguration.setOnClickListener(getActivity().this);
		
		serverIPEditText=(EditText)view.findViewById(R.id.editText_ipaddress);
		serverInstanceEditText=(EditText)view.findViewById(R.id.editText_serverinstance);
		userNameEditText=(EditText)view.findViewById(R.id.editText_server_username);
		passwordEditText=(EditText)view.findViewById(R.id.editText_server_password);
		
		
      return view;
	 
}

  
  
}
