package com.alkesh.sampleapp.adapter;


import com.alkesh.sampleapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SimpleListAdapter extends BaseAdapter {

	 private Context mContext;
	 private String[] menuList;
	 
	 LayoutInflater mLayoutInflater;
	 
	
	 private int colorList[];
	 
	 public SimpleListAdapter(Context mContext,String[] menuList){
		 this.mContext=mContext;
		 this.menuList=menuList;		 
		 mLayoutInflater=LayoutInflater.from(mContext);
		 colorList=this.mContext.getResources().getIntArray(R.array.drowarColoList);
	 }
	 
	 @Override
		public int getCount() {
			// TODO Auto-generated method stub
			return menuList.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return menuList[position];
		}
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder mViewHolder;
		if(convertView == null){
			convertView=(View)mLayoutInflater.inflate(R.layout.listview_item_layout,parent,false);     	
			mViewHolder=new ViewHolder();
			mViewHolder.menuTV=(TextView)convertView.findViewById(R.id.listConentTextView);
			
			convertView.setTag(mViewHolder);
			convertView.setId(position);
			
			if(position<colorList.length){
				convertView.setBackgroundColor(colorList[position]);
			}
//			 int colorPos = position % colors.length;
//			 convertView.setBackgroundColor(colors[colorPos]);
		      
		}else{
			mViewHolder=(ViewHolder)convertView.getTag();
		}
		
		mViewHolder.menuTV.setText(getItem(position).toString());
		
		return convertView;
	}

	 public class ViewHolder{
			TextView menuTV;
		}
}
