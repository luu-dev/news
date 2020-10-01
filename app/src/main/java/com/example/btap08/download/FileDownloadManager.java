package com.example.btap08.download;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloadManager {
    public static String download(String link,String tt) {
        try {

            String path = Environment.getExternalStorageDirectory().getPath();
            path += "/BTAP08/" + tt + ".html";
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            byte[] b = new byte[1024];
            int count = inputStream.read(b);
            File f = new File(path);
            f.getParentFile().mkdirs();
            f.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            while (count > 0) {
                fileOutputStream.write(b, 0, count);
                count = inputStream.read(b);
            }
            inputStream.close();
            fileOutputStream.close();
            return path;
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}