package com.st.samudratech.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.st.samudratech.R;
import com.st.samudratech.util.Constant;
import com.st.samudratech.util.IpAddressValidator;
import com.st.samudratech.util.Pref;

public class UtilitiesFragment extends Fragment {

	    private Button saveConfiguration;
	    
	    private EditText serverIPEditText;
	    private EditText serverInstanceEditText;
	    private EditText userNameEditText;
	    private EditText passwordEditText;
	    
	    IpAddressValidator ipAddressFormatValidator;
	    
	    private Activity mActivity;
	    
	    @Override
	 public void onAttach(Activity activity) {
	    // TODO Auto-generated method stub
	    super.onAttach(activity);
	 
	      mActivity=activity;
	      
	}
	    
  @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	  
	// TODO Auto-generated method stub
	  View view=inflater.inflate(R.layout.fragment_utilities_layout, container,false);
      
	  ipAddressFormatValidator = new IpAddressValidator();
		
	  saveConfiguration=(Button)view.findViewById(R.id.saveServerconfigButton);
	  		
		serverIPEditText=(EditText)view.findViewById(R.id.editText_ipaddress);
		serverInstanceEditText=(EditText)view.findViewById(R.id.editText_serverinstance);
		userNameEditText=(EditText)view.findViewById(R.id.editText_server_username);
		passwordEditText=(EditText)view.findViewById(R.id.editText_server_password);
		
		saveConfiguration.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveServerConfiguration();	
			}
		});
		
		setServerConfiguration();
		
      return view;
	 
}

  
  public void setServerConfiguration(){
		serverIPEditText.setText(Pref.getValue(Constant.PREF_SERVER_IP,"0.0.0.0"));
		serverInstanceEditText.setText(Pref.getValue(Constant.PREF_SERVER_INSTANCE,"RMRD"));
		userNameEditText.setText(Pref.getValue(Constant.PREF_USER_ID_SERVER,""));
		passwordEditText.setText(Pref.getValue(Constant.PREF_PASSWORD_SERVER,""));
	}
  
  
  private void saveServerConfiguration(){
		
		if(serverIPEditText.getText().toString().length()>0){
			if(ipAddressFormatValidator.validate(serverIPEditText.getText().toString())){
				Pref.setValue(Constant.PREF_SERVER_IP,serverIPEditText.getText().toString());	
			}else{
				Toast.makeText(mActivity.getApplicationContext(),"Invalid ip address",Toast.LENGTH_SHORT).show();
				return;
			}
		}else{
			Toast.makeText(mActivity.getApplicationContext(),"Please enter server ip address",Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(serverInstanceEditText.getText().toString().length()>0){
			Pref.setValue(Constant.PREF_SERVER_INSTANCE,serverInstanceEditText.getText().toString());	
		}else{
			Toast.makeText(mActivity.getApplicationContext(),"Please enter server instance",Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(userNameEditText.getText().toString().length()>0){
			Pref.setValue(Constant.PREF_USER_ID_SERVER,userNameEditText.getText().toString());	
		}else{
			Toast.makeText(mActivity.getApplicationContext(),"Please enter user name",Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(passwordEditText.getText().toString().length()>0){
			Pref.setValue(Constant.PREF_PASSWORD_SERVER,passwordEditText.getText().toString());	
		}else{
			Toast.makeText(mActivity.getApplicationContext(),"Please enter user name",Toast.LENGTH_SHORT).show();
			return;
		}
		
		Toast.makeText(mActivity.getApplicationContext(),"Server configuration save successfully",Toast.LENGTH_SHORT).show();
		
		
		Constant.HOST="http://"+Pref.getValue(Constant.PREF_SERVER_IP,"0.0.0.0")+":8080/"+Pref.getValue(Constant.PREF_SERVER_INSTANCE,"RMRD")+"/reports/";
		//Constant.HOST_REPORT="http://"+Pref.getValue(Constant.PREF_SERVER_IP,"0.0.0.0")+":8080/"+Pref.getValue(Constant.PREF_SERVER_INSTANCE,"RMRD")+"/Report/";
		//Constant.HOST_MASTER="http://"+Pref.getValue(Constant.PREF_SERVER_IP,"0.0.0.0")+":8080/"+Pref.getValue(Constant.PREF_SERVER_INSTANCE,"RMRD")+"/";

		Log.d("Host",Constant.HOST);
		//Log.d("Host_Report",Constant.HOST_REPORT);
		//Log.d("Host_Master",Constant.HOST_MASTER);
	}
}
