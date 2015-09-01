package com.music.searchexample;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.music.searchexample.adapter.MusicSearchAdapter;
import com.music.searchexample.app.MusicSearchController;
import com.music.searchexample.model.MusicSearchModel;

public class MusicSearchActivity extends Activity {
	// Log tag
	private static final String TAG = MusicSearchActivity.class.getSimpleName();

	public static final String ARTISTNAME = "artistname";
	public static final String TRACKNAME = "trackname";
	public static final String THUMBNAILURL = "thumbnailurl";

	private static final String MUSIC_SEARCH_URL = "https://itunes.apple.com/search?term=";

	private ProgressDialog pDialog;
	private List<MusicSearchModel> musicList = new ArrayList<MusicSearchModel>();
	private ListView musiclistView;
	private MusicSearchAdapter musicadapter;
	// Search EditText
	private EditText musicSearch;
	private Button searchButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		musiclistView = (ListView) findViewById(R.id.list);
		musicadapter = new MusicSearchAdapter(this, musicList);
		musicSearch = (EditText) findViewById(R.id.inputSearch);
		searchButton = (Button) findViewById(R.id.buttonSearch);
		musiclistView.setAdapter(musicadapter);

		// showProgressDialog();
		setProgressBarIndeterminateVisibility(true);

		// this is default list for music search.Instead of empty list,it shows
		// default list
		musicSearch("tom+waits");

		// refresh the list for new music search
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeyboard(v);
				musicList.clear();
				musicSearch(musicSearch.getText().toString());
			}
		});

		// hides the keyboard
		musicSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					hideKeyboard(v);
				}
			}
		});

		musiclistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent lyricsIntent = new Intent(MusicSearchActivity.this,
						LyricsActivity.class);

				parent.getItemAtPosition(position);
				TextView artistName = (TextView) view
						.findViewById(R.id.artistName);
				TextView trackName = (TextView) view
						.findViewById(R.id.trackName);
				NetworkImageView thumnailview = (NetworkImageView) view
						.findViewById(R.id.lyricsthumbnail);

				lyricsIntent.putExtra(ARTISTNAME, artistName.getText()
						.toString());
				lyricsIntent
						.putExtra(TRACKNAME, trackName.getText().toString());
				startActivity(lyricsIntent);
			}
		});
	}

	/**
	 * Parsing json response and passing the data to music adapter
	 * */
	private void parseJsonFeed(JSONObject response) {
		try {

			JSONArray musicArray = response.getJSONArray("results");

			for (int i = 0; i < musicArray.length(); i++) {
				JSONObject musicChildObj = (JSONObject) musicArray.get(i);

				MusicSearchModel musicItem = new MusicSearchModel();

				musicItem.setTrackName(musicChildObj.getString("trackName"));
				musicItem.setArtistName(musicChildObj.getString("artistName"));

				// Album might be null sometimes
				String album = musicChildObj.isNull("collectionName") ? null
						: musicChildObj.getString("collectionName");
				musicItem.setAlbumName(album);

				// Image might be null sometimes
				String image = musicChildObj.isNull("artworkUrl60") ? null
						: musicChildObj.getString("artworkUrl60");
				musicItem.setThumbnailUrl(image);

				// Image might be null sometimes
				String largeImage = musicChildObj.isNull("artworkUrl100") ? null
						: musicChildObj.getString("artworkUrl100");
				musicItem.setLargeUrl(largeImage);
				musicList.add(musicItem);
			}

			// notify data changes to music adapter

			musicadapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hideProgressDialog();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void showProgressDialog() {
		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();
	}

	private void hideProgressDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

	private void musicSearch(CharSequence cs) {
		// We first check for cached request
		Cache cache = MusicSearchController.getInstance().getRequestQueue()
				.getCache();
		Entry entry = cache.get(MUSIC_SEARCH_URL + cs);

		if (entry != null) {
			// fetch the data from cache
			try {
				String data = new String(entry.data, "UTF-8");
				try {
					parseJsonFeed(new JSONObject(data));
					// hideProgressDialog();
					setProgressBarIndeterminateVisibility(false);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {

			// making fresh volley request and getting json
			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
					MUSIC_SEARCH_URL + cs, null,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {

							VolleyLog.d(TAG, "Response: " + response.toString());
							if (response != null) {
								parseJsonFeed(response);
								setProgressBarIndeterminateVisibility(false);
								// hideProgressDialog();
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							// hideProgressDialog();
							setProgressBarIndeterminateVisibility(false);
							VolleyLog.d(TAG, "Error: " + error.getMessage());
						}

					});

			// Adding request to volley request queue
			MusicSearchController.getInstance().addToRequestQueue(jsonReq);

		}
	}

	public void hideKeyboard(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
