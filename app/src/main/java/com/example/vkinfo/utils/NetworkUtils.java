package com.example.vkinfo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetworkUtils {

    private static final String VK_API_BASE_URL = "https://api.vk.com";
    private static final String VK_USER_GET = "/method/users.get";
    private static final String PARAM_USER_ID = "user_ids";
    private static final String PARAM_FIELDS = "fields";
    private static final String PARAM_VERSION = "v";
    private static final String PARAM_TOKEN = "access_token";

    public static URL generateURL(String userIds) {
        Uri builtUri = Uri.parse(VK_API_BASE_URL + VK_USER_GET)
                .buildUpon()//строить на основании того что перед
                .appendQueryParameter(PARAM_USER_ID, userIds)
                .appendQueryParameter(PARAM_FIELDS, "online"+ ","+ "last_seen" + "," + "photo_200" )
                .appendQueryParameter(PARAM_VERSION, "5.89")
                .appendQueryParameter(PARAM_TOKEN, "c6744a9dc6744a9dc6744a9da6c60797b0cc674c6744a9d992f0561022e169bccacf630")
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");//\\A в рег выр означает начало строки, таким образом мы считаем построчно, по дефолту разделитель - пробел
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (UnknownHostException e) {
            return null;
        } finally // блок выполняемый в любом случае
        {
            urlConnection.disconnect();
        }
    }

    public static Bitmap getImageFromURL(URL url) throws IOException {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);
            in.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } finally // блок выполняемый в любом случае
        {
            urlConnection.disconnect();
        }
        return bitmap;
    }


}
