<p align="center">
    <img src="playstore/banner.png" height="250px" alt="banner"/>
</p>


# yt-analyser

Shows youtube playlist durations in one click

## Features

- Share a URL directly or paste Youtube URL video
- Offline caching
- Dark mode support

## Release

Download latest APK from [here](https://github.com/thesohelshaikh/yt-analyser/releases).

<a href='https://play.google.com/store/apps/details?id=com.thesohelshaikh.ytanalyser&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' height="75px" src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>

## Technologies Used

- 100% Kotlin, MVVM
- Youtube Data API - for fetching playlist and video data
- Retrofit - Networking
- kotlinx.serialization - Data serialization
- Jetpack Compose - UI
- Compose Navigation - Navigation
- Material 3 - UI
- Coil - Image loading
- Coroutines - Async
- Room Database - Offline caching

## Screenshots

<img height="400px" src="https://github.com/thesohelshaikh/yt-analyser/assets/26832180/e9c64a0a-8097-4b85-9bec-f09c43c83f59"/>
<img height="400px" src="https://github.com/thesohelshaikh/yt-analyser/assets/26832180/59f243a6-928b-461f-aafe-0a6e68c7e863" />
<img height="400px" src="https://github.com/thesohelshaikh/yt-analyser/assets/26832180/c5b59084-5eac-412d-b2ea-21b82f3aea70" />
<img height="400px" src="https://github.com/thesohelshaikh/yt-analyser/assets/26832180/cb9cfa8e-039a-4d3a-94c1-0b3ed35d2489" />

## Setup

1. Clone the repo.
2. Get [Youtube Data API](https://developers.google.com/youtube/v3/getting-started).
3. Place the API key in `local.properties` file by adding a
   property `YOUTUBE_API_KEY="YOUR_API_KEY"`
4. Run the project.

## Contributing

All PRs, suggestions, feedback is welcome.

## Licence

See [Licence](LICENSE).
