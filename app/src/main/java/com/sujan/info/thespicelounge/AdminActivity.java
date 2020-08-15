package com.sujan.info.thespicelounge;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toolbar;

import com.sujan.info.thespicelounge.Utils.Constants;
import com.sujan.info.thespicelounge.Utils.MyPreferences;
import com.sujan.info.thespicelounge.Utils.Utils;

public class AdminActivity extends AppCompatActivity {

    WebView webView;
    Toolbar tb;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        init();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_logout:
                MyPreferences.resetAllPreferences(getApplicationContext());
                Utils.activityTransitionRemovingHistory(this,new Intent(this, MainActivity.class));
                break;

            default:
                break;
        }

        return true;
    }

    private void init() {

        tb = (Toolbar) findViewById(R.id.toolbar_admin_activity);
        tb.setTitle("Admin Alan");
        tb.inflateMenu(R.menu.toolbar);
        setActionBar(tb);

        webView=(WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new MyBrowser());
        webView.loadUrl("https://thespicelounge.000webhostapp.com/adminPanelForApp.php");
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Utils.displayShortToastMessage(this, "Please click BACK again to exit");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }


}
