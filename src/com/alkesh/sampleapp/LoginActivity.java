package com.alkesh.sampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends ActionBarActivity implements OnClickListener{

	private Toolbar toolbar;
	private Button loginButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
		 if (toolbar != null) {
	            setSupportActionBar(toolbar);
	      }
		 loginButton=(Button)findViewById(R.id.loginButton);
		 loginButton.setOnClickListener(this);
		 
		 
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.loginButton:
			Intent homeIntent=new Intent(getApplicationContext(),HomeActivity.class);
			startActivity(homeIntent);
			
			break;
			
		}
	}
}
