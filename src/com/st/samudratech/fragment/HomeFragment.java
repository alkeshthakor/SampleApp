package com.st.samudratech.fragment;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import com.st.samudratech.R;
import com.st.samudratech.util.ConnectionDetector;
import com.st.samudratech.util.Constant;
import com.st.samudratech.util.DatePickerFragment;
import com.st.samudratech.util.ServerConnector;

@SuppressLint("SetJavaScriptEnabled")
public class HomeFragment extends Fragment {

	private final GestureDetector detector = new GestureDetector(
			new SwipeGestureDetector());

	public WebView mWebView_Chart;
	public WebSettings mWebViewSetting_Chart;

	private Activity mActivity;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private ViewFlipper mViewFlipper;
	private AnimationListener mAnimationListener;
	private Spinner chartReportSelectionSpinner;
	private EditText chartDateSelection;
	private String mServiceUrl;
	private int chartType;

	private String displayDate;
	private String chartDate;
	private String chartCaption;

	private String parameterDate;
	private int year, month, day;
	Calendar calender;

	private ProgressDialog mProgressDialog;
	ConnectionDetector cd;
	ServerConnector connector;
	Context mContext;
	Resources rs;

	private ArrayList<String> mCenterList;
	private ArrayList<String> mTotalCollectionList;
	private ArrayList<String> mCowCollectionList;
	private ArrayList<String> mBuffaloCollectionList;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = activity;
	}

	@SuppressLint("JavascriptInterface")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_home_layout, container,
				false);

		mContext = mActivity.getApplicationContext();

		cd = new ConnectionDetector(mContext);
		connector = new ServerConnector();
		rs = getResources();
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setIndeterminate(false);

		chartDateSelection = (EditText) view
				.findViewById(R.id.chartDateSelection);

		mWebView_Chart = (WebView) view.findViewById(R.id.mWebView_chart);
		chartReportSelectionSpinner = (Spinner) view
				.findViewById(R.id.chartReportSelectionSpinner);
		String[] chartTypeArray = getResources().getStringArray(
				R.array.chart_report_selection);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				R.layout.spinner_item, chartTypeArray);
		chartReportSelectionSpinner.setAdapter(adapter);

		mWebViewSetting_Chart = mWebView_Chart.getSettings();
		mWebViewSetting_Chart.setJavaScriptEnabled(true);

		if (Constant.DEVICE_SCREEN_WIDTH >= Constant.SAVEN_INCH_DEVICE_SCREEN) {
			mWebView_Chart
					.addJavascriptInterface(new JavaScriptInterface(
							getActivity().getApplicationContext(), 500, 300),
							"Android");
		} else {
			mWebView_Chart
					.addJavascriptInterface(new JavaScriptInterface(
							getActivity().getApplicationContext(), 300, 250),
							"Android");
		}
		mWebView_Chart.loadUrl(Constant.MILK_COLLECTION_CHART);
		calender = Calendar.getInstance();
		
		mViewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper);
		mViewFlipper.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(final View view, final MotionEvent event) {
				detector.onTouchEvent(event);
				return true;
			}
		});

		mViewFlipper.setAutoStart(true);
		mViewFlipper.setFlipInterval(4000);

		chartType = 0;
		mServiceUrl = Constant.HOST + Constant.DATE_WISE_CHART_SERVICE + "-500"
				+ "&seldate=2015-02-01";

		chartReportSelectionSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (position == 0) {
							chartDateSelection.setHint("Select Date");
							chartType = 0;
						} else {
							chartType = 1;
							chartDateSelection.setHint("Select Month");
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		chartDateSelection.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					selectDate();
					break;
				case MotionEvent.ACTION_UP:
					break;

				}
				return true;
			}
		});
		setDateOnStartup();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		mViewFlipper.startFlipping();
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

		public String MilkCollectionDataXML(){
			return BuildMilkCollectionChartXML();
		}
		
		public String XMLChartCaption() {
			return chartCaption;
		}

	}


	public String BuildMilkCollectionChartXML() {

		StringBuilder mNewDataForCharts = new StringBuilder();

		mNewDataForCharts.append("<categories>");

		for (int i = 0; i < mCenterList.size(); i++) {
			mNewDataForCharts.append("<category name='" + mCenterList.get(i)
					+ "'/>");
		}
		mNewDataForCharts.append("</categories>");

		mNewDataForCharts
				.append("<dataset seriesName='Cow Weight' color='#48d1cc'>");

		for (int i = 0; i < mCowCollectionList.size(); i++) {
			mNewDataForCharts.append("<set label='" + "Cow"
					+ "' tooltext='Milk Collection'  value='"
					+ mCowCollectionList.get(i)
					+ "' link=\"JavaScript:onChartClick(0)\"/>");
		}

		mNewDataForCharts.append("</dataset>");

		mNewDataForCharts
				.append("<dataset seriesName='Buffalo Weight' color='#aed5fc'>");

		for (int i = 0; i < mBuffaloCollectionList.size(); i++) {
			mNewDataForCharts.append("<set label='" + "Buffalo"
					+ "' tooltext='Milk Collection'  value='"
					+ mBuffaloCollectionList.get(i)
					+ "' link=\"JavaScript:onChartClick(0)\"/>");
		}

		mNewDataForCharts.append("</dataset>");
		return mNewDataForCharts.toString();
	}

	private class LoadChartTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog.show();

		}

		@Override
		protected JSONObject doInBackground(String... params) {
			return connector.getServerResponse(params[0]);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			parseResponse(result);
		}
	}

	class SwipeGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.left_in));
					mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.left_out));
					// controlling animation
					mViewFlipper.getInAnimation().setAnimationListener(
							mAnimationListener);
					mViewFlipper.showNext();
					return true;
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.right_in));
					mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(
							mContext, R.anim.right_out));
					// controlling animation
					mViewFlipper.getInAnimation().setAnimationListener(
							mAnimationListener);
					mViewFlipper.showPrevious();
					return true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mViewFlipper.stopFlipping();
	}

	private void selectDate() {
		DatePickerFragment date = new DatePickerFragment(
				mActivity.getApplicationContext());
		/**
		 * Set Up Current Date Into dialog
		 */
		// Calendar calender = Calendar.getInstance();
		Bundle args = new Bundle();
		args.putInt("year", calender.get(Calendar.YEAR));
		args.putInt("month", calender.get(Calendar.MONTH));
		args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));

		date.setArguments(args);
		/**
		 * Set Call back to capture selected date
		 */
		date.setCallBack(mDateListener);
		date.show(getFragmentManager(), "Date Picker");
	}

	OnDateSetListener mDateListener = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			monthOfYear = monthOfYear + 1;
			String dateValue = "";

			if (chartType == 0) {

				dateValue = year + "";

				if (monthOfYear < 10) {
					dateValue = dateValue + "-0" + monthOfYear;
				} else {
					dateValue = dateValue + "-" + monthOfYear;
				}

				if (dayOfMonth < 10) {
					dateValue = dateValue + "-0" + dayOfMonth;
				} else {
					dateValue = dateValue + "-" + dayOfMonth;
				}

				parameterDate = dateValue;

				dateValue = "";

				if (dayOfMonth < 10) {
					dateValue = "0" + dayOfMonth;
				} else {
					dateValue = "" + dayOfMonth;
				}

				if (monthOfYear < 10) {
					dateValue = dateValue + "-0" + monthOfYear;
				} else {
					dateValue = dateValue + "-" + monthOfYear;
				}
				dateValue = dateValue + "-" + year;

				displayDate = dateValue;

				mServiceUrl = Constant.HOST + Constant.DATE_WISE_CHART_SERVICE
						+ "-500" + "&seldate=" + parameterDate;

			} else if (chartType == 1) {

				dateValue = year + "";

				if (monthOfYear < 10) {
					dateValue = dateValue + "-0" + monthOfYear;
				} else {
					dateValue = dateValue + "-" + monthOfYear;
				}

				parameterDate = dateValue;
				dateValue = "";

				dateValue = Constant.MONTHS_INDEXED[(monthOfYear - 1)];
				dateValue = dateValue + " " + year;
				displayDate = dateValue;

				mServiceUrl = Constant.HOST + Constant.MONTH_WISE_CHART_SERVICE
						+ "-500" + "&seldate=" + parameterDate;

			}
			chartDateSelection.setText(displayDate);
			new LoadChartTask().execute(mServiceUrl);
		}
	};

	private void parseResponse(JSONObject responseData) {
		mCenterList = new ArrayList<String>();
		mCowCollectionList = new ArrayList<String>();
		mBuffaloCollectionList = new ArrayList<String>();
		mTotalCollectionList = new ArrayList<String>();

		if (responseData != null) {
			try {
				if (responseData.has("action")
						&& responseData.getBoolean("status") == true) {
					try {
						JSONArray values = responseData.getJSONArray("values");
						if (values != null&&values.length()>0) {
							for (int i = 0; i < values.length(); i++) {

								JSONObject dataObject = values.getJSONObject(i);

								int cowWeigth = (int) Float
										.parseFloat(dataObject
												.getString("cweight"));
								int buffaloWeigth = (int) Float
										.parseFloat(dataObject
												.getString("bweight"));
								int totalWeight = cowWeigth + buffaloWeigth;

								mCenterList.add("Center-"
										+ dataObject.getString("cntCode"));
								mCowCollectionList.add("" + cowWeigth);
								mBuffaloCollectionList.add("" + buffaloWeigth);
								mTotalCollectionList.add("" + totalWeight);

								if (dataObject.has("date")) {
									String responsedate = dataObject.getString(
											"date").substring(0, 10);
									String date[] = responsedate.split("-");
									if (date.length > 2) {
										String tempdate = date[2] + "-"
												+ date[1] + "-" + date[0];
										chartDate = tempdate;
									}

								} else if (dataObject.has("Month-Year")) {
									String date[] = dataObject.getString(
											"Month-Year").split("-");
									if (date.length > 1) {
										int monthINdex = Integer
												.parseInt(date[1]);
										String tempdate = Constant.MONTHS_INDEXED[monthINdex - 1]
												+ " " + date[0];
										chartDate = tempdate;
									}
								}
							}

							if (chartType == 0) {
								chartCaption = "Daily Center Wise Milk Collection - "
										+ chartDate;
							} else {
								chartCaption = "Monthly Center Wise Milk Collection - "
										+ chartDate;
							}

						}else{
							chartCaption = "Milk collection data not available for " + displayDate;
						}
						mWebView_Chart.loadUrl("javascript:LoadCharts()");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	public void setDateOnStartup() {

		year = calender.get(Calendar.YEAR);
		month = calender.get(Calendar.MONTH);
		day = calender.get(Calendar.DAY_OF_MONTH);

		month = month + 1;

		String dateValue = "";

		dateValue = year + "";

		if (month < 10) {
			dateValue = dateValue + "-0" + month;
		} else {
			dateValue = dateValue + "-" + month;
		}

		if (day != 1) {
			day = day - 1;
		}

		if (day < 10) {
			dateValue = dateValue + "-0" + day;
		} else {
			dateValue = dateValue + "-" + day;
		}

		parameterDate = dateValue;

		dateValue = "";

		if (day < 10) {
			dateValue = "0" + day;
		} else {
			dateValue = "" + day;
		}

		if (month < 10) {
			dateValue = dateValue + "-0" + month;
		} else {
			dateValue = dateValue + "-" + month;
		}

		dateValue = dateValue + "-" + year;
		displayDate = dateValue;
		chartDateSelection.setText(displayDate);
		mServiceUrl = Constant.HOST + Constant.DATE_WISE_CHART_SERVICE + "-500"
				+ "&seldate=" + parameterDate;
		new LoadChartTask().execute(mServiceUrl);
	}
}
