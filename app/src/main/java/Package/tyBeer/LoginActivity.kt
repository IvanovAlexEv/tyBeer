package Package.tyBeer

import android.content.Intent
import android.os.Bundle
import android.view.View
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
        if (!isValidEmail(email)){
            editText_emailLogin.error = getString(R.string.str_errorEmail)
        }
        if (!isValidPassword(pax)){
            editText_paxLogin.error = getString(R.string.str_errorPax)
        }
    }
    private fun isValidEmail(email:String):Boolean{
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_+A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
    private fun isValidPassword(pax:String?):Boolean {
        return (pax != null && pax.length>=8)
    }

    fun openRegister(v:View){
        val intentHome = Intent(this@LoginActivity, SigninActivity::class.java)
        startActivity(intentHome)
    }
}