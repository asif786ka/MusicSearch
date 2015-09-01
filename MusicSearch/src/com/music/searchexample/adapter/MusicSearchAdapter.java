package com.music.searchexample.adapter;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.music.searchexample.LyricsActivity;
import com.music.searchexample.R;
import com.music.searchexample.app.MusicSearchController;
import com.music.searchexample.model.MusicSearchModel;

public class MusicSearchAdapter extends BaseAdapter {

	public static final String ARTISTNAME = "artistname";
	public static final String TRACKNAME = "trackname";
	public static final String THUMBNAILURL = "thumbnailurl";
	private NetworkImageView thumbNail;
	private MusicSearchModel mMusic;
	private Activity activity;
	private LayoutInflater inflater;
	private List<MusicSearchModel> musicItems;
	ImageLoader imageLoader = MusicSearchController.getInstance()
			.getImageLoader();

	public MusicSearchAdapter(Activity activity,
			List<MusicSearchModel> musicItems) {
		this.activity = activity;
		this.musicItems = musicItems;

	}

	@Override
	public int getCount() {
		return musicItems.size();
	}

	@Override
	public Object getItem(int location) {
		return musicItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		if (imageLoader == null)
			imageLoader = MusicSearchController.getInstance().getImageLoader();
		thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
		TextView trackName = (TextView) convertView
				.findViewById(R.id.trackName);
		TextView artistName = (TextView) convertView
				.findViewById(R.id.artistName);
		TextView albumName = (TextView) convertView
				.findViewById(R.id.albumName);

		// getting music search data for the row
		mMusic = musicItems.get(position);

		// thumbnail image
		thumbNail.setImageUrl(mMusic.getThumbnailUrl(), imageLoader);

		// trackname
		trackName.setText(mMusic.getTrackName());
		// artistname
		artistName.setText(mMusic.getArtistName());
		// albumname
		albumName.setText(mMusic.getAlbumName());

		thumbNail.setTag(new Integer(position));

		/*
		 * trackName.setTag(new Integer(position)); artistName.setTag(new
		 * Integer(position)); thumbNail.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(final View v) {
		 * activity.setProgressBarIndeterminateVisibility(true); LayoutInflater
		 * inflater = activity.getLayoutInflater(); View view =
		 * inflater.inflate(R.layout.expandable_image, null); NetworkImageView
		 * imageView = (NetworkImageView) view .findViewById(R.id.imageView);
		 * 
		 * imageView.setImageUrl( musicItems
		 * .get(Integer.parseInt(v.getTag().toString())) .getLargeUrl(),
		 * imageLoader);
		 * 
		 * AlertDialog.Builder adb = new AlertDialog.Builder(activity);
		 * adb.setView(view); adb.show();
		 * activity.setProgressBarIndeterminateVisibility(false);
		 * 
		 * //Object implementing parcelable can be used if more parameters are
		 * required. Intent lyricsIntent = new Intent(activity,
		 * LyricsActivity.class); lyricsIntent.putExtra(ARTISTNAME,
		 * mMusic.getArtistName()); lyricsIntent.putExtra(TRACKNAME,
		 * mMusic.getTrackName()); lyricsIntent.putExtra(THUMBNAILURL,
		 * mMusic.getThumbnailUrl()); activity.startActivity(lyricsIntent);
		 * 
		 * } });
		 */

		return convertView;
	}

}