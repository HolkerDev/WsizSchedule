package wsiiz.holker.bus;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

    Button mButtonDownload;
    String link = null;
    TextView mTextViewDate;
    Calendar tempDate = Calendar.getInstance();
    Button mButtonCatyniaGo;
    Button mButtonCieplinskiegoGo;
    Button mButtonWarszawaGo;
    Button mButtonTescoGo;
    Button mButtonTyczynGo;
    Button mButtonKielnarowaBack;
    Button mButtonTyczynBack;
    Boolean mUpToDate;
    TextView mTextViewUpdate;
    String result = null;
    SharedPreferences mSharedPreferences;

    List<List<String>> column = new ArrayList<>();

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }


    public void toSchedule(View view) {
        Intent goToSchedule = new Intent(getApplicationContext(), ScheduleList.class);
        switch (view.getId()) {
            case R.id.btn_catynia_go:
                goToSchedule.putExtra("stationName", "ul. Ofiar Katynia (kladka piesza)");
                String[] array = column.get(0).toArray(new String[0]);
                goToSchedule.putExtra("schedule", array);
                startActivity(goToSchedule);
                break;
            case R.id.btn_cieplinskiego_go:
                goToSchedule.putExtra("stationName", "ul. Cieplinskiego");
                String[] array1 = column.get(1).toArray(new String[0]);
                goToSchedule.putExtra("schedule", array1);
                startActivity(goToSchedule);
                break;
            case R.id.btn_warszawa_go:
                goToSchedule.putExtra("stationName", "Al. Powst. Warszawy");
                String[] array2 = column.get(2).toArray(new String[0]);
                goToSchedule.putExtra("schedule", array2);
                startActivity(goToSchedule);
                break;
            case R.id.btn_tesco_go:
                goToSchedule.putExtra("stationName", "Parking TESCO");
                String[] array3 = column.get(3).toArray(new String[0]);
                goToSchedule.putExtra("schedule", array3);
                startActivity(goToSchedule);
                break;
            case R.id.btn_tyczyn_go:
                goToSchedule.putExtra("stationName", "Tyczyn Park to Kielnarowa");
                String[] array4 = column.get(4).toArray(new String[0]);
                goToSchedule.putExtra("schedule", array4);
                startActivity(goToSchedule);
                break;
            case R.id.btn_kielnarowa_back:
                goToSchedule.putExtra("stationName", "Kielnarowa");
                String[] array6 = column.get(6).toArray(new String[0]);
                goToSchedule.putExtra("schedule", array6);
                startActivity(goToSchedule);
                break;
            case R.id.btn_tyczyn_back:
                goToSchedule.putExtra("stationName", "Tyczyn Park to TESCO");
                String[] array7 = column.get(7).toArray(new String[0]);
                goToSchedule.putExtra("schedule", array7);
                startActivity(goToSchedule);
                break;
            default:
                break;
        }
    }

    public class getLinkToSchedule extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(strings[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    data = reader.read();
                    result.append(current);
                }

                return result.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


    public String currentDate(Calendar data) {
        StringBuilder curDate = new StringBuilder();
        String[] month = new String[]{
                "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"
        };
        int monthNumber = data.get(Calendar.MONTH);
        curDate.append(String.valueOf(data.get(Calendar.DATE)));
        curDate.append("-");
        curDate.append(month[monthNumber]);
        curDate.append("-");
        curDate.append(String.valueOf(data.get(Calendar.YEAR)));
        return curDate.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getSharedPreferences("wsiiz.holker.bus", MODE_PRIVATE);

        //find
        mButtonDownload = (Button) findViewById(R.id.button);
        mTextViewDate = (TextView) findViewById(R.id.tv_date);
        mButtonCatyniaGo = (Button) findViewById(R.id.btn_catynia_go);
        mButtonCieplinskiegoGo = (Button) findViewById(R.id.btn_cieplinskiego_go);
        mButtonWarszawaGo = (Button) findViewById(R.id.btn_warszawa_go);
        mButtonTescoGo = (Button) findViewById(R.id.btn_tesco_go);
        mButtonTyczynGo = (Button) findViewById(R.id.btn_tyczyn_go);
        mButtonTyczynBack = (Button) findViewById(R.id.btn_tyczyn_back);
        mButtonKielnarowaBack = (Button) findViewById(R.id.btn_kielnarowa_back);
        mTextViewUpdate = (TextView) findViewById(R.id.tv_up_to_date);

        String pathCheck = Objects.requireNonNull(getExternalFilesDir(null)).getPath() +
                "/" + mSharedPreferences.getString("lastFileName", "") + ".xlsx";

        File myFile = new File(pathCheck);
        FileInputStream fis = null;
        try {
            XlsxParser parser = new XlsxParser();
            fis = new FileInputStream(myFile);
            mUpToDate = true;
            column = parser.startParse(pathCheck);
            mTextViewUpdate.setText("Ready to use");
            mButtonDownload.setEnabled(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mUpToDate = false;
            mTextViewUpdate.setText("Need to download data");
            mButtonKielnarowaBack.setEnabled(false);
            mButtonTyczynBack.setEnabled(false);
            mButtonTyczynGo.setEnabled(false);
            mButtonTescoGo.setEnabled(false);
            mButtonWarszawaGo.setEnabled(false);
            mButtonCatyniaGo.setEnabled(false);
            mButtonCieplinskiegoGo.setEnabled(false);
            if (isInternetAvailable()) {
                mButtonDownload.setEnabled(false);
                mTextViewUpdate.setText("No internet connection");
            } else {
                mButtonDownload.setEnabled(true);
            }

        }


        mTextViewDate.setText(currentDate(tempDate));


        mButtonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String subLink = null;
                mButtonDownload.setEnabled(false);

                getLinkToSchedule task = new getLinkToSchedule();

                try {
                    result = task.execute("https://observer.name/api/wsiz").get();
                    JSONObject object = new JSONObject(result);


                    String schedule = object.getString("wsizbus");
                    object = new JSONObject(schedule);
                    link = object.getString("url");

                    Pattern p = Pattern.compile("narowa\\/(.*?).xlsx");
                    Matcher m = p.matcher(link);
                    while (m.find()) {
                        Log.i("MyLog", m.group(1));
                        subLink = m.group(1);
                    }


                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Uri uri = Uri.parse(link);


                DownloadManager.Request request1 = new DownloadManager.Request(uri);
                request1.setDescription("Description");
                request1.setTitle("Title");
                request1.setVisibleInDownloadsUi(false);

                request1.setDestinationInExternalFilesDir(getApplicationContext(), null, subLink + ".xlsx");

                DownloadManager manager1 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Objects.requireNonNull(manager1).enqueue(request1);
                if (DownloadManager.STATUS_SUCCESSFUL == 8) {
                    Toast.makeText(getApplicationContext(), "Successful",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG);
                }

                mSharedPreferences.edit().putString("lastFileName", subLink).apply();

                Toast.makeText(getApplicationContext(), "Restart your app", Toast.LENGTH_LONG).show();
                finishAndRemoveTask();
            }
        });

    }

}
