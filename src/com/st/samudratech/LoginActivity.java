package com.st.samudratech;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.st.samudratech.util.ConnectionDetector;
import com.st.samudratech.util.Constant;
import com.st.samudratech.util.Pref;
import com.st.samudratech.util.ServerConnector;

public class LoginActivity extends ActionBarActivity implements OnClickListener{

	private Toolbar toolbar;
	private Button loginButton;
	
	private EditText userIdEditText;
	private EditText passwordEditText;
	

	private ProgressDialog mProgressDialog;
    ConnectionDetector cd;
    ServerConnector connector;
    Context mContext;
    Resources rs;
    
    private CheckBox rememberMeCheckBox;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
		 if (toolbar != null) {
	            setSupportActionBar(toolbar);
	            getSupportActionBar().setDisplayShowTitleEnabled(false);
	      }
		 loginButton=(Button)findViewById(R.id.loginButton);
		 loginButton.setOnClickListener(this);
		 
		    mContext=this;
			Constant.CONTEXT=this;
			
			cd=new ConnectionDetector(mContext);
			connector=new ServerConnector();
			rs=getResources();
			mProgressDialog=new ProgressDialog(LoginActivity.this);
			mProgressDialog.setMessage("Please wait...");
			mProgressDialog.setIndeterminate(false);
			
			
			userIdEditText=(EditText)findViewById(R.id.userNameEditText);
			passwordEditText=(EditText)findViewById(R.id.passwordEditText);
		    
			
			rememberMeCheckBox=(CheckBox)findViewById(R.id.rememberMeCheckBox);

			rememberMeCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					// TODO Auto-generated method stub
					rememberMe();
				}
			});
			
			if(!Pref.getValue(Constant.PREF_USER_ID,"").equals("")){
				userIdEditText.setText(Pref.getValue(Constant.PREF_USER_ID,""));
				rememberMeCheckBox.setChecked(true);
			}else{
				rememberMeCheckBox.setChecked(false);
			}
			
		 
    }

    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		userIdEditText.setText("9909431965");
		passwordEditText.setText("dp1");
	}
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.loginButton:
			processAuthentication();
			
			break;
			
		}
	}
	
	private void rememberMe(){
		   if(rememberMeCheckBox.isChecked()){
			  Pref.setValue(Constant.PREF_USER_ID, userIdEditText.getText().toString());
			  Pref.setValue(Constant.PREF_PASSWORD, passwordEditText.getText().toString());
			}else{
				 Pref.setValue(Constant.PREF_USER_ID,"");
				 Pref.setValue(Constant.PREF_PASSWORD,"");
			}
	   }
	
private void processAuthentication(){
	if(userIdEditText.getText().length()>0&&passwordEditText.getText().length()>0){
		rememberMe();
		 if(cd.isConnectingToInternet()){
				String userID=userIdEditText.getText().toString();
				userID.trim();
				String password=passwordEditText.getText().toString();
				String urlString=Constant.HOST_LOGIN+Constant.LOGIN_SERVICE+"mobileno="+userID.trim()+"&password="+password.trim()+"&loginType=1";
				new UserLoginTask().execute(urlString);
				
			  }else{
				  Toast.makeText(mContext,rs.getString(R.string.net_not_available),Toast.LENGTH_SHORT).show();
				  return;
			  }
	}else{
		Toast.makeText(getApplicationContext(),"Please enter userid or password",Toast.LENGTH_SHORT).show();
	}
  }

class UserLoginTask extends AsyncTask<String,Void,JSONObject>{
    @Override
    protected void onPreExecute() {
    	super.onPreExecute();
    	mProgressDialog.show();
    }
	@Override
	protected JSONObject doInBackground(String... param) {
		return connector.getServerResponse(param[0]);
		
	}
	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		mProgressDialog.dismiss();
		
		if(result!=null){
			try {
				Log.d("Data",result.toString());
				if(result.has("action")&&result.getBoolean("status")==true){
					try {
						JSONObject values=result.getJSONObject("values");
						if(values!=null){
							int boothid=values.getInt("boothId");
							Pref.setValue(Constant.PREF_BOOTH_ID,""+boothid);
							Pref.setValue(Constant.PREF_BOOTH_NAME,values.getString("boothName"));
							Pref.setValue(Constant.PREF_BOOTH_BALANCE,values.getString("openingDepo"));
							
							Intent homeIntent=new Intent(getApplicationContext(),HomeActivity.class);
							startActivity(homeIntent);	
						}
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

}
