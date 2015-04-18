package com.st.samudratech;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.st.samudratech.adapter.SimpleListAdapter;
import com.st.samudratech.fragment.AdminFragment;
import com.st.samudratech.fragment.BoothFragment;
import com.st.samudratech.fragment.HomeFragment;
import com.st.samudratech.fragment.SupplyChainFragment;
import com.st.samudratech.fragment.UtilitiesFragment;

public class HomeActivity extends ActionBarActivity {
	
	private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private String[] leftSliderData = {"Home", "Booth", "Supply Chain", "Admin","Utilites","Log Out"};
    
	private SimpleListAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		 toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
		 if (toolbar != null) {
	            setSupportActionBar(toolbar);
	            getSupportActionBar().setDisplayShowTitleEnabled(false);
	           // TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
	      }
	     
	     leftDrawerList = (ListView) findViewById(android.R.id.list);
	     drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	     
	     //myAdapter=new SimpleListAdapter(getApplicationContext(), leftSliderData);
	     
	     navigationDrawerAdapter=new ArrayAdapter<String>(this, R.layout.listview_item_layout,
	    		    R.id.listConentTextView, leftSliderData);
	     //navigationDrawerAdapter=new ArrayAdapter<String>( HomeActivity.this,R.layout.listview_item_layout, leftSliderData);
	   
	     leftDrawerList.setAdapter(navigationDrawerAdapter);
	     
	     leftDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				loadFragment(position);
				drawerLayout.closeDrawer(leftDrawerList);
			}
		});
	     initDrawer();
	     loadFragment(0);
	}
	
	private void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        
    }
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		drawerToggle.syncState();
	}
	
	private void loadFragment(int viewposition){
		
	  Fragment mFragment;
      FragmentManager fragmentManager = getSupportFragmentManager();
      FragmentTransaction transaction = fragmentManager.beginTransaction();
		
		switch(viewposition){
		case 0:
			mFragment=new HomeFragment();
			transaction.replace(R.id.container, mFragment);
            transaction.addToBackStack(null);
            transaction.commit();
			break;
		case 1:
			mFragment=new BoothFragment();
			transaction.replace(R.id.container, mFragment);
            transaction.addToBackStack(null);
            transaction.commit();
			break;
		case 2:
			mFragment=new SupplyChainFragment();
			transaction.replace(R.id.container, mFragment);
            transaction.addToBackStack(null);
            transaction.commit();
			break;
		case 3:
			mFragment=new AdminFragment();
			transaction.replace(R.id.container, mFragment);
            transaction.addToBackStack(null);
            transaction.commit();

			break;
		case 4:
			mFragment=new UtilitiesFragment();
			transaction.replace(R.id.container, mFragment);
            transaction.addToBackStack(null);
            transaction.commit();
			break;
		case 5:
			finish();
			break;
			
		}
	}
}
