package zuzusoft.com.bagbag.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyBrowser extends WebViewClient {

    private String TAG = "My browser";
    private Context context;

    private MyBrowserListener mListener;
    public interface MyBrowserListener{

        void onEduPageStarted(WebView view, String url, Bitmap favicon);
        void onEduPageFinished(WebView view, String url);
        void onEduReceivedError(WebView view, WebResourceRequest request, WebResourceError error);

    }

    public MyBrowser(Context context) {

        this.context = context;
        mListener = (MyBrowserListener)context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl(request.getUrl().toString());
        return true;
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        Log.v(TAG, "onPageStarted");

        if(mListener != null){

            mListener.onEduPageStarted(view, url, favicon);
        }

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        Log.v(TAG, "onPageFinished");

        if(mListener != null){

            mListener.onEduPageFinished(view, url);
        }
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);

        Log.v(TAG, "onLoadResource");
    }


    @Override
    public void onPageCommitVisible(WebView view, String url) {
        super.onPageCommitVisible(view, url);

        Log.v(TAG, "onPageCommitVisible");
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);

        Log.v(TAG, "onReceivedError");

        if(mListener != null){

            mListener.onEduReceivedError(view, request, error);
        }

    }
}