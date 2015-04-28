package com.st.samudratech.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.st.samudratech.AdminMasterActivity;
import com.st.samudratech.AdminReportActivity;
import com.st.samudratech.DailyActivity;
import com.st.samudratech.R;
import com.st.samudratech.adapter.MyRecyclerAdapter;
import com.st.samudratech.adapter.MyRecyclerAdapter.OnItemClickListener;
import com.st.samudratech.util.DividerItemDecoration;

public class AdminFragment extends Fragment {

	private String menuList[];
	private Activity mActivity;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_admin, container, false);

		menuList = getResources().getStringArray(R.array.admin_menu_array);

		RecyclerView recyclerView = (RecyclerView) view
				.findViewById(R.id.recyclerView);

		recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		
		recyclerView.setHasFixedSize(true);
		 
		// 2. set layoutManger
		recyclerView.setLayoutManager(new LinearLayoutManager(mActivity
				.getApplicationContext()));
		// 3. create an adapter
		MyRecyclerAdapter mAdapter = new MyRecyclerAdapter(menuList);
		// 4. set adapter
		recyclerView.setAdapter(mAdapter);
		// 5. set item animator to DefaultAnimator
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		
		
		mAdapter.SetOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View v , int position) {
                // do something with position
            	startNextActivity(position);
            }
        });
		
		
		
		    
		return view;

	}
	
	
	public void startNextActivity(int id){
		Intent nextIntent;
		
		switch(id){
		case 0:
			  nextIntent=new Intent(mActivity.getApplicationContext(),AdminMasterActivity.class);
			  nextIntent.putExtra("name",menuList[id]);
			  startActivity(nextIntent);
			break;
		case 1:
			 nextIntent=new Intent(mActivity.getApplicationContext(),DailyActivity.class);
			 nextIntent.putExtra("name",menuList[id]);
			 startActivity(nextIntent);
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			 nextIntent=new Intent(mActivity.getApplicationContext(),AdminReportActivity.class);
			 nextIntent.putExtra("name",menuList[id]);
			 startActivity(nextIntent);
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			break;
			
		}
	}
}
