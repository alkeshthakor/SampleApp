package com.st.samudratech.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.st.samudratech.OrderPlaceActivity;
import com.st.samudratech.R;

public class BoothFragment extends Fragment implements OnClickListener {

private Button orderPlaceButton;
private Button previousOrderButton;
private Button onlinePaymentButton;
private Button accountStateButton;


private Activity mActivity;

@Override
public void onAttach(Activity activity) {
    super.onAttach(activity);

  mActivity=activity;
}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	
	    View view=inflater.inflate(R.layout.fragment_booth_layout, container,false);
	    
       orderPlaceButton=(Button)view.findViewById(R.id.orderPlaceButton);
       previousOrderButton=(Button)view.findViewById(R.id.previousOrderButton);
       onlinePaymentButton=(Button)view.findViewById(R.id.onlineOrderButton);
       accountStateButton=(Button)view.findViewById(R.id.accountStatementButton);
        
       orderPlaceButton.setOnClickListener(this);
       previousOrderButton.setOnClickListener(this);
       onlinePaymentButton.setOnClickListener(this);
       accountStateButton.setOnClickListener(this);
	     
      return view; 
}

@Override
public void onClick(View view) {
	// TODO Auto-generated method stub
	switch(view.getId()){
	case R.id.orderPlaceButton:
		Intent productIntent=new Intent(mActivity.getApplicationContext(),OrderPlaceActivity.class);
		productIntent.putExtra("name",getResources().getString(R.string.lbl_order_place));
		startActivity(productIntent);
		break;
	case R.id.previousOrderButton:
		Intent previousIntent=new Intent(mActivity.getApplicationContext(),OrderPlaceActivity.class);
		previousIntent.putExtra("name",getResources().getString(R.string.lbl_previous_order));
		startActivity(previousIntent);
		break;
	case R.id.onlineOrderButton:
		break;
	case R.id.accountStatementButton:
		break;
		
	}
}

}
