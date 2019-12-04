I have implemented this using all the latest technologies(Kotlin and Android Jetpack Components) and have used the following components,

1. MVVM, LiveData and Repository architecture to observe live changes in the data
2. Retrofit, GSON for network call and data parsing
3. ViewModel is also helping in handling configuration changes such as device rotation
4. Caching is implemented using file read/write and Cache Updation is done using WorkManager(from Jetpack)
5. Using Android KTX
6. Since I'm new to UI and Instrumentation Testing, I have written some very basic test cases as of now but I will be learning new testing techniques to build more tests over the next few days.
