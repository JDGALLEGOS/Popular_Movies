package com.galpotechsolutions.popularmovies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class myCustomAsyncTask extends AsyncTask<String, Void, Bitmap> {

    ImageView imageView;

    myCustomAsyncTask(ImageView imageView) {

        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urlOfImage = urls[0];
        Bitmap logo = null;
        try {
            InputStream is = new URL(urlOfImage).openStream();
            logo = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            // Catch the download exception
            Log.v("download", e.getMessage());

        }
        return logo;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}
