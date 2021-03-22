package com.example.vkinfo.utils;

import android.content.Context;

import com.example.vkinfo.R;
import com.example.vkinfo.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class StorageUtils {

    private Context context;
    private JSONArray userArr;

    public StorageUtils(Context context) {
        this.context = context;

        String dataFromFile;
        try{
            dataFromFile = readFile();
            if (dataFromFile.length() == 0)
            {
                userArr = new JSONArray();
            } else
            {
                userArr = new JSONArray(dataFromFile);
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private String readFile() throws Exception
    {
        String fileName = context.getResources().getString(R.string.File_with_users);
        File internalStorageDir = context.getFilesDir();
        File userStorage = new File(internalStorageDir, fileName);

        if(!userStorage.isFile()) {
            userStorage.createNewFile();
        }

        FileInputStream fis = new FileInputStream(userStorage);
        InputStreamReader isr  = new InputStreamReader(fis, "utf-8");
        BufferedReader br =new BufferedReader(isr);

        String data = "";
        data += br.readLine();

        br.close();
        isr.close();
        fis.close();
        return data;
    }

    private int writeFile() throws Exception
    {
        String fileName = context.getResources().getString(R.string.File_with_users);
        File internalStorageDir = context.getFilesDir();
        File userStorage = new File(internalStorageDir, fileName);

        FileOutputStream fos = new FileOutputStream(userStorage);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
        osw.write(userArr.toString());

        osw.close();
        fos.close();
        return 0;
    }

    public List<String> getUsersList()
    {
        List<String> userNames = new ArrayList<>();
        try
        {
            for (int j=0; j<userArr.length();j++)
            {
                JSONObject obj = userArr.getJSONObject(j);
                userNames.add(obj.getString("firstName") + ' ' + obj.getString("lastName"));
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return userNames;
    }

    public String getUserIdByIndex(int index)
    {
        String dataFromFile;
        String id=null;

        try
        {
//            dataFromFile = readFile();
//            JSONArray userArr = new JSONArray(dataFromFile);
            JSONObject obj = userArr.getJSONObject(index);
            id = obj.getString("id");

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return id;
    }

    public int saveUser(User user)
    {
        try {
            JSONObject userObject = new JSONObject(user.toString());

            boolean userExists = false;
            for (int j = 0; j < userArr.length(); j++)
            {
                JSONObject obj = userArr.getJSONObject(j);
                if (obj.getString("id").equals(user.getId()))//////////////////////////////////////////смена имени?!
                {
                    userExists = true;
                    break;
                }
            }
            if (!userExists) {
                userArr.put(userObject);
                writeFile();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    public int deleteUserByIndex(int index)
    {
        String id=null;
        try
        {
            Object res = userArr.remove(index);

            if(res == null) {
                return 1;
            }

            writeFile();
        }catch(Exception e)
        {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }
}
