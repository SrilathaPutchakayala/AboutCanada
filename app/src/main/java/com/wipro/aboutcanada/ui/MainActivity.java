package com.wipro.aboutcanada.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.wipro.aboutcanada.R;
import com.wipro.aboutcanada.model.AboutCanada;
import com.wipro.aboutcanada.model.AboutCanadaAdapter;
import com.wipro.aboutcanada.model.ServiceCallSource;
import com.wipro.aboutcanada.utils.NetworkConnectivityHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tv_refresh;
    private AboutCanadaAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tv_refresh = (TextView) findViewById(R.id.tv_refresh);

        new LoadWebServiceListAsyncTask().execute();

        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadWebServiceListAsyncTask().execute();
            }
        });
    }

    /**
     * LoadWebServiceListAsyncTask async task to call the web service and fetches the JSON response
     */
    private class LoadWebServiceListAsyncTask extends AsyncTask<String, Void, String> {

        ServiceCallSource serviceCallObj;
        ProgressDialog pd;
        private List<AboutCanada> rowsList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = null;
            try {
                serviceCallObj = new ServiceCallSource();
                String url = getResources().getString(R.string.service_url);
                if(NetworkConnectivityHelper.isOnline(MainActivity.this)) {
                    response = serviceCallObj.getResponseFromServiceURL(url);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if(pd.isShowing()) {
                    pd.dismiss();
                    if (result != null) {
                        JSONObject resultJsonObject = new JSONObject(result);
                        JSONArray rowsJsonArray = resultJsonObject.getJSONArray("rows");

                        for(int i=0; i<rowsJsonArray.length(); i++) {
                            JSONObject eachRowObj = rowsJsonArray.getJSONObject(i);
                            AboutCanada aboutCanadaObj = new AboutCanada();
                            aboutCanadaObj.setTitle(eachRowObj.getString("title"));
                            aboutCanadaObj.setDescription(eachRowObj.getString("description"));
                            aboutCanadaObj.setImage(eachRowObj.getString("imageHref"));
                            rowsList.add(aboutCanadaObj);
                            aboutCanadaObj=null;
                        }

                        mAdapter = new AboutCanadaAdapter(rowsList,MainActivity.this);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);

                    }else {
                        Toast.makeText(MainActivity.this, R.string.error_no_connection, Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException jex) {
                jex.printStackTrace();
            }
        }
    }
}
