package kotlinx.coroutines.experimental.firebase.futures

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import kotlinx.coroutines.experimental.suspendCancellableCoroutine

/**
 * Coroutine support to Firebase callbacks
 *
 * This extension function allows you to interact with a Firebase [ApiFuture]
 * using the `await()` method instead of the standard listeners.
 *
 * There is a sample code below comparing the two approaches. Assuming that
 * `auth` variable has the value returned from `FirebaseAuth.getInstance()`
 * method call then your code can be something like:
 *
 * ```
 * ApiFutures.addCallback(auth.getUserByEmailAsync(email), object : ApiFutureCallback<UserRecord> {
*    override fun onSuccess(user: UserRecord) = println(user)
*    override fun onFailure(throwable: Throwable) = println(throwable)
*  })
 * ```
 *
 * When using the coroutine approach, it should be more like this:
 *
 * ```
 * try {
 *   val user = auth.getUserByEmailAsync(email).await()
 *   println(user)
 * } catch (exception: Exception) {
 *   println(exception)
 * }
 * ```
 */
suspend fun <T> ApiFuture<T>.await(): T = suspendCancellableCoroutine { continuation ->
    ApiFutures.addCallback(this@await, object : ApiFutureCallback<T> {
        override fun onSuccess(result: T) = continuation.resume(result)
        override fun onFailure(throwable: Throwable) = continuation.resumeWithException(throwable)
    })

    continuation.invokeOnCancellation {
        this@await.cancel(true)
    }
}
