package wsiiz.holker.bus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ScheduleList extends Activity {

    TextView mTextView;
    String[] schedule;
    ListView mListViewStations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        //find
        mTextView = (TextView) findViewById(R.id.tv_name_station);
        mListViewStations = (ListView) findViewById(R.id.lv_stations);

        String stationName = "";
        Intent fromPrev = getIntent();
        stationName = fromPrev.getStringExtra("stationName");
        schedule = fromPrev.getStringArrayExtra("schedule");
        Log.i("MyLog", "YEEEAS" + schedule[0]);

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, schedule);

        mListViewStations.setAdapter(arrayAdapter);

        mTextView.setText(stationName);
    }
}
