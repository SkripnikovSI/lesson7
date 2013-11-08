package ru.ifmo.ctddev.skripnikov.androidhw6;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ChannelsListActivity extends Activity {

    public final static String BROADCAST_ACTION = "ru.ifmo.ctddev.skripnikov.androidhw6.ba.update";

    private ListView listView;
    private BroadcastReceiver br;
    private ChangeChannelDialog ccd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView = (ListView) findViewById(R.id.listView);

        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Channel c = (Channel) intent.getSerializableExtra("channel");
                Toast.makeText(getBaseContext(), c.name + " +" + intent.getIntExtra("number", 0),
                        Toast.LENGTH_SHORT).show();
                reloadChannelsList();
            }
        };

        PendingIntent pi = PendingIntent.getService(this, 0, new Intent(this, FeedReaderService.class), 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(pi);
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000, 300000, pi);
    }
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BROADCAST_ACTION));
        reloadChannelsList();
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    private void reloadChannelsList() {
        DBStorage dbStorage = new DBStorage(getBaseContext());
        final Channel[] channels = dbStorage.getChannels();
        dbStorage.destroy();

        ChannelsListAdapter adapter = new ChannelsListAdapter(getBaseContext(), channels);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                ccd = new ChangeChannelDialog(channels[position], getBaseContext(), new Listener() {
                    @Override
                    public void onDialogDismissed() {
                        reloadChannelsList();
                    }
                });
                ccd.show(getFragmentManager(), "ccd");
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ItemsListActivity.class);
                intent.putExtra("channel", channels[position]);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.channels_list_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(this, AddChannelActivity.class));
                return true;
            case R.id.action_reload:
                DBStorage dbStorage = new DBStorage(this);
                Channel[] channels = dbStorage.getChannels();
                dbStorage.destroy();
                for (Channel channel : channels) {
                    Intent intent = new Intent(this, FeedReaderService.class);
                    intent.putExtra("channel", channel);
                    startService(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
