package com.phone.copyweixin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "ResourceAsColor", "ShowToast" })
public class PopMenus {
	private JSONArray jsonArray;
	private Context context;
	private PopupWindow popupWindow;
	private LinearLayout listView;
	private int width, height;
	private View containerView;
	ChatKeybordListerner mchatKeybordListerner;
	public PopMenus(Context context, JSONArray _jsonArray, int _width, int _height, ChatKeybordListerner chatKeybordListerner) {
		this.context = context;
		this.jsonArray = _jsonArray;
		this.width = _width;
		this.height = _height;
		mchatKeybordListerner=chatKeybordListerner;
		containerView = LayoutInflater.from(context).inflate(R.layout.popmenus, null);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
		containerView.setLayoutParams(lp);
		// 设置 listview
		listView = (LinearLayout) containerView.findViewById(R.id.layout_subcustommenu);
		try {
			setSubMenu();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listView.setBackgroundColor(Color.parseColor("#ff646464"));
		listView.setFocusableInTouchMode(true);
		listView.setFocusable(true);

		popupWindow = new PopupWindow(containerView, width == 0 ? LayoutParams.WRAP_CONTENT : width, height == 0 ? LayoutParams.WRAP_CONTENT : height);
	}

	// 下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		popupWindow.showAsDropDown(parent);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 刷新状态
		popupWindow.update();

		popupWindow.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			@Override
			public void onDismiss() {
			}
		});
	}

	public void showAtLocation(View parent) {
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		containerView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int[] location = new int[2];
		int popupWidth = containerView.getMeasuredWidth();
		int popupHeight =  containerView.getMeasuredHeight();
		parent.getLocationOnScreen(location);
		int x = (location[0]+parent.getWidth()/2)-popupWidth/2;
		int y = location[1]-popupHeight;
		popupWindow.showAtLocation(parent, Gravity.NO_GRAVITY,x , y);

		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 刷新状态
		popupWindow.update();

		popupWindow.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			@Override
			public void onDismiss() {
			}
		});
	}

	// 隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();
	}

	void setSubMenu() throws JSONException {
		listView.removeAllViews();
		for (int i = 0; i < jsonArray.length(); i++) {
			final JSONObject ob = jsonArray.getJSONObject(i);
			LinearLayout layoutItem = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.pomenu_menuitem, null);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
			containerView.setLayoutParams(lp);
			layoutItem.setFocusable(true);
			TextView tv_funbtntitle = (TextView) layoutItem.findViewById(R.id.pop_item_textView);
			View pop_item_line = layoutItem.findViewById(R.id.pop_item_line);
			if ((i + 1) == jsonArray.length()) {
				pop_item_line.setVisibility(View.GONE);
			}
			tv_funbtntitle.setText(ob.getString("title"));
			layoutItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						mchatKeybordListerner.tabShowContent(ob.getString("title"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					dismiss();

				}
			});
			listView.addView(layoutItem);
		}
		listView.setVisibility(View.VISIBLE);
	}

}
