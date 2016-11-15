package com.iiitd.apurupa.mcassignment.connecttoiiitd;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mconnect;
    private TextView mtitle;
    private TextView mcontent;
    private String title;
    private static String url = "https://www.iiitd.ac.in/about";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mconnect =(Button)findViewById(R.id.connectbutton);
        mtitle=(TextView)findViewById(R.id.titletextView);
        mcontent=(TextView)findViewById(R.id.contenttextView);
        mconnect.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.connectbutton:
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // fetch data
                   new downloadinfo().execute(url);
                } else {
                    Toast.makeText(this.getApplicationContext(),"Network Error!!",Toast.LENGTH_SHORT);
                    break;
                }

        }
    }

    private class downloadinfo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.

            String text;
            String para="";
            try {

                Document doc = Jsoup.connect(urls[0]).get();
                System.out.println("Document" + doc);
                System.out.println("JSOUP" + doc.text());
                 title = doc.title();
                text=doc.text();

                Element link = doc.select("div#content clearfix > p").first();
                Elements descs = doc.getElementsByClass("content clearfix");
                for (Element desc : descs) {
                    Elements paragraphs = desc.getElementsByTag("p");
                    for (Element paragraph : paragraphs) {
                        if(!(paragraph.text().equals("Information Brochure")||paragraph.text().equals("Annual Report"))) {
                            para += paragraph.text();
                            if(!para.equals("")) break;
                        }
                          //  System.out.println("APPU" + paragraph.text());
                    }
                }

                System.out.println("Final"+ para);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
            return para;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
         // Log.d("Result",result);
            mtitle.setText(title);
            mcontent.setText(result);
        }
    }











}
