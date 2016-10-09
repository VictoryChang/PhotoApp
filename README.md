# PhotoApp
Incorporates photos API, Retrofit, gson, Picasso, SQLite DB


Emulator: Nexus 5, API 23, 4.95in.

Tested Physical Phone: Samsung Avant, KitKat 4.4.2/4.5".

Time taken: +/- 15 hours or so.

Notes: 1. On homescreen, [RANDOM] calls the unsplash photos api. Random is automatically called and will update ui pending the api. 2. On homescreen, [SEARCH] is used in correspondance with the search edit text. 3. To save an image, simply click on the imageButton. On homescreen, [View Saved] Takes you to a new activity that displays all saved images in a list. 4. Upon clicking [View Saved], the list is clickable. It will open the image in a browser by means of implicit intent.

There is no saving of search results when the page is changed.
