package com.st.samudratech.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.st.samudratech.R;

public class SlideShowPagerAdapter extends PagerAdapter {

	Context mContext;
	LayoutInflater mLayoutInflater;
    private int mResources[];
	public SlideShowPagerAdapter(Context context,int mResources[]) {
	mContext = context;
	this.mResources=mResources;
	
	mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	@Override
	public int getCount() {
	return mResources.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
	return view == ((LinearLayout) object);
	}


	@Override
	public Object instantiateItem(ViewGroup container, int position) {
	View itemView = mLayoutInflater.inflate(R.layout.layout_slider_image, container, false);

	ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
	imageView.setImageResource(mResources[position]);

	container.addView(itemView);

	return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	container.removeView((LinearLayout) object);
	}
	

}
