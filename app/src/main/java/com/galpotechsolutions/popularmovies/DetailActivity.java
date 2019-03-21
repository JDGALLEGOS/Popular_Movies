package com.galpotechsolutions.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movies_activity);

        //Get extras from the Intent
        final String originalTitle = getIntent().getStringExtra("originalTitle");
        final String posterPath = getIntent().getStringExtra("posterPath");
        final String overView = getIntent().getStringExtra("overView");
        final String voteAverage = getIntent().getStringExtra("voteAverage");
        final String releaseDate = getIntent().getStringExtra("releaseDate");

        ArrayList<Detail> details = new ArrayList<>();
        details.add(new Detail(originalTitle, posterPath, overView, voteAverage, releaseDate));

        // Create the detailAdapter
        DetailAdapter itemAdapter = new DetailAdapter(this, details);

        // Find the listView
        final ListView listView = findViewById(R.id.list);
        // Set the Adapter using the adapter
        listView.setAdapter(itemAdapter);

    }
}
