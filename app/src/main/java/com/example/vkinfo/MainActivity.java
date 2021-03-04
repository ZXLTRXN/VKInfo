package com.example.vkinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vkinfo.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.vkinfo.utils.NetworkUtils.generateURL;
import static com.example.vkinfo.utils.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {

    private EditText searchField;
    private Button searchClear;
    private Button searchButton;
    private TextView errorMessage;
    private ProgressBar loadingIndicator;


    private void showErrorText() {
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void hideErrorText() {
        errorMessage.setVisibility(View.INVISIBLE);
    }

    //запрос к серверу ВК
    class VKQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {//делаем индикатор прогресса видимым, после выполнения запроса скроем его
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(URL... urls)// массив класса URL
        {
            String response = null;
            try {
                response = (String)getResponseFromURL(urls[0], NetworkUtils.ResponseType.String);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {

            JSONObject jsonResponse = null;
            try {
                jsonResponse = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (response != null && response != "" && !jsonResponse.has("error")) {
                errorMessage.setVisibility(View.INVISIBLE);
                Intent userData = new Intent(MainActivity.this, UserActivity.class);
                userData.putExtra(UserActivity.RESPONSE, response);
                startActivity(userData);
            } else {
                showErrorText();
            }

            loadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchField = findViewById(R.id.et_search_field);
        searchClear = findViewById(R.id.et_search_clear);
        searchButton = findViewById(R.id.b_exe);
        errorMessage = findViewById(R.id.tv_error_message);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!searchField.getText().toString().contains(" ")) {
                    hideErrorText();
                    String[] values = {NetworkUtils.VALUE_ONLINE,NetworkUtils.VALUE_LASTSEEN,NetworkUtils.VALUE_PHOTO};
                    URL generatedURL = generateURL(searchField.getText().toString(),values);
                    new VKQueryTask().execute(generatedURL);
                }else showErrorText();
            }
        };

        View.OnClickListener onClearListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchField.setText("");
            }
        };

        searchButton.setOnClickListener(onClickListener);
        searchClear.setOnClickListener(onClearListener);

    }
}