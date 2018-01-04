package com.phone.copyweixin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phone.copyweixin.keybord.ACEChatKeyboardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhang on 2017/12/29.
 */

public class MyWeChatView extends RelativeLayout implements View.OnClickListener {
    private RelativeLayout rootView;
    private LinearLayout layout_custommenu, layout_custom_toolbar,layout_btn,inputviews,plugin_show_content;
    private PopMenus popupWindow_custommenu;
    private LinearLayout layout_exchange;
    private View keybordicon;
    private Context mcontext;
    private ImageButton plugin_chatkeyboard_btn_keybord_down;
    private ACEChatKeyboardView aceChatKeyboardView;
    private WebView webview;
    private Button show_meassge_info,show_meassge_info_two;
    private RelativeLayout chat_main_view;
    private ImageButton plugin_back_two,plugin_back;
    private ChatKeybordListerner chatKeybordListerner;

    public void setChatKeybordListerner(ChatKeybordListerner chatKeybordListerner) {
        this.chatKeybordListerner = chatKeybordListerner;
    }

    public MyWeChatView(Context context) {
        this(context,null);
    }

    public MyWeChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext=context;
        initView(context);
        initData(context);
        initListener();
    }

    private void initListener() {
        layout_exchange.setOnClickListener(this);
        plugin_chatkeyboard_btn_keybord_down.setOnClickListener(this);
        show_meassge_info.setOnClickListener(this);
        show_meassge_info_two.setOnClickListener(this);
        plugin_back_two.setOnClickListener(this);
        plugin_back.setOnClickListener(this);
    }

    private void initData(Context context) {
        try {
            setCustomMenu(new JSONObject(Gloable.jsonStr),context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        rootView = (RelativeLayout) layoutInflater.inflate(R.layout.activity_main, this);
        //底部外层
        layout_custom_toolbar = (LinearLayout)rootView.findViewById(R.id.layout_custom_toolbar);
        //菜单栏
        layout_custommenu = (LinearLayout)rootView.findViewById(R.id.layout_custommenu);
        //键盘
        layout_exchange=(LinearLayout)rootView.findViewById(R.id.layout_exchange);
        //
//        layout_custom_input=(RelativeLayout) rootView.findViewById(R.id.layout_custom_input);
        //修改图标
        keybordicon=rootView.findViewById(R.id.keybordicon);
        layout_btn=(LinearLayout)rootView.findViewById(R.id.layout_btn);
        inputviews=(LinearLayout)rootView.findViewById(R.id.inputviews);
        chat_main_view=(RelativeLayout) rootView.findViewById(R.id.chat_main_view);
        plugin_show_content=(LinearLayout) rootView.findViewById(R.id.plugin_show_content);

        aceChatKeyboardView = new ACEChatKeyboardView(mcontext);
        plugin_chatkeyboard_btn_keybord_down=(ImageButton) aceChatKeyboardView.findViewById(R.id.plugin_chatkeyboard_btn_keybord_down);


        //webview 初始化
        webview=rootView.findViewById(R.id.webview);
        show_meassge_info=(Button) rootView.findViewById(R.id.show_meassge_info);
        show_meassge_info_two=(Button)rootView.findViewById(R.id.show_meassge_info_two);
        plugin_back_two=(ImageButton) rootView.findViewById(R.id.plugin_back_two);
        plugin_back=(ImageButton) rootView.findViewById(R.id.plugin_back);

        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webview.loadUrl("http://www.baidu.com");



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_exchange: //显示键盘输入
                    layout_btn.setVisibility(GONE);
                    inputviews.setVisibility(VISIBLE);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    lp.bottomMargin=0;
                if(inputviews.getChildCount()<1){
                    inputviews.addView(aceChatKeyboardView,lp);
                }
                break;
            case R.id.plugin_chatkeyboard_btn_keybord_down://隐藏键盘显示菜单
                layout_btn.setVisibility(VISIBLE);
                inputviews.setVisibility(GONE);
                break;

            case R.id.show_meassge_info: // 显示个人信息页面
                chat_main_view.setVisibility(INVISIBLE);
                plugin_show_content.setVisibility(VISIBLE);
                break;
            case R.id.show_meassge_info_two:// 显示自定菜单
                chatKeybordListerner.showMyMenuView();
                break;

            case R.id.plugin_back:
                chatKeybordListerner.dissmissMainView();
                break;
            case R.id.plugin_back_two: // 返回上级界面
                chat_main_view.setVisibility(VISIBLE);
                plugin_show_content.setVisibility(GONE);
                break;

        }



    }


    /**
     * 获取数据，并填充数据
     * @param jsonObject
     * @param context
     * @throws JSONException
     */
    private void setCustomMenu(JSONObject jsonObject, final Context context) throws JSONException {
        JSONArray jsonCustomMenu = jsonObject.getJSONArray("customemenu");
        if (jsonCustomMenu != null && jsonCustomMenu.length() > 0) {
            layout_custom_toolbar.setVisibility(View.VISIBLE);
            layout_custommenu.removeAllViews();
            JSONArray btnJson = jsonCustomMenu;
            for (int i = 0; i < btnJson.length(); i++) {
                final JSONObject ob = btnJson.getJSONObject(i);
                LinearLayout layout = (LinearLayout) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_custommenu, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                layout.setLayoutParams(lp);
                ImageView image_tab=layout.findViewById(R.id.icon_tab);
                TextView tv_custommenu_name = (TextView) layout.findViewById(R.id.tv_custommenu_name);
                tv_custommenu_name.setText(ob.getString("title"));
                if (ob.getJSONArray("sub").length() > 0) // 显
                // 示三角
                {
                    image_tab.setVisibility(VISIBLE);
//                        tv_custommenu_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_black, 0);
                } else // 隐藏三角
                {
                    image_tab.setVisibility(GONE);
//                    tv_custommenu_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        try {
                            if (ob.getJSONArray("sub").length() == 0) {
                                chatKeybordListerner.tabShowContent(ob.getString("title"));
                            } else {
                                popupWindow_custommenu = new PopMenus(context, ob.getJSONArray("sub"), v.getWidth() + 10, 0,chatKeybordListerner);
                                popupWindow_custommenu.showAtLocation(v);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                layout_custommenu.addView(layout);
            }
        } else {
            layout_custom_toolbar.setVisibility(View.GONE);
        }
    }

  
}
