# Keystore Ultimate
<h1 align=center>
<img src="Logo/horizontal.png" width=55%>
</h1>

The ultimate solution for keystore manipulation in Android

Tired of deal with Android keystore? A lot of code ever? This library is for you

This library is tiny (no dependencies) just to small implementations to store/retrieve keystore values
it automatically deals with old Android keystore implementation (pre api 23) and newer Android keystore

Usage:
---
```java
//Just get a instance of the CipherStorage
CipherStorage cipherStorage = CipherStorageFactory.newInstance(context);
//Then you can use the encrypt and decrypt method
```

Gradle dependency:
---
```groovy
implementation 'com.github.leonardoxh:keystore-ultimate:1.2.1'
```

...Unit tests?
---
* I care a lot about unit tests, and since this library touch on the Android Keystore
making it almost impossible to test, but, I said "almost" that's why I provide another library
just for tests, if you wanna use mockito `when...then` it's fine but I think this is not
your responsability so just add this into your test implementation and that's it, the
`CipherStorageFactory` will return a in memory implementation just for tests you don't need change
anything on your source code:
```groovy
testImplementation 'com.github.leonardoxh:keystore-ultimate-test:1.2.1'
```

Library usage?
---
* Did you use this library? Please notify me via email leonardoxh@gmail.com I would love to receive feedbacks and see if this library is used in some project and if it's helping somehow.

Inspiration:
---
* [React native keychain](https://github.com/oblador/react-native-keychain)

Limitations:
---
* On Api level 22 and bellow your alias for key should not contain more than 256 bytes otherwise we can't save
the the value due a limitation on the Android Keystore

Special thanks:
---
- [Zularizal](https://github.com/zularizal) for this amazing library logo and sample icons

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
