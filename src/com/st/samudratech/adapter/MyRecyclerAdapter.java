package com.st.samudratech.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.st.samudratech.R;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{
	
	String []mItemData;
	
	 OnItemClickListener mItemClickListener;;

    public MyRecyclerAdapter(String []itemsData) {
        this.mItemData = itemsData;
    }
     
    // Create new views (invoked by the layout manager)
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.recycler_item_layout, null);
 
        // create ViewHolder       
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }
 
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
         
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        viewHolder.txtViewTitle.setText(mItemData[position]);
 
    }
     
    // inner class to hold a reference to each item of RecyclerView 
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        
        public TextView txtViewTitle;
         
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            //itemLayoutView.setOnClickListener(this);
            itemLayoutView.setOnTouchListener(this);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.listConentTextView);
        }

//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			if (mItemClickListener != null) {
//                mItemClickListener.onItemClick(v, getPosition());
//            }
//		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
			return false;
		}
    }
 
    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mItemData.length;
    }
    
    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
