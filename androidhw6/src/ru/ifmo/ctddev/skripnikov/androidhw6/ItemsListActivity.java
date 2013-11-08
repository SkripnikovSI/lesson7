package ru.ifmo.ctddev.skripnikov.androidhw6;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemsListActivity extends Activity {
    private ListView listView;
    private Channel channel;
    private BroadcastReceiver br;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        channel = (Channel) getIntent().getSerializableExtra("channel");
        listView = (ListView) findViewById(R.id.listView);
        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (((Channel) intent.getSerializableExtra("channel")).id == channel.id) {
                    Toast.makeText(getBaseContext(), channel.name + " updated",
                            Toast.LENGTH_SHORT).show();
                    reloadItemsList();
                }

            }
        };
        reloadItemsList();
    }

    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(ChannelsListActivity.BROADCAST_ACTION));
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    public void reloadItemsList() {
        DBStorage dbStorage = new DBStorage(this);
        final ArrayList<FeedItem> items = dbStorage.getItemsByChannelId(channel.id);
        channel.time = System.currentTimeMillis();
        channel.numberOfNewEntrys = 0;
        dbStorage.changeChannel(channel);
        dbStorage.destroy();
        ItemsListAdapter adapter = new ItemsListAdapter(getBaseContext(), items.toArray(new FeedItem[items.size()]));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(getBaseContext(), WebActivity.class);
                intent.putExtra("description", items.get(position).description);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items_list_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload:
                Intent intent = new Intent(this, FeedReaderService.class);
                intent.putExtra("channel", channel);
                startService(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
