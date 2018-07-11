package kotlinx.coroutines.experimental.firebase.tasks

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseCredentials
import com.google.firebase.auth.UserRecord
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.FileInputStream

@DisplayName("Task<T>.await() Test")
class TaskApiTest {

    companion object {

        @BeforeAll
        @JvmStatic
        fun setUpFirebase() {
            FileInputStream("serviceAccount.json").use {
                FirebaseApp.initializeApp(FirebaseOptions.Builder()
                        .setCredential(FirebaseCredentials.fromCertificate(it))
                        .build())
            }
        }
    }

    @Test
    fun `A Task can return a result`() = runBlocking<Unit> {
        val auth = FirebaseAuth.getInstance()

        val name = "John"
        val email = "example@email.com"
        auth.createUser(UserRecord.CreateRequest()
                .setDisplayName(name)
                .setEmail(email)).await()

        val user = auth.getUserByEmail(email).await()

        assertEquals(user.displayName, name)
        assertEquals(user.email, email)

        auth.deleteUser(user.uid).await()
    }

    @Test
    fun `A Task can throw an exception`() {
        val invalidEmail = "not.registered@email.com"

        assertThrows(FirebaseAuthException::class.java) {
            runBlocking {
                FirebaseAuth.getInstance().getUserByEmail(invalidEmail).await()
            }
        }
    }
}
