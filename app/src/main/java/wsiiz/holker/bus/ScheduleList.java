package wsiiz.holker.bus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ScheduleList extends Activity {

    TextView mTextView;
    String[] schedule;
    ListView mListViewStations;
    String stationName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        //find
        mTextView = findViewById(R.id.tv_name_station);
        mListViewStations = findViewById(R.id.lv_stations);

        //get extra
        Intent fromPrev = getIntent();
        stationName = fromPrev.getStringExtra("stationName");
        schedule = fromPrev.getStringArrayExtra("schedule");

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, schedule);

        //Show whole list of dates
        mListViewStations.setAdapter(arrayAdapter);

        mTextView.setText(stationName);


        //Show map
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maps = new Intent(getApplicationContext(), MapsActivity.class);
                maps.putExtra("stationName", stationName);
                startActivity(maps);
            }
        });
    }
}
