# keystore-ultimate
The ultimate solution for keystore manipulation in Android

Tired of deal with Android keystore? A lot of code ever? This library is for you

This library is tiny (no dependencies) just to small implementations to store/retrieve keystore values
it automatically deals with old Android keystore implementation (pre api 23) and newer Android keystore

Usage:
---
```java
//Just get a instance of the CipherStorage and thats it
CipherStorage cipherStorage = CipherStorageFactory.newInstance(context);
//Then you can use the encrypt and decrypt method
```

Gradle dependency:
---
```groovy
implementation 'com.github.leonardoxh:keystore-ultimate:1.0.1'
```

Inspiration:
---
* [React native keychain](https://github.com/oblador/react-native-keychain)

LICENSE:
---
```
Copyright 2018 Leonardo Rossetto

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
