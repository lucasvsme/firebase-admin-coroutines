package kotlinx.coroutines.experimental.firebase.futures

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.FileInputStream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiFutureTest {

    @BeforeAll
    fun setUpFirebase() {
        FileInputStream("serviceAccount.json").use {
            FirebaseApp.initializeApp(FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(it))
                    .build())
        }
    }

    @Test
    fun `An ApiFuture can return a result`() = runBlocking<Unit> {
        val auth = FirebaseAuth.getInstance()

        val (name, email) = "John" to "example@email.com"
        auth.createUserAsync(UserRecord.CreateRequest()
                .setDisplayName(name)
                .setEmail(email)).await()

        val user = auth.getUserByEmailAsync(email).await()

        Assertions.assertEquals(user.displayName, name)
        Assertions.assertEquals(user.email, email)

        auth.deleteUserAsync(user.uid).await()
    }

    @Test
    fun `An ApiFuture can throw an exception`() {
        val invalidEmail = "not.registered@email.com"

        Assertions.assertThrows(FirebaseAuthException::class.java) {
            runBlocking {
                FirebaseAuth.getInstance().getUserByEmailAsync(invalidEmail).await()
            }
        }
    }

}
