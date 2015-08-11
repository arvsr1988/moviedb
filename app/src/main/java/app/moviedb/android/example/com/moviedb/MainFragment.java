package app.moviedb.android.example.com.moviedb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvind on 11/08/15.
 */
public class MainFragment extends Fragment {

    private GridView gridView;
    private static List<MovieData> movieDataList;

    public MainFragment() {
    }

    public void onStart() {
        super.onStart();
        getMovieData();
    }

    private void getMovieData() {
        MovieDataTask movieDataTask = new MovieDataTask();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        String sortOrder = sharedPreferences.getString(getString(R.string.pref_sort_key), "popularity.desc");
        movieDataTask.execute(sortOrder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        List<MovieData> emptyList = new ArrayList<MovieData>();
        gridView.setAdapter(new ImageAdapter(getActivity().getBaseContext(), emptyList));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Context context = view.getContext();
                MovieData movieData = movieDataList.get(position);
                Intent detailActivityIntent = new Intent(context, DetailActivity.class);
                detailActivityIntent.putExtra("movieData", movieData);
                startActivity(detailActivityIntent);
            }
        });
        return rootView;
    }

    private class MovieDataTask extends AsyncTask<String, Void, List<MovieData>> {

        private static final String RESULTS = "results";

        @Override
        protected List<MovieData> doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            List<MovieData> movieDataList = new ArrayList<MovieData>();
            String movieGridJSON = null;

            String API_KEY = PropertyReader.getProperty(getActivity().getBaseContext(), "apikey");
            String SORT_BY = params[0];

            try {
                Uri.Builder uriBuilder = new Uri.Builder();
                uriBuilder.scheme("http");
                uriBuilder.authority("api.themoviedb.org");
                uriBuilder.appendPath("3");
                uriBuilder.appendPath("discover");
                uriBuilder.appendPath("movie");
                uriBuilder.appendQueryParameter("api_key", API_KEY);
                uriBuilder.appendQueryParameter("sort_by", SORT_BY);

                URL url = new URL(uriBuilder.build().toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(100000);
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                movieGridJSON = buffer.toString();
                JSONObject movieListJSONObject = new JSONObject(movieGridJSON);
                JSONArray movieJSONArray = movieListJSONObject.getJSONArray(RESULTS);
                for (int i = 0; i < movieJSONArray.length(); i++) {
                    JSONObject movieJSONObject = movieJSONArray.getJSONObject(i);
                    String movieTitle = movieJSONObject.getString("title");
                    String imageUrl = movieJSONObject.getString("poster_path");
                    String releaseDate = movieJSONObject.getString("release_date");
                    String description = movieJSONObject.getString("overview");
                    String userRating = movieJSONObject.getString("vote_average");
                    movieDataList.add(new MovieData(movieTitle, imageUrl, description, userRating, releaseDate));
                }
                return movieDataList;
            } catch (Exception e) {
                Log.e("MOVIE GRID API", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return new ArrayList<MovieData>();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("ForecastFragment", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(List<MovieData> movieDataList) {
            MainFragment.movieDataList = movieDataList;
            ImageAdapter imageAdapter = new ImageAdapter(getActivity().getBaseContext(), movieDataList);
            gridView.setAdapter(imageAdapter);
        }
    }
}
