package zuzusoft.com.bagbag.home;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;

import zuzusoft.com.bagbag.R;
import zuzusoft.com.bagbag.helper.BaseActivity;
import zuzusoft.com.bagbag.helper.MyBrowser;
import zuzusoft.com.bagbag.rest.ApiClient;

public class TosActivity extends BaseActivity implements MyBrowser.MyBrowserListener{

    private String URL = "";
    private WebView myWebView;
    public ProgressBar pageLoader;
    public int AnimationMode;
    public int LoadMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tos);

        //initialization
        checkInternet();
    }



    public void checkInternet(){

        if (ApiClient.isNetworkAvailable(context)) {

            //initialization
            initialization();

        } else {

            //show dialog ... leader board is empty
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Network");
            builder.setMessage("Please check your internet connection and try again");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    checkInternet();
                }
            });

            builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();

                }
            });

            AlertDialog d = builder.create();
            d.setCancelable(false);
            d.show();

        }

    }


    private void initialization() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Legal");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        URL = "https://termsfeed.com/terms-conditions/c61fc27fa07f60d972042a71df17ab59";


        myWebView = (WebView) findViewById(R.id.webView);
        // Configure related browser settings
        myWebView.getSettings().setLoadsImagesAutomatically(true);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // Configure the client to use when opening URLs
        myWebView.setWebViewClient(new MyBrowser(this));

        // Enable responsive layout
        myWebView.getSettings().setUseWideViewPort(true);
        // Zoom out if the content width is greater than the width of the viewport
        myWebView.getSettings().setLoadWithOverviewMode(true);


        pageLoader = (ProgressBar) findViewById(R.id.progressBar);

        // Load the initial URL
        myWebView.loadUrl(URL);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onEduPageStarted(WebView view, String url, Bitmap favicon) {

    }

    @Override
    public void onEduPageFinished(WebView view, String url) {

        pageLoader.setVisibility(View.GONE);
    }

    @Override
    public void onEduReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

    }
}