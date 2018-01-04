package com.phone.copyweixin;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;

public class MainActivity extends Activity {
    MyWeChatView myWeChatView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         myWeChatView = new MyWeChatView(this);
        setContentView(myWeChatView);
        myWeChatView.setChatKeybordListerner(new ChatKeybordListerner() {
            @Override
            public void dissmissMainView() {
                Toast.makeText(MainActivity.this,"关闭窗口",Toast.LENGTH_LONG).show();
            }
            @Override
            public void tabShowContent(String message) {
                Toast.makeText(MainActivity.this,"message"+message,Toast.LENGTH_LONG).show();
            }
            @Override
            public void showMyMenuView() {
                Toast.makeText(MainActivity.this,"新开窗口",Toast.LENGTH_LONG).show();
            }
        });
    }
}
