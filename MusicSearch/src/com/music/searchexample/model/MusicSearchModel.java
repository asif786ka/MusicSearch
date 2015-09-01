package com.music.searchexample.model;

public class MusicSearchModel {

	private String trackName, artistName, albumName, thumbnailUrl, largeUrl;

	public MusicSearchModel() {

	}

	public MusicSearchModel(String trackName, String artistName,
			String albumName, String thumbnailUrl, String largeUrl) {
		super();
		this.trackName = trackName;
		this.artistName = artistName;
		this.albumName = albumName;
		this.thumbnailUrl = thumbnailUrl;
		this.largeUrl = largeUrl;

	}

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getLargeUrl() {
		return largeUrl;
	}

	public void setLargeUrl(String largeUrl) {
		this.largeUrl = largeUrl;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

}
