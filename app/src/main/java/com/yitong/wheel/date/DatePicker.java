package com.yitong.wheel.date;//package com.yitong.wheel.date;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.Typeface;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.WindowManager;
//import android.webkit.WebView;
//import android.widget.TextView;
//
//import com.mrwujay.cascade.R;
//
//import java.util.Calendar;
//
//
//public class DatePicker {
//
//	private Dialog dialog_time;
//	private Context context;
////	private boolean timeChanged = false;
////	private boolean timeScrolled = false;
//	StringBuffer buffer;
//	private String age;
//	private int mCurYear;
//	private int mCurMonth;
//
//	private int mCurHour;
//	private int mCurMinute;
//
//	private String[] dateType;
//	private TextView submit;
//	private TextView cancel;
//	private DateNumericAdapter monthAdapter, dayAdapter, yearAdapter,hourAdapter,minuteAdapter;
//	private int mCurDay;
//	private int style;
//	private boolean isHasDay = true;
//	private WebView webView;
//	private DateSelectedListener dateSelectedListener;
//
//	public static String HIDDEN_CALLBACK = "javascript:Fw.Calendar.onHidden()";
//	public DatePicker(Context context, boolean isHasDay, WebView webView){
//		this.context = context;
//		style = R.style.Dialog_Fullscreen;
//		this.isHasDay=isHasDay;
//		this.webView=webView;
//	}
//	public DatePicker(Context context, int style){
//		this.context = context;
//		this.style = style;
//	}
//	/**
//	 * 选择日期对话框
//	 * @param maxtime 最大日期
//	 * @param mintime 最小日期
//	 */
//	public void selectDateDialog(final String maxtime,String mintime,String currenttime,DateSelectedListener dlistener) {
//		View view = newDataDialog(R.layout.picker_date);
//		this.dateSelectedListener = dlistener;
//		 submit = (TextView) view.findViewById(R.id.submit);
//		 cancel = (TextView) view.findViewById(R.id.cancel);
//		final WheelView year = (WheelView) view.findViewById(R.id.year);
//		final WheelView month = (WheelView) view.findViewById(R.id.month);
//		final WheelView day = (WheelView) view.findViewById(R.id.day);
//		if(isHasDay){
//			day.setVisibility(View.VISIBLE);
//		}else{
//			day.setVisibility(View.GONE);
//		}
//		// TextView tv_title = (TextView)
//		// view.findViewById(R.id.dialog_time_title);
//
//		this.age = currenttime;
//		String[] maxt = null;
//		String[] mint = null;
//		String[] currt = null;
//		if(!TextUtils.isEmpty(maxtime)){
//			maxt = maxtime.split("-");
//		}
//		if(!TextUtils.isEmpty(mintime)){
//			mint = mintime.split("-");
//		}
//		if(!TextUtils.isEmpty(currenttime)){
//			currt = currenttime.split("-");
//		}
//
//		submit.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				dateSelectedListener.onDateSelectedListener(age);
//				dialog_time.dismiss();
//			}
//		});
//		cancel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				dialog_time.dismiss();
//			}
//		});
//		OnWheelChangedListener listener = new OnWheelChangedListener() {
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				updateDays(year, month, day);
//			}
//		};
//		if (age != null && age.contains("-")) {
//			String str[] = age.split("-");
//			mCurYear = Integer.parseInt(currt[0]);
//			mCurMonth = Integer.parseInt(str[1]) - 1;
//			isHasDay=false;
//			if(str.length>2){
//				mCurDay = Integer.parseInt(str[2]) - 1;
//				isHasDay=true;
//			}
//		}
//		dateType = context.getResources().getStringArray(R.array.date);
//		monthAdapter = new DateNumericAdapter(context, Integer.parseInt(mint[1]), Integer.parseInt(maxt[1]), 5);
//		monthAdapter.setTextType(dateType[1]);
//		month.setViewAdapter(monthAdapter);
//		month.setCurrentItem(Integer.parseInt(currt[1])-1);
//		month.addChangingListener(listener);
//		// year
//
//		yearAdapter = new DateNumericAdapter(context, Integer.parseInt(mint[0]), Integer.parseInt(maxt[0]), 1993);
//		yearAdapter.setTextType(dateType[0]);
//		year.setViewAdapter(yearAdapter);
//		if(Integer.parseInt(currt[0])-Integer.parseInt(mint[0])<0){
//			year.setCurrentItem(0);
//		}else{
//		year.setCurrentItem(Integer.parseInt(currt[0])-Integer.parseInt(mint[0]));
//		}
//		year.addChangingListener(listener);
//		// day
//		day.setCurrentItem(Integer.parseInt(currt[2])-1);
//		updateDays(year, month, day);
//		day.addChangingListener(listener);
//
//		dialog_time.show();
//	}
//
//
//	/**
//	 * 显示  小时和分钟的dialog
//	 * @param tv_time  需要填充时间的控件
//	 * @param defaultTime 初始时间
//	 */
//	public void selectTimeDialog(final TextView tv_time,String defaultTime) {
//		View view = newDataDialog(R.layout.picker_time);
//
//		 submit = (TextView) view.findViewById(R.id.submit);
//		 cancel = (TextView) view.findViewById(R.id.cancel);
//		final WheelView hour = (WheelView) view.findViewById(R.id.dialog_time_hour);
//		final WheelView minute = (WheelView) view.findViewById(R.id.dialog_time_minute);
//
//
//		try {
//			String time = ((TextView)tv_time).getText().toString().trim();
//			if(TextUtils.isEmpty(time)){
//				if(TextUtils.isEmpty(defaultTime)){
//				 this.age = "10:01";
//				}else{
//					this.age = defaultTime;
//				}
//			}else{
//				this.age = time;
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			this.age = "10:01";
//		}
//
//
//		submit.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				((TextView) tv_time).setText(age);
//				dialog_time.dismiss();
//			}
//		});
//		cancel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				dialog_time.dismiss();
//			}
//		});
//		Calendar calendar = Calendar.getInstance();
//		OnWheelChangedListener listener = new OnWheelChangedListener() {
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				updateTime(hour, minute);
//
//			}
//
//		};
//		if (age != null && age.contains(":")) {
//			String str[] = age.split(":");
//			mCurHour =Integer.parseInt(str[0]);
//			mCurMinute = Integer.parseInt(str[1]);
//		}
//
//		dateType = context.getResources().getStringArray(R.array.date);
//		hourAdapter = new DateNumericAdapter(context, 0, 23, calendar.get(Calendar.HOUR_OF_DAY));
//		hourAdapter.setTextType(dateType[3]);
//		hour.setViewAdapter(hourAdapter);
//		hour.setCurrentItem(mCurHour);
//		hour.addChangingListener(listener);
//		// hour
//
//		minuteAdapter = new DateNumericAdapter(context, 0, 59, calendar.get(Calendar.MINUTE));
//		minuteAdapter.setTextType(dateType[4]);
//		minute.setViewAdapter(minuteAdapter);
//		minute.setCurrentItem(mCurMinute);
//		minute.addChangingListener(listener);
//		// minute
//
////		updateDays(hour, minute);
//
//		dialog_time.show();
//
//	}
//
//	@SuppressWarnings("deprecation")
//	private View newDataDialog(int layoutID) {
//		buffer = new StringBuffer();
//		dialog_time = new Dialog(context, style);
//		dialog_time.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View view = inflater.inflate(layoutID, null);
//		int screenWidth =((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
//
//		Window window = dialog_time.getWindow();
//		// 重新设置
//		WindowManager.LayoutParams lp = window.getAttributes();
//		window.setWindowAnimations(R.style.addresspickerstyle); // 添加动画
//		window.setGravity(Gravity.BOTTOM);
//		// window.setWindowAnimations(R.style.mystyle); // 添加动画
//		lp.x = 0; // 新位置X坐标
//		lp.y = 0; // 新位置Y坐标
//		// lp.width = screenWidth;
//		// window.setAttributes(lp);
//		view.setMinimumWidth(screenWidth - 0);
//		dialog_time.setContentView(view, lp);
//		return view;
//	}
//	/**
//	 * 年月日 的更新操作
//	 * @param year
//	 * @param month
//	 * @param day
//	 */
//	public void updateDays(WheelView year, WheelView month, WheelView day) {
//
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
//		calendar.set(Calendar.MONTH, month.getCurrentItem());
//		if(isHasDay){
//			int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//			dayAdapter = new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1);
//			dayAdapter.setTextType(dateType[2]);
//			day.setViewAdapter(dayAdapter);
//			int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
//			day.setCurrentItem(curDay - 1, true);
//		}
//		int years = calendar.get(Calendar.YEAR)-1;
//		String monthss = "";
//		String dayss = "";
//		if(month.getCurrentItem()+1<=9){
//			monthss = "0"+(month.getCurrentItem()+1);
//		}else{
//			monthss = ""+(month.getCurrentItem()+1);
//		}
//		if(isHasDay){
//			if(day.getCurrentItem()+1<=9){
//				dayss = "0"+(day.getCurrentItem()+1);
//			}else{
//				dayss = ""+(day.getCurrentItem()+1);
//			}
//			age = years + "-" + monthss + "-" + dayss;
//		}else {
//			age = years + "-" + monthss;
//		}
//	}
//	/**
//	 * 时分的更新操作
//	 * @param hour
//	 * @param minutes
//	 */
//	public void updateTime(WheelView hour, WheelView minutes) {
//
//		/*Calendar calendar = Calendar.getInstance();
//		int maxMinute = calendar.getMaximum(Calendar.MINUTE);
//		minuteAdapter = new DateNumericAdapter(context, 0, maxMinute, calendar.get(Calendar.MINUTE));
//		minuteAdapter.setTextType(dateType[4]);
//		minutes.setViewAdapter(minuteAdapter);
//		int curDay = Math.min(maxMinute, minutes.getCurrentItem() + 1);
//		minutes.setCurrentItem(curDay - 1, true);*/
//		String hourss = "";
//		String minutess = "";
//		if(hour.getCurrentItem()<=9){
//			hourss = "0"+(hour.getCurrentItem());
//		}else{
//			hourss = ""+(hour.getCurrentItem());
//		}
//		if(minutes.getCurrentItem()<=9){
//			minutess = "0"+(minutes.getCurrentItem());
//		}else{
//			minutess = ""+(minutes.getCurrentItem());
//		}
//
//		age = hourss + ":" + minutess;
//	}
//
//
//
//	/**
//	 * Adapter for numeric wheels. Highlights the current value.
//	 */
//	private class DateNumericAdapter extends NumericWheelAdapter {
//		// Index of current item
//		int currentItem;
//		// Index of item to be highlighted
//		int currentValue;
//
//		/**
//		 * Constructor
//		 */
//		public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
//			super(context, minValue, maxValue);
//			this.currentValue = current;
//			setTextSize(24);
//		}
//
//		protected void configureTextView(TextView view) {
//			super.configureTextView(view);
//			view.setTypeface(Typeface.SANS_SERIF);
//		}
//
//		public CharSequence getItemText(int index) {
//			currentItem = index;
//			return super.getItemText(index);
//		}
//
//	}
//	public interface DateSelectedListener{
//		public abstract void onDateSelectedListener(String text);
//	}
//}
