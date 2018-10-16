package wsiiz.holker.bus;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    Button mButtonDownload;
    String link = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String result = null;


        //find
        mButtonDownload = (Button) findViewById(R.id.button);

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
