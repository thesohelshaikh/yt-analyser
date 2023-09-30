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

[<img src="https://user-images.githubusercontent.com/663460/26973090-f8fdc986-4d14-11e7-995a-e7c5e79ed925.png" alt="Get it on GitHub" height="75">](https://github.com/thesohelshaikh/yt-analyser/releases/latest)
<a href='https://play.google.com/store/apps/details?id=com.thesohelshaikh.ytanalyser'><img alt='Get it on Google Play' height="75px" src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>


## Technologies Used

- 100% Kotlin, MVVM
- [Youtube Data API](https://developers.google.com/youtube/v3) - for fetching playlist and video
  data
- [Retrofit](https://square.github.io/retrofit/) - Networking
- [kotlinx.serialization](https://kotlinlang.org/docs/serialization.html) - Data serialization
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - UI
- [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) - Navigation
- [Material 3](https://m3.material.io/) - UI
- [Coil](https://github.com/coil-kt/coil) - Image loading
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - Async
- [Room Database](https://developer.android.com/jetpack/androidx/releases/room) - Offline caching
- [Kotlin Symbol Processing](https://kotlinlang.org/docs/ksp-overview.html) - compiler plugins
- [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html) - build
  configuration

## Screenshots

<img height="400px" src="https://github.com/thesohelshaikh/yt-analyser/assets/26832180/fac6e1b6-92fe-42b7-a17d-a9563b6f5bdc"/>
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

You can also contribute by adding translations using the
Hosted [Weblate](https://hosted.weblate.org/projects/yt-analyser/yt-analyser/), an open source and
web-based translation platform.

## Licence

See [Licence](LICENSE).
