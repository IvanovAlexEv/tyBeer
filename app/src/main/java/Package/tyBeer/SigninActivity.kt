package Package.tyBeer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_signin.*
import java.util.regex.Pattern

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
    }

    fun checkRegister(v: View?){
        val username:String = editText_UsernameRegister.text.toString()
        val email:String = editText_emailRegister.text.toString()
        val pax:String = editText_paxRegister.text.toString()
        val paxRepeat:String = editText_paxRegisterRepeat.text.toString()
        val name:String = editText_NameRegister.text.toString()
        val surname:String = editText_NameRegister.text.toString()
        var isAllOkay:Boolean = true

        // CONTROLLO DATI
        if (!isValidUsername(username)){
            editText_UsernameRegister.error =getString(R.string.str_errorUsername)
            isAllOkay=false
        }
        if (!isUsernameFree(username)){
            editText_UsernameRegister.error =getString(R.string.str_errorUsernameNotFree)
            isAllOkay=false
        }
        if (!isValidNameOrSurname(name)){
            editText_NameRegister.error =getString(R.string.str_errorNameOrSurname)
            isAllOkay=false
        }
        if (!isValidNameOrSurname(surname)){
            editText_SurnameRegister.error =getString(R.string.str_errorNameOrSurname)
            isAllOkay=false
        }
        if (!isValidEmail(email)){
            editText_emailRegister.error = getString(R.string.str_errorEmail)
            isAllOkay=false
        }
        if (!isEmailFree(email)){
            editText_emailRegister.error = getString(R.string.str_errorEmailNotFree)
            isAllOkay=false
        }
        if (!isValidPassword(pax)){
            editText_paxRegister.error = getString(R.string.str_errorPax)
            isAllOkay=false
        }
        if (!pax.equals(paxRepeat)){
            editText_paxRegisterRepeat.error = getString(R.string.str_errorPaxEquals)
            isAllOkay=false
        }
        if (isAllOkay) {
            // REGISTRAZIONE...
            Toast.makeText(this@SigninActivity, "REGISTRAZIONE...", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isValidUsername(user:String?):Boolean {
        var valid:Boolean=true
        if (user != null) {
            if (user.length<4) valid=false
            for (i in user) {
                if (i.equals(" ")) valid=false
                if (!i.isLetterOrDigit()) valid=false
            }
        } else return false
        return valid
    }
    private fun isUsernameFree(user:String?):Boolean {
        //  CONTROLLO DISPONIBILITA' USERNAME
        return true
    }
    private fun isValidNameOrSurname(NorS:String?):Boolean{
        return !(NorS.equals(""))
    }
    private fun isValidEmail(email:String):Boolean{
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_+A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
    private fun isEmailFree(email:String):Boolean{
        //  CONTROLLO DISPONIBILITA' EMAIL
        return true
    }
    private fun isValidPassword(pax:String?):Boolean {
        var UpperCase:Boolean=false
        var LowerCase:Boolean=false
        var Number:Boolean=false
        var Length:Boolean=false
        if (pax != null) {
            for (i in pax){
                if (i.isUpperCase()) UpperCase=true
                if (i.isLowerCase()) LowerCase=true
                if (i.isDigit()) Number=true
            }
            if (pax.length>=8)  Length=true
        }
        return ( UpperCase && LowerCase && Number && Length)
    }
    fun openLogin(v: View){
        finish()
    }
}