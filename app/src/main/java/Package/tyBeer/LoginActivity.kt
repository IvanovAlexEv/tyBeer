package Package.tyBeer

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieAnimationView.INVISIBLE
import com.airbnb.lottie.LottieAnimationView.VISIBLE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.LottieLoad
import kotlinx.android.synthetic.main.activity_waiting_email.*
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance();
    }
    override fun onStart() {
        super.onStart()
        UpdateUI(auth.currentUser)
    }

    fun checkLogin(v: View?){
        LottieLoad!!.visibility = VISIBLE
        LottieLoad!!.playAnimation()
        val email:String = editText_emailLogin.text.toString()
        val pax:String = editText_paxLogin.text.toString()
        var emailOkay : Boolean = true
        var paxOkay : Boolean = true
        if (!isValidEmail(email)) {
            editText_emailLogin.error = getString(R.string.str_errorEmail)
            emailOkay = false
        }
        if (!isValidPassword(pax)) {
            editText_paxLogin.error = getString(R.string.str_errorPax)
            paxOkay = false
        }
        if( !emailOkay || !paxOkay) {
            LottieLoad!!.cancelAnimation()
            LottieLoad!!.visibility = INVISIBLE
            return
        }
        isUserFound(pax,email)
    }
    private fun isValidEmail(email:String):Boolean{
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_+A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
    private fun isValidPassword(pax:String?):Boolean {
        var UpperCase: Boolean = false
        var LowerCase: Boolean = false
        var Number: Boolean = false
        var Length: Boolean = false
        if (pax != null) {
            for (i in pax) {
                if (i.isUpperCase()) UpperCase = true
                if (i.isLowerCase()) LowerCase = true
                if (i.isDigit()) Number = true
            }
            if (pax.length >= 8) Length = true
        }
        return (UpperCase && LowerCase && Number && Length)
    }

    private fun isUserFound(pax:String, email:String) {
        auth.signInWithEmailAndPassword(email, pax).addOnCompleteListener(this) { task ->
            if(task.isSuccessful){
                UpdateUI(auth.currentUser)
            } else {
                LottieLoad.cancelAnimation()
                LottieLoad.visibility = INVISIBLE
                editText_emailLogin.error = getString(R.string.str_errorNotFound)
                Toast.makeText(this@LoginActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                UpdateUI(null)
            }
        }
    }
    private fun UpdateUI(currentUser : FirebaseUser?) {
        currentUser?.reload()
        if (currentUser != null && currentUser.isEmailVerified){
            val intentHome = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intentHome)
            finish()
            return
        }
        if (currentUser != null && !currentUser.isEmailVerified) {
            val intentHome = Intent(this@LoginActivity, WaitingEmailActivity::class.java)
            startActivity(intentHome)
            finish()
            return
        }
    }
    fun openRegister(v:View){
        val intentHome = Intent(this@LoginActivity, SigninActivity::class.java)
        startActivity(intentHome)
        finish();
    }
    fun openNewPax(v:View){
        val emailText = EditText(this@LoginActivity)
        val newPax = AlertDialog.Builder(this@LoginActivity)
        newPax.setTitle(R.string.str_TitleNewPax)
        newPax.setMessage(R.string.str_MessageNewPax)
        newPax.setView(emailText)
        newPax.setPositiveButton(R.string.str_Invia, DialogInterface.OnClickListener { dialog, which ->
            var email : String = emailText.text.toString()
            if (isValidEmail(email)){
                auth.sendPasswordResetEmail(email).addOnSuccessListener {
                    Toast.makeText(this@LoginActivity, "Email sent.", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{
                    Toast.makeText(this@LoginActivity, "Error! Email not found. " + it.message, Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this@LoginActivity, R.string.str_errorEmail, Toast.LENGTH_LONG).show()
            }
        })
        newPax.setNegativeButton(R.string.str_Chiudi, DialogInterface.OnClickListener { dialog, which ->})
        newPax.create().show()
    }
}