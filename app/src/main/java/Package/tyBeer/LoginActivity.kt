package Package.tyBeer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    fun checkLogin(v: View?){
        val email:String = editText_emailLogin.text.toString()
        val pax:String = editText_paxLogin.text.toString()
        var emailOkay:Boolean = false
        if (!isValidEmail(email)){
            editText_emailLogin.error = getString(R.string.str_errorEmail)
        } else {
            if (!isEmailFound(email)){
                editText_emailLogin.error = getString(R.string.str_errorEmailNotFound)
            } else {
                emailOkay = true
            }
        }
        if (!isValidPassword(pax)){
            editText_paxLogin.error = getString(R.string.str_errorPax)
        } else {
            if (emailOkay){
                if (!isPasswordMatch(pax,email)){
                    editText_paxLogin.error = getString(R.string.str_errorPaxNotMatch)
                } else {
                    // LOGIN...
                    Toast.makeText(this@LoginActivity, "LOGIN...", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun isValidEmail(email:String):Boolean{
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_+A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
    private fun isEmailFound(email:String):Boolean{
        // CONTROLLO ESISTENZA EMAIL
        return false
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
    private fun isPasswordMatch(pax:String?, email:String?):Boolean {
        // CONTROLLO ESISTENZA PASSWORD
        return false
    }

    fun openRegister(v:View){
        val intentHome = Intent(this@LoginActivity, SigninActivity::class.java)
        startActivity(intentHome)
    }
}