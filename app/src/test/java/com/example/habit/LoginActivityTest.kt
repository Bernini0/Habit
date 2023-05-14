//import android.text.TextUtils
//import android.widget.EditText
//import android.widget.TextView
//import com.google.android.material.textfield.TextInputLayout
//import com.google.firebase.auth.FirebaseAuth
//import org.junit.Assert.assertFalse
//import org.junit.Assert.assertTrue
//import org.junit.Before
//import org.junit.Test
//import org.mockito.Mock
//import org.mockito.Mockito
//import org.mockito.MockitoAnnotations
//
//class LoginActivityTests {
//
//    @Mock
//    private lateinit var mAuth: FirebaseAuth
//
//    @Mock
//    private lateinit var mEdtEmail: EditText
//
//    @Mock
//    private lateinit var mEdtPassword: EditText
//
//    @Mock
//    private lateinit var mLayoutEmail: TextInputLayout
//
//    @Mock
//    private lateinit var mLayoutPassword: TextInputLayout
//
//    @Mock
//    private lateinit var mAuthErrorMessage: TextView
//
//    private lateinit var loginActivity: LoginActivity
//
//    @Before
//    fun setup() {
//        MockitoAnnotations.initMocks(this)
//        loginActivity = LoginActivity()
//        loginActivity.mAuth = mAuth
//        loginActivity.mEdtEmail = mEdtEmail
//        loginActivity.mEdtPassword = mEdtPassword
//        loginActivity.mLayoutEmail = mLayoutEmail
//        loginActivity.mLayoutPassword = mLayoutPassword
//        loginActivity.mAuthErrorMessage = mAuthErrorMessage
//    }
//
//    @Test
//    fun testValidateForm_ValidInputs() {
//        Mockito.`when`(mEdtEmail.text).thenReturn(Mockito.mock(CharSequence::class.java))
//        Mockito.`when`(mEdtPassword.text).thenReturn(Mockito.mock(CharSequence::class.java))
//        assertTrue(loginActivity.validateForm())
//        Mockito.verify(mLayoutEmail, Mockito.never())?.error = Mockito.anyString()
//        Mockito.verify(mLayoutPassword, Mockito.never())?.error = Mockito.anyString()
//    }
//
//    @Test
//    fun testValidateForm_EmptyEmail() {
//        Mockito.`when`(mEdtEmail.text).thenReturn(null)
//        Mockito.`when`(mEdtPassword.text).thenReturn(Mockito.mock(CharSequence::class.java))
//        assertFalse(loginActivity.validateForm())
//        Mockito.verify(mLayoutEmail)?.error = "Required."
//        Mockito.verify(mLayoutPassword, Mockito.never())?.error = Mockito.anyString()
//    }
//
//    @Test
//    fun testValidateForm_EmptyPassword() {
//        Mockito.`when`(mEdtEmail.text).thenReturn(Mockito.mock(CharSequence::class.java))
//        Mockito.`when`(mEdtPassword.text).thenReturn(null)
//        assertFalse(loginActivity.validateForm())
//        Mockito.verify(mLayoutEmail, Mockito.never())?.error = Mockito.anyString()
//        Mockito.verify(mLayoutPassword)?.error = "Required."
//    }
//
//    @Test
//    fun testValidateForm_EmptyInputs() {
//        Mockito.`when`(mEdtEmail.text).thenReturn(null)
//        Mockito.`when`(mEdtPassword.text).thenReturn(null)
//        assertFalse(loginActivity.validateForm())
//        Mockito.verify(mLayoutEmail)?.error = "Required."
//        Mockito.verify(mLayoutPassword)?.error = "Required."
//    }
//}