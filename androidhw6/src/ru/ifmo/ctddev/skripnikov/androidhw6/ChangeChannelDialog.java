package ru.ifmo.ctddev.skripnikov.androidhw6;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ChangeChannelDialog extends DialogFragment implements View.OnClickListener {
    Channel channel;
    Listener listener;
    Context context;
    EditText etName;
    EditText etLink;
    EditText etEncoding;

    ChangeChannelDialog(Channel channel, Context context, Listener listener) {
        this.channel = channel;
        this.context = context;
        this.listener = listener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().setTitle("Edit Channel");
        View v = inflater.inflate(R.layout.dialog, null);
        v.findViewById(R.id.dialog_delete).setOnClickListener(this);
        v.findViewById(R.id.dialog_change).setOnClickListener(this);
        v.findViewById(R.id.dialog_cancel).setOnClickListener(this);
        etName = (EditText) v.findViewById(R.id.dialog_name);
        etLink = (EditText) v.findViewById(R.id.dialog_link);
        etEncoding = (EditText) v.findViewById(R.id.dialog_encoding);
        etName.setText(channel.name);
        etLink.setText(channel.link);
        etEncoding.setText(channel.encoding);
        return v;
    }

    public void onClick(View v) {
        int id = v.getId();
        DBStorage dbStorage;
        switch (id) {
            case R.id.dialog_delete:
                dbStorage = new DBStorage(context);
                dbStorage.deleteChannel(channel.id);
                dbStorage.destroy();
                listener.onDialogDismissed();
                break;
            case R.id.dialog_change:
                dbStorage = new DBStorage(context);
                channel.name = etName.getText().toString();
                channel.link = etLink.getText().toString();
                channel.encoding = etEncoding.getText().toString();
                dbStorage.changeChannel(channel);
                dbStorage.destroy();
                listener.onDialogDismissed();
                break;

            case R.id.dialog_cancel:
                break;
        }
        dismiss();
    }

}
