package tohtotera.ab.auonenet.jp.contactsample;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ContactSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TableLayout tablelayout = (TableLayout)findViewById(R.id.tablelayout);

        String strContact = null;

        //コンテンツリゾルバの取得
        ContentResolver resolver = getContentResolver();
        //Cursorの作成（コンタクトリストから全件取得
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()){
            //IDを取得
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //名前を取得
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //電話番号を取得
            String tell = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1));
            //取得したIDと名前、電話番号を結合
            strContact = id + " " + name + " " + tell;
            //複数の電話番号に対応するために行う処理
            Cursor phones = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                           null,
                                           ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " - " + id,
                                           null, null);
            while (phones.moveToNext()){
                //電話番号を取得
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //取得した電話番号を追加
                strContact = strContact + "\n" + phoneNumber;
            }
            phones.close();

            TableRow row = new TableRow(this);
            TextView display = new TextView(this);
            display.setText(strContact);
            row.addView(display);

            //tablelayoutにセット
            tablelayout.addView(row);
        }
        cursor.close();
    }
}
