package com.st.samudratech.util;

import android.R.bool;
import android.content.Context;

public class Constant {
	
	// APPLICATION PREFERENCE FILE NAME
		public static String PREF_FILE = "PREF_SAMUDRA_TECH";
		
	public static Context CONTEXT;
	
	
	public static String HOST="http://117.239.35.172:8080";
	public static String HOST_LOGIN="http://117.239.35.172:8080";

	//http://117.239.35.171:8080/gdairy/user/login?mobileno=9909431965&password=DP1&loginType=1
		
	public final static String MILK_COLLECTION_CHART = "file:///android_asset/milk_collection_report_stack_chart.html";
	
	public static final String LOGIN_SERVICE="/gdairy/user/login?";
	public static final String PRODUCT_SERVICE="/gdairy/Product/getProduct?";
	public static final String SUBMIT_ORDER_SERVICE="/gdairy/Order/order/";
	public static final String TODAY_ORDER_SERVICE="/gdairy/Order/list?";
	
	public static final String DOC_REPORT_SERVICE="/RMRD/Report/DocReport?";
	public static final String LAB_REPORT_SERVICE="/RMRD/Report/LabReport?";
	
	public static final String MONTH_WISE_CHART_SERVICE="/RMRD/reports/getMonthWiseMilkData?centerId=";
	public static final String DATE_WISE_CHART_SERVICE="/RMRD/reports/getDateWiseMilkData?centerId=";
	
	public static final String REPORT_LIST_SERVICE="/gdairy/Order/viewListReport?boothId=";
	public static final String REPORT_DETAIL_SERVICE="/gdairy/Order/viewReport?";
	
	
	public static String PREF_BOOTH_ID = "PREF_BOOTH_ID";
	public static String PREF_BOOTH_NAME = "PREF_BOOTH_NAME";
	public static String PREF_BOOTH_BALANCE = "PREF_BOOTH_BALANCE";
	
	public static String PREF_USER_ID = "pref_userid";
	public static String PREF_PASSWORD = "pref_password";

	
	public static String PREF_USER_ID_SERVER = "pref_userid_server";
	public static String PREF_PASSWORD_SERVER = "pref_password_server";
	public static String PREF_SERVER_IP = "pref_server_ip";
	public static String PREF_SERVER_INSTANCE = "pref_server_password";

	
	public static int DEVICE_SCREEN_WIDTH =0;
	
	public static int MOBILE_DEVICE_SCREEN = 480;
	public static int SAVEN_INCH_DEVICE_SCREEN =600;
	public static int TEN_INCH_DEVICE_SCREEN =800;

	public static String[] MONTHS_INDEXED = new String[] {"JAN", "FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
}
