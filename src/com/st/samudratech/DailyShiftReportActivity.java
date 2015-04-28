package com.st.samudratech;

import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.st.samudratech.adapter.MyRecyclerAdapter;
import com.st.samudratech.adapter.MyRecyclerAdapter.OnItemClickListener;
import com.st.samudratech.util.DividerItemDecoration;

public class DailyShiftReportActivity extends ActionBarActivity {

	private Toolbar toolbar;
	private String menuList[];
	private RecyclerView recyclerView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_shift_report);
		
		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			 getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		        getSupportActionBar().setHomeButtonEnabled(true);
		        
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			if(getIntent()!=null)
			mTitle.setText(getIntent().getStringExtra("name"));
			
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.white),Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);

		}
		
		menuList = getResources().getStringArray(R.array.shift_report_menu_array);

		recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

		recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		
		recyclerView.setHasFixedSize(true);
		 
		// 2. set layoutManger
		recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		
		// 3. create an adapter
		MyRecyclerAdapter mAdapter = new MyRecyclerAdapter(menuList);
		// 4. set adapter
		recyclerView.setAdapter(mAdapter);
		// 5. set item animator to DefaultAnimator
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		
		mAdapter.SetOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View v , int position) {
            }
        });
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
	    case android.R.id.home:
	        finish();
	        break;

	    default:
	        break;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
