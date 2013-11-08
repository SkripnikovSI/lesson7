package ru.ifmo.ctddev.skripnikov.androidhw6;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddChannelActivity extends Activity {
    private EditText editName;
    private EditText editLink;
    private EditText editEncoding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_channel);

        editName = (EditText) findViewById(R.id.editName);
        editLink = (EditText) findViewById(R.id.editLink);
        editEncoding = (EditText) findViewById(R.id.editEncoding);
    }

    public void onClickAdd(View v) {
        DBStorage db = new DBStorage(this);
        db.addChannel(editName.getText().toString(),
                "http://"+editLink.getText().toString(),
                editEncoding.getText().toString());
        db.destroy();
        finish();
    }
}
