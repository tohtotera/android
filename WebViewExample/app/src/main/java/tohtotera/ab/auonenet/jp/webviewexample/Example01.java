package tohtotera.ab.auonenet.jp.webviewexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by Weider on 2015/11/29.
 */
public class Example01 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        webView.loadUrl(getIntent().getCharSequenceArrayExtra("url").toString());
        setContentView(webView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

    }
}
