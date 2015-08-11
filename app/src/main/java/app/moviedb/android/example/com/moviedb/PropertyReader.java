package app.moviedb.android.example.com.moviedb;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private static Properties properties = null;

    public static String getProperty(Context context, String propertyName) {
        try {
            if(properties != null){
                return getPropertyFromFileName(propertyName);
            }

            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties = new Properties();
            properties.load(inputStream);

            return getPropertyFromFileName(propertyName);
        } catch (IOException e) {
            Log.e("AssetsPropertyReader", e.toString());
            return "";
        }
    }

    private static String getPropertyFromFileName(String propertyName){
        return properties.getProperty(propertyName);
    }
}
