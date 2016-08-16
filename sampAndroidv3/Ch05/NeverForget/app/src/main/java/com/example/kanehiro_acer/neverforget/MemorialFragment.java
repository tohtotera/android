package com.example.kanehiro_acer.neverforget;


import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class MemorialFragment extends Fragment {
    public static Fragment newInstance(Context context) {
        MemorialFragment f = new MemorialFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_memorial,null);
        return  root;
    }
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = this.getActivity().getSharedPreferences("memorial", Context.MODE_PRIVATE);
        String wedding = prefs.getString("wedding", "");
        String birthday = prefs.getString("birthday", "");
        String birthday1 = prefs.getString("birthday1", "");
        String birthday2 = prefs.getString("birthday2", "");
        String birthday3 = prefs.getString("birthday3", "");

        EditText edText1 = (EditText) getView().findViewById(R.id.editText1);
        edText1.setText(wedding);
        EditText edText2 = (EditText) getView().findViewById(R.id.editText2);
        edText2.setText(birthday);
        EditText edText3 = (EditText) getView().findViewById(R.id.editText3);
        edText3.setText(birthday1);
        EditText edText4 = (EditText) getView().findViewById(R.id.editText4);
        edText4.setText(birthday2);
        EditText edText5 = (EditText) getView().findViewById(R.id.editText5);
        edText4.setText(birthday3);
    }

    @Override
    public void onPause() {
        super.onPause();
        EditText edText1 = (EditText) getView().findViewById(R.id.editText1);
        EditText edText2 = (EditText) getView().findViewById(R.id.editText2);
        EditText edText3 = (EditText) getView().findViewById(R.id.editText3);
        EditText edText4 = (EditText) getView().findViewById(R.id.editText4);
        EditText edText5 = (EditText) getView().findViewById(R.id.editText5);

        String wedding = edText1.getText().toString();
        String birthday = edText2.getText().toString();
        String birthday1 = edText3.getText().toString();
        String birthday2 = edText4.getText().toString();
        String birthday3 = edText5.getText().toString();

        // 保存
        SharedPreferences prefs = this.getActivity().getSharedPreferences("memorial", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("wedding",wedding);
        editor.putString("birthday",birthday);
        editor.putString("birthday1",birthday1);
        editor.putString("birthday2",birthday2);
        editor.putString("birthday3",birthday3);
        //editor.commit();
        editor.apply();     // commitの非同期
    }

}
