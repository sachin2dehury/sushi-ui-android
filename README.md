# Sushi Android UI Kit


[![Actions Status](https://github.com/Zomato/Sushi-Android/workflows/Android%20CI/badge.svg)](https://github.com/Zomato/Sushi-Android/actions)

## Usage

### Installation
This package is available via Github Package Registry. To use this, follow these steps.

Add the Github Maven repository and the dependency in your app's build.gradle

```groovy
repositories {
    // ... google(), jcenter() etc
    maven {
        url "https://maven.pkg.github.com/Zomato/Sushi-Android"
        credentials(HttpHeaderCredentials) {
            name = "Authorization"
            value = "token ${System.getenv("GITHUB_TOKEN")}"
        }
        authentication { header(HttpHeaderAuthentication) }
    }
}

dependencies {
    // ... other dependencies
    implementation "sushi-android:sushilib:0.9.3"
}

```

> NOTE: Make sure you have the `GITHUB_TOKEN` environment variable set. This token should have `read:packages` enabled.


## Documentation
A delicious UI Kit to build Android apps. Made with ❤ by Zomato <br />
<https://zomato.github.io/Sushi-Android/>

## Testing & Coverage

Run all tests and get coverage report
```shell 
./gradlew jacocoTestReport
```

## Publishing

To publish this package run 

```bash
./gradlew assemble publish
```

> For publish to work you need to have `GITHUB_TOKEN` env variable with `write:packages` scope.

