# Stream Feeds android sample app
![screenshot](./docs/app-screenshot.png)
1. Set your user id, api key, and token in `./app/build.gradle`
```groovy
buildConfigField "String", "USER_ID", "\"stream-eugene\""
buildConfigField "String", "API_KEY", "\"gp6e8sxxzud6\""
buildConfigField "String", "TOKEN", "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoic3RyZWFtLWV1Z2VuZSJ9.uDCSn6PPUL8_tdqEUnAp4nxw8KtRNkKyiSyBAqPfo4c\""
``` 
2. build
```shell script
./gradlew clean assembleDebug
```
3. install app
```shell script
adb install -r ./app/build/outputs/apk/debug/app-debug.apk
```
4. run app
```shell script
adb shell monkey -p io.getstream.feeds.android.sample -c android.intent.category.LAUNCHER 1
```