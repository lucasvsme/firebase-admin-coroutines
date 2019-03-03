> ## Deprecation notice
> A better implementation has been merged into the official [kotlin/kotlinx.coroutines](https://github.com/kotlin/kotlinx.coroutines) repository.
> 
> Please, use the [official implementation](https://github.com/Kotlin/kotlinx.coroutines/tree/master/integration/kotlinx-coroutines-play-services) instead.

# Firebase Admin Coroutines

Coroutine wrappers for Tasks API and ApiFuture API used in Firebase Admin SDK.

## Example

The following code shows how to get an user from Firebase and delete it using two approaches.

The standard approach is to write nested callback functions.

```kotlin
val auth = FirebaseAuth.getInstance()

auth.getUserByEmail(email)
    .addOnSuccessListener { user ->
        auth.deleteUser(user.uid)
          .addOnSuccessListener { println("User deleted") }
          .addOnFailureListener { exception -> println(exception) }
    }
    .addOnFailureListener { exception -> println(exception) }
```

The other approach is the use of a `suspended coroutine` to make the code flatter, so we can call the `await()` method
to wait the first asynchronous operations (retrieve the user) to complete before starting the second one (delete the user).

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

Assuming you already have the [Firebase Admin SDK](https://github.com/firebase/firebase-admin-java) as a dependency in your project, you just need to copy and paste the functions into your project.

| Firebase SDK version | Function |
| :--- | :--- |
| 5.x.x or earlier | [`Task<T>.await()`](firebase-tasks/src/main/kotlin/kotlinx/coroutines/experimental/firebase/tasks/TaskApi.kt) | 
| 6.x.x or later | [`ApiFuture<T>.await()`](firebase-api-futures/src/main/kotlin/kotlinx/coroutines/experimental/firebase/futures/ApiFuture.kt) |

## About testing

To run the test cases you'll need to put the [private key](https://firebase.google.com/docs/admin/setup#add_firebase_to_your_app) of a Firebase project in the root path. It also should be named `google-services.json`. In case you don't the tests will throw an error due to unauthorized call to the Firebase servers.

If you know how to implement unit tests for the case, please fill an issue and send a pull request.

## Contributing

If you found any bug or want to improve the project, please feel free to fork it, fill issues and send pull requests.
