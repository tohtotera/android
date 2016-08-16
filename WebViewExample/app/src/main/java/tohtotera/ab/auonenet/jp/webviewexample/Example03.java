package tohtotera.ab.auonenet.jp.webviewexample;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Weider on 2015/11/29.
 */
public class Example03 extends Activity implements View.OnClickListener, View.OnKeyListener{

    class MyWebViewClient extends WebViewClient{
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload){
            back.setEnabled(webView.canGoBack());
            forward.setEnabled(webView.canGoForward());
        }
    }

    Button back;
    Button forward;
    EditText url;
    WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example03);

        back = (Button)findViewById(R.id.Button01);
        forward = (Button)findViewById(R.id.Button02);
        url = (EditText)findViewById(R.id.EditText01);
        webView = (WebView)findViewById(R.id.WebView01);
        webView.setWebViewClient(new MyWebViewClient());

        back.setEnabled(false);
        forward.setEnabled(false);

        back.setOnClickListener(this);
        forward.setOnClickListener(this);
        url.setOnKeyListener(this);
    }

    @Override
    public void onClick(View v){
        if (v == back){
            webView.goBack();
        }else if (v == forward){
            webView.goForward();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_ENTER){
            webView.loadUrl(url.getText().toString());
            return true;
        }
        return false;
    }
}
