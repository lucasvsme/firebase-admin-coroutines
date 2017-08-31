# kotlinx-coroutines-firebase

Coroutine wrapper for Firebase [Task](https://firebase.google.com/docs/reference/admin/java/reference/com/google/firebase/tasks/Task) class.
This module implements a method called [`await()`](src/main/kotlin/kotlinx/coroutines/experimental/firebase/TaskAwait.kt) to handle the `OnCompleteListener` callback.

_P.S: The implementation is based on the Firebase Admin SDK._

## Example

The following code shows how to get an user from Firebase and delete it using two approaches.

The standard approach is to write nested callback functions.

```kotlin
val auth = FirebaseAuth.getInstance()

auth.getUserByEmail(email)
    .addOnSuccessListener { user ->
        auth.deleteUser(user.uid)
            .addOnSuccesListener { println("User deleted") }
            .addOnFailureListener { exception -> println(exception) }
    }
    .addOnFailureListener { exception -> println(exception) }
```

The other approach is the use of a `suspended coroutine` to make the code flatter, so we can call the `await()` method
to wait the first asyncronous operations (retrieve the user) to complete before starting the second one (delete the user).

```kotlin
val auth = FirebaseAuth.getInstance()

try {
  val user = auth.getUserByEmail(email).await()

  auth.deleteUser(user.uid).await()

  println("User deleted")
} catch (exception: FirebaseAuthException) {
  println(exception)
}
```

## How to use

Assuming you already have the [Firebase Admin SDK](https://github.com/firebase/firebase-admin-java) as a dependency in your project, you just need to copy and paste [this](src/main/kotlin/kotlinx/coroutines/experimental/firebase/TaskAwait.kt) function in it.

## About testing

To run the test cases you'll need to put the [private key](https://firebase.google.com/docs/admin/setup#add_firebase_to_your_app) of a Firebase project in the root path. It also should be named `serviceAccount.json`. In case you don't the tests will throw an error due to unauthorized call to the Firebase servers.

If you know how to implement unit tests for the case, please fill an issue and send a pull request.

## Contributing

If you want to make it better, feel free to fork it, fill issues and send pull requests.
