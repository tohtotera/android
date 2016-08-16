package tohtotera.ab.auonenet.jp.webviewexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

/**
 * Created by Weider on 2015/11/29.
 */
public class Example02 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example02);

        final WebView webView = (WebView) findViewById(R.id.WebView01);
        webView.setWebViewClient(new WebViewClient());
        final EditText editText = (EditText) findViewById(R.id.EditText01);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    webView.loadUrl(editText.getEditableText().toString());
                    return true;
                }
                return false;
            }
        });
    }
}
