## Play WvW Data

A webservice that proxies and aggregates data retrieved by the Guild Wars 2 WvW API

## Requirements

* Scala 2.11.6+
* sbt   0.13.8+
* JDK   1.8.0+
 
## Running the app

    git clone https://github.com/felixbr/play-wvw-data.git
    cd play-wvw-data
    sbt run
    
This can take several minutes, so go grab a coffee. :)

When it's finished, you can explore the api by opening [http://localhost:9000](http://localhost:9000).

## Running the test suite

    sbt test

### External Api Docs

* [Guild Wars 2 API](https://forum-en.guildwars2.com/forum/community/api/API-Documentation)
