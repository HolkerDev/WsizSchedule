package wsiiz.holker.bus;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

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

    public void toSchedule(View view) {
        Intent goToSchedule = new Intent(getApplicationContext(), ScheduleList.class);
        switch (view.getId()) {
            case R.id.btn_catynia_go:
                goToSchedule.putExtra("stationName", "ul. Ofiar Katynia (kladka piesza)");
                startActivity(goToSchedule);
                break;
            case R.id.btn_cieplinskiego_go:
                goToSchedule.putExtra("stationName", "ul. Cieplinskiego");
                startActivity(goToSchedule);
                break;
            case R.id.btn_warszawa_go:
                goToSchedule.putExtra("stationName", "Al. Powst. Warszawy");
                startActivity(goToSchedule);
                break;
            case R.id.btn_tesco_go:
                goToSchedule.putExtra("stationName", "Parking TESCO");
                startActivity(goToSchedule);
                break;
            case R.id.btn_tyczyn_go:
                goToSchedule.putExtra("stationName", "Tyczyn Park to Kielnarowa");
                startActivity(goToSchedule);
                break;
            case R.id.btn_kielnarowa_back:
                goToSchedule.putExtra("stationName", "Kielnarowa");
                startActivity(goToSchedule);
                break;
            case R.id.btn_tyczyn_back:
                goToSchedule.putExtra("stationName", "Tyczyn Park to TESCO");
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
        String result = null;


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


        mTextViewDate.setText(currentDate(tempDate));

        getLinkToSchedule task = new getLinkToSchedule();

        try {
            result = task.execute("https://observer.name/api/wsiz").get();
            JSONObject object = new JSONObject(result);

            String schedule = object.getString("wsizbus");
//                Toast.makeText(getApplicationContext(), schedule, Toast.LENGTH_LONG).show();
            object = new JSONObject(schedule);
//                Toast.makeText(getApplicationContext(), object.getString("url"),
//                        Toast.LENGTH_LONG).show();
            link = object.getString("url");
            Toast.makeText(getApplicationContext(), link,
                    Toast.LENGTH_LONG).show();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mButtonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), link,
//                        Toast.LENGTH_LONG).show();
//                mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(link);
//                DownloadManager.Request request = new DownloadManager.Request(uri);
//                request.setNotificationVisibility(DownloadManager.Request
//                        .VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                idRequest = mDownloadManager.enqueue(request);


                DownloadManager.Request request1 = new DownloadManager.Request(uri);
                request1.setDescription("Description");   //appears the same in Notification bar while downloading
                request1.setTitle("Title");
                request1.setVisibleInDownloadsUi(false);

                request1.setDestinationInExternalFilesDir(getApplicationContext(), null, "file.xlsx");

                DownloadManager manager1 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Objects.requireNonNull(manager1).enqueue(request1);
                if (DownloadManager.STATUS_SUCCESSFUL == 8) {
                    Toast.makeText(getApplicationContext(), "Successful",
                            Toast.LENGTH_LONG).show();
                }
                XlsxParser parser = new XlsxParser();

                String path = Objects.requireNonNull(getExternalFilesDir(null)).getPath() + "/file.xlsx";

                String res = parser.startParse(path);

                //Log.i("MyLog", res);

            }
        });

    }

}
