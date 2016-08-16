package tohtotera.ab.auonenet.jp.webviewexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * Created by Weider on 2015/11/29.
 */
public class Example04 extends Activity {
    float[] history;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final WebView webView = new WebView(this);

        webView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    history = new float[event.getHistorySize()];
                    for (int i = 0; i < history.length; i++) {
                        history[i] = event.getHistoricalX(i);
                    }
                } else {
                    if (event.getAction() == MotionEvent.ACTION_UP ||
                            event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        if (history != null && history.length >= 3) {
                            boolean back = webView.canGoBack();
                            boolean forward = webView.canGoForward();
                            for (int i = 0; i < history.length; i++) {
                                if (back && (i != 0 && history[i] >= history[i - 1])) {
                                    back = false;
                                }
                                if (forward && (i != 0 && history[i] <= history[i - 1])) {
                                    forward = false;
                                }
                            }
                            if (back) {
                                webView.goBack();
                                return true;
                            } else if (forward) {
                                webView.goForward();
                                return true;
                            }
                        }
                    }
                    history = null;
                }
                return false;
            }

        });

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(getIntent().getCharSequenceExtra("url").toString());
        setContentView(webView, new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }
}
