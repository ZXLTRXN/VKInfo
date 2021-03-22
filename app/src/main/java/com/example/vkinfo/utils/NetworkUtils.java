package com.example.vkinfo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetworkUtils {
    public static final String VK_URL = "https://vk.com/";

    private static final String VK_API_BASE_URL = "https://api.vk.com";
    private static final String VK_USER_GET = "/method/users.get";
    private static final String PARAM_USER_ID = "user_ids";
    private static final String LANG = "lang";
    private static final String PARAM_FIELDS = "fields";
    private static final String PARAM_VERSION = "v";
    private static final String PARAM_TOKEN = "access_token";

    public static final String VALUE_ONLINE="online";
    public static final String VALUE_LASTSEEN="last_seen";
    public static final String VALUE_PHOTO="photo_200";

    public enum ResponseType{
        String,
        Bitmap
    }
// строит
    public static URL generateURL(String userIds,String[] values) {
        String valueString = TextUtils.join(",",values);
        Uri builtUri = Uri.parse(VK_API_BASE_URL + VK_USER_GET)
                .buildUpon()//строить на основании того что перед
                .appendQueryParameter(PARAM_USER_ID, userIds)
                .appendQueryParameter(PARAM_FIELDS,  valueString) //"online"+ ","+ "last_seen" + "," + "photo_200"
                .appendQueryParameter(LANG, "ru")
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

    //получает ответ от сервера и возвращает обьект для downcasting'a в String или Bitmap
    public static Object getResponseFromURL(URL url, ResponseType respType) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        Object result;
        try {
            InputStream in = urlConnection.getInputStream();
            switch(respType){
                case String:
                    result = readString(in);
                    break;
                case Bitmap:
                    result = readBitmap(in);
                    break;
                default:
                    throw new Exception("unexpected ResponseType");
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally
        {
            urlConnection.disconnect();
        }
        return result;
    }

    //считывает поток от сервера и возвращает строку
    private static String readString(InputStream in) {
        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");//\\A в рег выр означает начало строки, таким образом мы считаем построчно, по дефолту разделитель - пробел
        boolean hasInput = scanner.hasNext();
        if (hasInput) {
            return scanner.next();
        } else {
            return null;
        }
    }
    //считывает поток от сервера и возвращает Bitmap
    private static Bitmap readBitmap(InputStream in) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(in);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;

    }


}
