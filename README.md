# MusicSearch
This repo shows music search results in listview.

MusicSearchActivity is reponsible for showing results from the music search api in listview.
Lyrics activity shows the lyrics when an item is clicked in the list in MusicSearchActivity.
Handling case of empty search results will be added.

For cases involving image distortion,custom class is added which will be used in future commits.

It uses Volley as a networking library to parse json data and show in listview. The approach first attempts to fetch data from cache,if present data is loaded from cache.If data is not present in cache,JSONObject request is made. Volley populates the parsed response on the UI thread.

Example works for oreintation changes and shows how Volley is an efficient library compared to async tasks and its limitations during orientation changes. NetworkImageView from volley instead of the normal imageview is used for loading the images.
