package com.st.samudratech;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.st.samudratech.modal.Product;
import com.st.samudratech.util.ConnectionDetector;
import com.st.samudratech.util.Constant;
import com.st.samudratech.util.Pref;
import com.st.samudratech.util.ServerConnector;

public class OrderPlaceActivity extends ActionBarActivity implements
		OnClickListener {

	private ProgressDialog mProgressDialog;
	ConnectionDetector cd;
	ServerConnector connector;
	Context mContext;
	Resources rs;

	private LinearLayout productItemLinearLayout;

	private TextView mIdTextView;
	private TextView mAreaTextView;
	private TextView mBalanceTextView;
	private TextView mLastOrderTextView;
	private TextView mOrderShiftTextView;
	private TextView mCurrentOrderTextView;

	private LinearLayout productListView;
	private List<Product> mProductList;
	float[] productTotal;
	float currentOrder;

	private String msgBody;
	Calendar mCalendar;

	private int year;
	private int month;
	private int day;
	private int mMode;

	private List<String> mProductItemList;
	private List<String> mProductItemQuantityList;

	private List<EditText> quantityEditTextList;

	private String todayDate;
	private String screenName;
	

	private Toolbar toolbar;

	private Button mSmsButton;
	private Button mSubmitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_place);

		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			 getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		     getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			TextView mTitle = (TextView) toolbar
					.findViewById(R.id.toolbar_title);
			mTitle.setText(R.string.lbl_order_place);
			
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.white),Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);
			
		}

		
		productListView = (LinearLayout) findViewById(R.id.productListView);

		mIdTextView = (TextView) findViewById(R.id.boothIdTextView);
		mAreaTextView = (TextView) findViewById(R.id.boothAreaTextView);
		mBalanceTextView = (TextView) findViewById(R.id.balanceTextView);
		mLastOrderTextView = (TextView) findViewById(R.id.lastOrderTextView);
		mOrderShiftTextView = (TextView) findViewById(R.id.oshiftTextView);
		mCurrentOrderTextView = (TextView) findViewById(R.id.currentOrderTextView);

		mSmsButton = (Button) findViewById(R.id.smsButton);
		mSubmitButton = (Button) findViewById(R.id.submitButton);

		mSmsButton.setOnClickListener(this);
		mSubmitButton.setOnClickListener(this);

		mContext = this;
		Constant.CONTEXT = this;

		cd = new ConnectionDetector(mContext);
		connector = new ServerConnector();
		rs = getResources();
		mProgressDialog = new ProgressDialog(OrderPlaceActivity.this);
		mProgressDialog.setMessage("Data loading...");
		mProgressDialog.setIndeterminate(false);
		mCalendar = Calendar.getInstance();
		int hoursOfDay = mCalendar.get(Calendar.HOUR_OF_DAY);

		if (hoursOfDay >= 4 && hoursOfDay <= 12) {
			mMode = 1;
			mOrderShiftTextView.setText(getResources().getString(
					R.string.lbl_shift_night));

		} else {
			mMode = 0;
			mOrderShiftTextView.setText(getResources().getString(
					R.string.lbl_shift_morning));
		}

		if (getIntent() != null)
			screenName = getIntent().getStringExtra("name");

		if (screenName.equalsIgnoreCase(getResources().getString(
				R.string.lbl_previous_order))) {
			mSmsButton.setVisibility(View.GONE);
			mSubmitButton.setVisibility(View.GONE);
		}

		String boothId = Pref.getValue(Constant.PREF_BOOTH_ID, "0");
		if (!boothId.equals("0")) {
			String urlString = Constant.HOST + Constant.PRODUCT_SERVICE
					+ "boothId=" + boothId.trim();
			new LoadProductTask().execute(urlString);
		}
	}

	class LoadProductTask extends AsyncTask<String, Void, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.setMessage("Data loading...");
			mProgressDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... param) {
			return connector.getServerResponse(param[0]);
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();

			if (result != null) {
				try {
					Log.d("Data", result.toString());
					if (result.has("action")
							&& result.getBoolean("status") == true) {
						setDataToUI(result.getJSONArray("values"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void setDataToUI(JSONArray mProductData) {

		mIdTextView.setText(Pref.getValue(Constant.PREF_BOOTH_ID, ""));
		mAreaTextView.setText(Pref.getValue(Constant.PREF_BOOTH_NAME, ""));
		mBalanceTextView
				.setText(Pref.getValue(Constant.PREF_BOOTH_BALANCE, ""));

		try {
			mProductList = new ArrayList<Product>();
			productTotal = new float[mProductData.length()];

			quantityEditTextList = new ArrayList<EditText>();

			for (int i = 0; i < mProductData.length(); i++) {

				productTotal[i] = 0;

				JSONObject mObject = mProductData.getJSONObject(i);
				final Product mProduct = new Product();
				mProduct.setProductId(mObject.getInt("productId"));
				mProduct.setProductName(mObject.getString("prodname"));
				mProduct.setShortName(mObject.getString("shortname"));
				mProduct.setBoothName(mObject.getString("boothName"));
				mProduct.setBoothNo(mObject.getString("boothno"));
				mProduct.setPackaging(mObject.getString("packaging"));
				String unit = mObject.getString("unit");
				if ("".equals(unit))
					mProduct.setUnit(0);
				else
					mProduct.setUnit(Integer.parseInt(unit));

				mProduct.setQuantityNo(Integer.parseInt(mObject
						.getString("qtyno")));

				mProduct.setRate(Float.parseFloat(mObject.getString("rate")));

				mProductList.add(mProduct);

				View view = new View(mContext);
				view = ((Activity) mContext).getLayoutInflater().inflate(
						R.layout.layout_product_item, null);

				TextView productNameTV = (TextView) view
						.findViewById(R.id.productNameTextView);
				final EditText quantityET = (EditText) view
						.findViewById(R.id.quantityEditText);
				quantityET.setId(i);

				quantityEditTextList.add(quantityET);

				productNameTV.setText(mProduct.productName);
				if (mProduct.unit != 0)
					quantityET.setText(mProduct.unit + "");

				quantityET.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// TODO Auto-generated method stub
					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// TODO Auto-generated method stub
					}

					@Override
					public void afterTextChanged(Editable arg0) {
						String unitText = quantityET.getText().toString();
						if (!"".equals(unitText)) {
							int unit = Integer.parseInt(unitText);
							float amount = unit * mProduct.quantityNo
									* mProduct.rate;
							mProduct.setUnit(unit);
							Log.d("View Position: ", quantityET.getId() + "");
							productTotal[quantityET.getId()] = amount;
							currentOrder = 0;
							for (int j = 0; j < productTotal.length; j++) {
								currentOrder += productTotal[j];
							}
							mCurrentOrderTextView.setText("" + currentOrder);
						}
					}
				});
				productListView.addView(view);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (screenName.equalsIgnoreCase(getResources().getString(
				R.string.lbl_previous_order))) {
			String listParams = "boothId="
					+ Pref.getValue(Constant.PREF_BOOTH_ID, "0") + "&shift="
					+ mMode + "&date=" + getDate();
			new GetTodayOrderInfoTask().execute(listParams);
		}
	}

	class GetTodayOrderInfoTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {

			JSONObject todsyOrersObj = connector
					.getTodayOrderInfo(Constant.HOST
							+ Constant.TODAY_ORDER_SERVICE + params[0]);
			return todsyOrersObj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			try {
				if ("No Data Found"
						.equalsIgnoreCase(result.getString("values"))) {

				} else {
					JSONObject valuesObject = result.getJSONObject("values");
					if (valuesObject != null) {
						JSONArray todayOrderItems = valuesObject
								.getJSONArray("items");
						if (todayOrderItems != null
								&& todayOrderItems.length() > 0) {
							mProductItemList = new ArrayList<String>();
							mProductItemQuantityList = new ArrayList<String>();

							for (int j = 0; j < todayOrderItems.length(); j++) {
								mProductItemList.add(todayOrderItems
										.getJSONObject(j).getString("itemId"));
								mProductItemQuantityList.add(todayOrderItems
										.getJSONObject(j).getString("qty"));
							}

							for (int i = 0; i < mProductList.size(); i++) {
								String itemId = mProductList.get(i).productId
										+ "";
								if (mProductItemList.contains(itemId)) {
									int position = mProductItemList
											.lastIndexOf(itemId);
									int quantiy = Integer
											.parseInt(mProductItemQuantityList
													.get(position));
									mProductList.get(i).setUnit(quantiy);
									// quantityEditTextList.get(i).setText(quantiy);
									EditText et = quantityEditTextList.get(i);
									et.setText("" + quantiy);

								}
							}
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class SubmtOrderTask extends AsyncTask<JSONObject, Void, Integer> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog.setMessage("Order Submiting...");
			mProgressDialog.show();
		}

		@Override
		protected Integer doInBackground(JSONObject... orders) {
			// TODO Auto-generated method stub
			int statusCode = connector.submitOrder(Constant.HOST
					+ Constant.SUBMIT_ORDER_SERVICE, orders[0]);
			// submitOrder();
			return statusCode;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			mProgressDialog.cancel();
			if (result == 200)
				Toast.makeText(getApplicationContext(),
						"Order sent successfully", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(),
						"Order not sent successfully", Toast.LENGTH_SHORT)
						.show();

		}

	}

	private String getDate() {

		year = mCalendar.get(Calendar.YEAR);

		String yearShort = "" + year;

		year = Integer.parseInt(yearShort.substring(2));

		month = mCalendar.get(Calendar.MONTH);
		day = mCalendar.get(Calendar.DAY_OF_MONTH);
		month = month + 1;

		String dateValue = "";

		if (month < 10) {
			dateValue = "0" + month;
		} else {
			dateValue = "" + month;
		}
		if (day < 10) {
			dateValue = dateValue + "-0" + day;
		} else {
			dateValue = dateValue + "-" + day;
		}

		dateValue = dateValue + "-" + year;

		todayDate = dateValue;

		return todayDate;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.submitButton:
			submitOrder();
			break;
		case R.id.smsButton:
			showSmsDialog();
			break;
		}
	}

	public void showSmsDialog() {

		// custom dialog
		final Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.layout_smslayout);
		dialog.setTitle("Order Information");
		final EditText numberEditText = (EditText) dialog
				.findViewById(R.id.numberEditText);
		EditText messageEditText = (EditText) dialog
				.findViewById(R.id.messageDetailEditText);
		numberEditText.setText("9909431965");
		// numberEditText.setText("9725729431");

		String values = "";

		for (int i = 0; i < mProductList.size(); i++) {
			values += mProductList.get(i).productId + ":"
					+ mProductList.get(i).unit + ", ";
		}

		msgBody = "BoothName: " + Pref.getValue(Constant.PREF_BOOTH_NAME, "")
				+ ", BoothId: " + Pref.getValue(Constant.PREF_BOOTH_ID, "")
				+ ",\n Shift: " + mMode + ", Mobile: "
				+ numberEditText.getText() + ",\n End:~ " + values + " ~"
				+ ",\n Date: " + getDate();

		messageEditText.setText(msgBody);

		Button sendButton = (Button) dialog.findViewById(R.id.messageSendBT);
		// if button is clicked, close the custom dialog
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SmsManager.getDefault().sendTextMessage(
						numberEditText.getText().toString(), null, msgBody,
						null, null);
				dialog.dismiss();
			}
		});

		Button cancelButton = (Button) dialog
				.findViewById(R.id.messageCancelBT);
		// if button is clicked, close the custom dialog
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	public void submitOrder() {

		JSONObject order = new JSONObject();

		try {

			order.put("orderId", "0");
			order.put("boothId", Pref.getValue(Constant.PREF_BOOTH_ID, ""));
			order.put("mobileno", "9909431965");
			order.put("shift", "" + mMode);
			order.put("status", "0");
			order.put("orderDtTime", getDate());

			JSONArray orderItemList = new JSONArray();

			for (int i = 0; i < mProductList.size(); i++) {
				JSONObject itemObject = new JSONObject();
				itemObject.put("itemId", mProductList.get(i).productId + "");
				itemObject.put("qty", mProductList.get(i).unit + "");
				orderItemList.put(itemObject);
			}
			order.put("items", orderItemList);

			new SubmtOrderTask().execute(order);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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