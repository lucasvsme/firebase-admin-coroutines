package kotlinx.coroutines.experimental.firebase.futures

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.FileInputStream
import java.nio.file.Paths

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiFutureTest {

    private val credentialsPath = Paths.get(".").resolve("..").resolve("google-services.json")

    @BeforeAll
    fun setUpFirebase() {
        FileInputStream(credentialsPath.toFile()).use {
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

        assertEquals(user.displayName, name)
        assertEquals(user.email, email)

        auth.deleteUserAsync(user.uid).await()
    }

    @Test
    fun `An ApiFuture can throw an exception`() {
        val invalidEmail = "not.registered@email.com"

        assertThrows(FirebaseAuthException::class.java) {
            runBlocking {
                FirebaseAuth.getInstance().getUserByEmailAsync(invalidEmail).await()
            }
        }
    }

}
