package tohtotera.ab.auonenet.jp.webviewexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/** JavaScript をハンドリングするサンプル。 */
public class Example06 extends Activity {

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            if (message.length() != 0) {
                AlertDialog.Builder buildr = new AlertDialog.Builder(Example06.this);
                buildr.setTitle("From JavaScript").setMessage(message).show();
                result.cancel();
                return true;
            }
            return false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.loadUrl(getIntent().getCharSequenceExtra("url").toString());
        setContentView(webView, new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }

}