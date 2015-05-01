package com.st.samudratech.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.st.samudratech.R;
import com.st.samudratech.util.Constant;

@SuppressLint("SetJavaScriptEnabled")
public class HomeFragment extends Fragment {
	
	 public WebView mWebView_Chart;
     public WebSettings mWebViewSetting_Chart;
		
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
	  View view=inflater.inflate(R.layout.fragment_home_layout, container,false);
     
      
	  mWebView_Chart = (WebView)view.findViewById(R.id.mWebView_chart);
	  
	  mWebViewSetting_Chart = mWebView_Chart.getSettings();
	  mWebViewSetting_Chart.setJavaScriptEnabled(true);
	  mWebView_Chart.addJavascriptInterface(new JavaScriptInterface(
      getActivity().getApplicationContext(),300,300), "Android");	
	  mWebView_Chart.loadUrl(Constant.URL_HEALTH_WEIGHT);
	  new LoadChartTask().execute();
      return view;
}
  
  @Override
public void onResume() {
	super.onResume();

}
  
  public class JavaScriptInterface {

		Context mContext;
		int mWidth, mHight;

		/** Instantiate the interface and set the context */
		JavaScriptInterface(Context c, int width, int hight) {
			mContext = c;
			this.mWidth = width;
			this.mHight = hight;
		}

		/** Show a toast from the web page */
		public int getWidth() {
			return mWidth;
		}

		public int getHight() {
			return mHight;
		}

		public String XMLDataWeight() {
			return BuildWeightXML();
		}

	}

  
  public String BuildWeightXML() {

	  Toast.makeText(getActivity().getApplication(),"Build xml called",Toast.LENGTH_SHORT).show();
	  
		StringBuilder mNewDataForCharts = new StringBuilder();
		mNewDataForCharts.append("<categories>");
		
		for (int i = 0; i < 4; i++) {
			mNewDataForCharts.append("<category name='"
					+ "Cat-"+i
					+ "'/>");
		}
		
		mNewDataForCharts.append("</categories>");
		mNewDataForCharts
				.append("<dataset seriesName='Weight' color='#FBB117'>");
		int values=500;
		for (int i = 0; i < 4; i++) {
						mNewDataForCharts.append("<set label='" + "Timestemp-"+i
					+ "' tooltext='Weight'  value='" + (values+=50)
					+ "' link=\"JavaScript:onChartClick(0)\"/>");
		}
		mNewDataForCharts.append("</dataset>");
		return mNewDataForCharts.toString();
	}
	
 
  private class LoadChartTask extends AsyncTask<Void,Void,Void>{

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	  
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		  mWebView_Chart.loadUrl("javascript:LoadCharts()");
	}
  }
  
}
