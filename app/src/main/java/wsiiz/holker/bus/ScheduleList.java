package wsiiz.holker.bus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ScheduleList extends Activity {

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        //find
        mTextView = (TextView) findViewById(R.id.tv_name_station);
        String stationName = "";
        Intent fromPrev = getIntent();
        stationName = fromPrev.getStringExtra("stationName");

        mTextView.setText(stationName);
    }
}
