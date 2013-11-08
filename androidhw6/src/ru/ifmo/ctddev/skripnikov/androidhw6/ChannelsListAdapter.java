package ru.ifmo.ctddev.skripnikov.androidhw6;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ChannelsListAdapter extends ArrayAdapter<Channel> {
    private final Context context;
    private final Channel[] channels;

    public ChannelsListAdapter(Context context, Channel[] channels) {
        super(context, R.layout.channel_item, channels);
        this.context = context;
        this.channels = channels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View feedItem;
        if (convertView == null) {
            feedItem = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.channel_item, parent, false);
        } else {
            feedItem = convertView;
        }
        ((TextView) feedItem.findViewById(R.id.channel_item_title))
                .setText(channels[position].name);
        TextView number = (TextView) feedItem.findViewById(R.id.channel_item_new);
        if (channels[position].numberOfNewEntrys != 0)
            number.setText("+" + channels[position].numberOfNewEntrys);
        return feedItem;
    }
}
