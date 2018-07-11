package kotlinx.coroutines.experimental.firebase.futures

import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import kotlinx.coroutines.experimental.suspendCancellableCoroutine

suspend fun <T> ApiFuture<T>.await(): T = suspendCancellableCoroutine { continuation ->
    ApiFutures.addCallback(this@await, object : ApiFutureCallback<T> {
        override fun onSuccess(result: T) = continuation.resume(result)
        override fun onFailure(throwable: Throwable) = continuation.resumeWithException(throwable)
    })

    continuation.invokeOnCancellation {
        this@await.cancel(true)
    }
}
