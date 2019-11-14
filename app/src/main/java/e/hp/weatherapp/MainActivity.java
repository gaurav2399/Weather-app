package e.hp.weatherapp;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView answer;
    EditText city;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city=(EditText)findViewById(R.id.city);
        answer=(TextView)findViewById(R.id.Result);
        //link=new DownlaodLink();


    }
    public void weather(View view) {
        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(city.getWindowToken(),0);
        String cityname;
        cityname = city.getText().toString();
        DownlaodLink link=new DownlaodLink();
        //String name=(String)cityname;
       // Toast.makeText(getApplicationContext(),cityname,Toast.LENGTH_SHORT).show();
        Log.i("city name999",cityname);

            String weather;
            try {
                link.execute("https://openweathermap.org/data/2.5/weather?q="+cityname+"&appid=b6907d289e10d714a6e88b30761fae22").get();

            } catch (Exception e) {
                Log.i("name999999", "yeh koi name nhi h");
                Toast.makeText(getApplicationContext(),"Could Not Find Weather",Toast.LENGTH_SHORT).show();
            }

    }

    public void run(View view) {
        answer.setText("");
    }


    public class DownlaodLink extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection=null;
            String result="";
            Log.i("In function on backgro","99999999999");
            try {
                url=new URL(strings[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                Log.i("Weather broadcasting...",result);
                return result;

            } catch (RuntimeException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could Not Find Weather",Toast.LENGTH_SHORT).show();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could Not Find Weather",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could Not Find Weather",Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String message="";
            Log.i("In function on post","8888888888888");
            try {
                JSONObject jsonObject=new JSONObject(result);

                String weather=jsonObject.getString("weather");
                Log.i("weather info....",weather);
                JSONArray jsonArray=new JSONArray(weather);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonPart=jsonArray.getJSONObject(i);
                    String main=jsonPart.getString("main");
                    String description=jsonPart.getString("description");
                    if(main!=""&&description!="") {
                        message = main + " : " + description + "\r\n";
                    }
                    if(message!=""){
                        answer.setText(message);
                    }

                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Could Not Find Weather",Toast.LENGTH_SHORT).show();
            }

        }

    }
}
