package com.example.vkinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.vkinfo.utils.NetworkUtils.getImageFromURL;

public class UserActivity extends AppCompatActivity {
    public static final String RESPONSE = "RESPONSE";
    public static final String VKURL = "https://vk.com/";
    public static String id;

    private TextView userName;
    private TextView userStatus;
    private Button openSiteButton;
    private ImageView userPhoto;


    //загрузка фотографии по ссылке из response
    class AsyncUploadImage extends AsyncTask<URL, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(URL... urls) {
            Bitmap bitmap = null;
            try {
                bitmap = getImageFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                userPhoto.setImageBitmap(bitmap);
            } else
            {
                userPhoto.setImageURI(Uri.parse("C:/androidprojects/VKInfo/app/src/main/res/mipmap-hdpi/nophoto_200.png"));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userPhoto = findViewById(R.id.iv_photo);
        userName = findViewById(R.id.tv_result_name);
        userStatus = findViewById(R.id.tv_result_status);
        openSiteButton = findViewById(R.id.btn_open_site);

        String response;
        String nameString = "";
        String statusString = "";
        URL photoURL = null;
        String firstName = null;
        String lastName = null;
        String platformString=null;

        JSONObject lastSeen = null;
        long seenTime = 0;
        int platform  = 0;
        int isOnline= -1;

        Bundle bundle = getIntent().getExtras();
        response = bundle.getString(RESPONSE);


        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray jsonArray = jsonResponse.getJSONArray("response");
            JSONObject userInfo = jsonArray.getJSONObject(0);


            firstName = userInfo.getString("first_name");
            lastName = userInfo.getString("last_name");
            isOnline = userInfo.getInt("online");

            lastSeen = userInfo.getJSONObject("last_seen");
            seenTime = lastSeen.getLong("time");
            platform = lastSeen.getInt("platform");


            id = userInfo.getString("id");
            photoURL = new URL(userInfo.getString("photo_200"));

            nameString += "Имя: " + firstName + "\nФамилия: " + lastName;
            if(isOnline == 1){
                statusString = "В сети";
            }else
            {
                platformString = (platform < 6)?  "мобильного устройства": "браузера";
                String result = new java.text.SimpleDateFormat("HH:mm, dd/MM/yyyy").format(new Date(seenTime * 1000));
                statusString = "Был в сети \n" +  result + "\nc "+ platformString;
            }

        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }



        new UserActivity.AsyncUploadImage().execute(photoURL);
        userName.setText(nameString);
        userStatus.setText(statusString);


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(id);
                Uri userURL=null;
                if(matcher.find()){
                    userURL = Uri.parse(VKURL + "id" + id);
                }else
                {
                    userURL = Uri.parse(VKURL + id);
                }
                Intent openProfile = new Intent(Intent.ACTION_VIEW, userURL);
                if (openProfile.resolveActivity(getPackageManager()) != null) {
                    startActivity(openProfile);
                }
            }
        };
        openSiteButton.setOnClickListener(onClickListener);

    }

}