package com.music.searchexample;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.music.searchexample.app.MusicSearchController;

public class LyricsActivity extends Activity {
	// Log tag
	private static final String TAG = LyricsActivity.class.getSimpleName();

	private static final String LYRICS_DETAIL_BASE_URL = "http://lyrics.wikia.com/api.php?func=getSong&artist=";
	private String lyricsURL = "";

	private ProgressDialog pDialog;
	private NetworkImageView thumbNail;
	ImageLoader imageLoader = MusicSearchController.getInstance()
			.getImageLoader();
	private TextView lyricsDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lyrics_detail);

		if (imageLoader == null)
			imageLoader = MusicSearchController.getInstance().getImageLoader();
		thumbNail = (NetworkImageView) findViewById(R.id.thumbnail);
		TextView trackName = (TextView) findViewById(R.id.trackName);
		TextView artistName = (TextView) findViewById(R.id.artistName);
		lyricsDetail = (TextView) findViewById(R.id.lyricsDetail);

		// showProgressDialog();
		setProgressBarIndeterminateVisibility(true);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String artistname = extras
					.getString(MusicSearchActivity.ARTISTNAME);
			String trackname = extras.getString(MusicSearchActivity.TRACKNAME);
			// String thumbNailURL =
			// extras.getString(MusicSearchActivity.THUMBNAILURL);

			// thumbnail image
			// thumbNail.setImageUrl(thumbNailURL, imageLoader);

			// trackname
			trackName.setText(trackname);
			// artistname
			artistName.setText(artistname);
			lyricsURL = LYRICS_DETAIL_BASE_URL + artistname + "&song="
					+ trackname + "&fmt=json";
			Log.d(TAG, lyricsURL);
		}

		// this is default list for music search.Instead of empty list,it shows
		// default list
		getLyricDetails();

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

	
	// Lyrics response in this method can be handled in a better way.If the response was jsonarray or jsonobject,
	// it can be parsed quickly.
	private void getLyricDetails() {
		// We first check for cached request
		Cache cache = MusicSearchController.getInstance().getRequestQueue()
				.getCache();
		Entry entry = cache.get(lyricsURL);

		if (entry != null) {
			// fetch the data from cache
			try {
				String data = new String(entry.data, "UTF-8");
				// Display the first 500 characters of the response string.
				lyricsDetail.setText("Response is: " + data);
				// hideProgressDialog();
				setProgressBarIndeterminateVisibility(false);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {

			// making fresh volley request and getting json
			// Request a string response from the provided URL.
			StringRequest stringRequest = new StringRequest(Request.Method.GET,
					lyricsURL, new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							// Display the first 500 characters of the response
							// string.
							Log.d(TAG, response);
							lyricsDetail.setText("Response is: " + response);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							lyricsDetail
									.setText("Could not get lyrics details");
						}
					});

			// Adding request to volley request queue
			MusicSearchController.getInstance()
					.addToRequestQueue(stringRequest);

		}
	}

	public void hideKeyboard(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
