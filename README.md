#Steam Game Picker For Android
Indecisive about what game to play next? Maybe you've bought every Humble Bundle ever made and simply can't decide? Well let Steam Game Picker For Android decide for you!

Simply enter your custom steam community profile ID and follow on the screen instructions to get a random game selected for you!

The latest binary version can be downloaded and installed from the [Google Play Store](https://play.google.com/store/apps/details?id=com.numan1617.steam_game_picker).

## Building and Running
### Steam Community API Key
To build and run the app you will need to obtain a [Steam community API key](http://steamcommunity.com/dev/apikey).

Once you have this key you will need to enter it into res/values/steam_api.xml under the key: `steam_api_key`

### Google Analytics Key
To enable Google Analytics you will need to generate a Google Analytics project key and enter it into res/values/analytics.xml under the key: `ga_trackingId`