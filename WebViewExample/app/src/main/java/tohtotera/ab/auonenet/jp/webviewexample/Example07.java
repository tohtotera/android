package tohtotera.ab.auonenet.jp.webviewexample;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/** Google 翻訳を使用するサンプル。 */
public class Example07 extends Activity {

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, final String message, JsResult result) {
            TextView textView = (TextView)findViewById(R.id.TextView01);
            textView.setText(message);
            result.confirm();
            return true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example07);

        WebView webView = (WebView)findViewById(R.id.WebView01);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.loadUrl(getIntent().getCharSequenceExtra("url").toString());
    }

}