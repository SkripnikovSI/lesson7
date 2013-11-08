package ru.ifmo.ctddev.skripnikov.androidhw6;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemsListAdapter extends ArrayAdapter<FeedItem> {
    private final Context context;
    private final FeedItem[] feed;

    public ItemsListAdapter(Context context, FeedItem[] feed) {
        super(context, R.layout.feed_item, feed);
        this.context = context;
        this.feed = feed;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View feedItem;
        if (convertView == null) {
            feedItem = ((LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.feed_item, parent, false);
        } else {
            feedItem = convertView;
        }
        ((TextView) feedItem.findViewById(R.id.feed_item_title))
                .setText(feed[position].title);
        return feedItem;
    }
}
