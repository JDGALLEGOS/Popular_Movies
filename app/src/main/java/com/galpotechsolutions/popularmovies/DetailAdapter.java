package com.galpotechsolutions.popularmovies;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class DetailAdapter extends ArrayAdapter<Detail> {

    public DetailAdapter(Activity context, ArrayList<Detail> details) {
        super(context, 0, details);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();

        //Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_items,parent, false);

           //Find the TextView in the list_item.xml layout with the ID overview_text_view
            viewHolder.overViewTextView = listItemView.findViewById(R.id.overview_text_view);

            //Find the TextView in the list_item.xml layout with the ID title_text_view
            viewHolder.originTitleTextView = listItemView.findViewById(R.id.title_text_view);

            //Find the TextView in the list_item.xml layout with the ID vote_average_text_view
            viewHolder.voteAverageDouble = listItemView.findViewById(R.id.vote_average_text_view);

            //Find the TextView in the list_item.xml layout with the ID release_date_text_view
            viewHolder.releaseDateTextView = listItemView.findViewById(R.id.release_date_text_view);

            //Find the ImageView in the list_items.xml layout with the ID poster_image_view
            viewHolder.imageView = listItemView.findViewById(R.id.poster_image_view);

            //Set the tag of the ViewHolder
            listItemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) listItemView.getTag();
        }

        // Get the {@link Detail} object located at this position in the list
        Detail currentDetail =getItem(position);

        //Get the title of the movie from the current new object and set this text to the Movie title
        viewHolder.originTitleTextView.setText(currentDetail.getOriginalTitle());

        //Get the release date of the movie from the current new object and set this text to the Movie release date
        viewHolder.releaseDateTextView.setText(currentDetail.getReleaseDate());

        //Get the vote average of the movie from the current new object and set this text to the Movie vote average
        viewHolder.voteAverageDouble.setText(currentDetail.getVoteAverage());

        //Get the vote average of the movie from the current new object and set this text to the Movie overview
        viewHolder.overViewTextView.setText(currentDetail.getOverview());

        // Get the image of the new from the current new object and set this imageResource to the New image
        String imageUrl = currentDetail.getImageResourceIds();

        // Check if an image is provided for this new story or not
        if (currentDetail.hasImage()){
            // Display the image of the current new story in that ImageView
            //new DetailAdapter.DownloadImageTask(viewHolder.imageView).execute(imageUrl);
            new myCustomAsyncTask(viewHolder.imageView).execute(imageUrl);
        }

        return listItemView;
    }

    static class ViewHolder{
        TextView originTitleTextView;
        //TextView posterPathTextView;
        TextView overViewTextView;
        TextView voteAverageDouble;
        TextView releaseDateTextView;
        ImageView imageView;

        /**@BindView(R.id.overview_text_view) TextView originTitleTextView;
        //TextView posterPathTextView;
        @BindView(R.id.title_text_view) TextView overViewTextView;
        @BindView(R.id.vote_average_text_view) TextView voteAverageDouble;
        @BindView(R.id.release_date_text_view) TextView releaseDateTextView;
        @BindView(R.id.poster_image_view) ImageView imageView;*/

        /**public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }*/
    }

    // Inner Class for downloading images
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        DownloadImageTask(ImageView imageView) {

            this.imageView = imageView;
        }

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

}
