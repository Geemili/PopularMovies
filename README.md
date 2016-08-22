# Popular Movies App

This application downloads a list of popular movies from [The Movie DB][tmdb_org]. Tapping on a movies icon will show more information about it.

## Building

In order to build this application you must add a `secrets.properties` file to the top level directory. The file should contain your API key for [The Movie DB][tmdb_org].

```
TMDB_KEY=<YOUR API KEY>
```

## Libraries

This project use two libraries; [themoviedbapi][themoviedbapi] and [Picasso][picasso].

[themoviedbapi][themoviedbapi] provided a nice high-level api for The Movie DB.

[Picasso][picasso] saved me a lot of time by making image downloading and caching easy.


[tmdb_org]: https://www.themoviedb.org/
[themoviedbapi]: https://github.com/holgerbrandl/themoviedbapi
[picasso]: http://square.github.io/picasso/
