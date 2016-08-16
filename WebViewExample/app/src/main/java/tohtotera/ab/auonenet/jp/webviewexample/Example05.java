package tohtotera.ab.auonenet.jp.webviewexample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;



/**
 * Created by Weider on 2015/11/29.
 */
public class Example05 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getCharSequenceExtra("url").toString());
        webView.addJavascriptInterface(this, "android");
        setContentView(webView, new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }

    public void js(String action, String uri) {
        Intent intent = new Intent(action, Uri.parse(uri));
        startActivity(intent);
    }
}
