package Package.tyBeer

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_signin.LottieLoad
import java.util.regex.Pattern

class SigninActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private val userRef = FirebaseDatabase.getInstance().getReference("users")
    var ListUser : MutableList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        auth = FirebaseAuth.getInstance();
    }
    override fun onStart() {
        super.onStart()
        UpdateUI(auth.currentUser)
        allUsername()
    }

    // CHECK DI VERIFICA DEI CAMPI
    fun checkRegister(v: View?){

        LottieLoad!!.visibility = LottieAnimationView.VISIBLE
        LottieLoad!!.playAnimation()

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
        if (username.length>12){
            editText_UsernameRegister.error =getString(R.string.str_errorLong)
            isAllOkay=false
        }
        if (!isValidNameOrSurname(name)){
            editText_NameRegister.error =getString(R.string.str_errorNameOrSurname)
            isAllOkay=false
        }
        if (name.length>12){
            editText_NameRegister.error =getString(R.string.str_errorLong)
            isAllOkay=false
        }
        if (!isValidNameOrSurname(surname)){
            editText_SurnameRegister.error =getString(R.string.str_errorNameOrSurname)
            isAllOkay=false
        }
        if (surname.length>12){
            editText_SurnameRegister.error =getString(R.string.str_errorLong)
            isAllOkay=false
        }
        if (!isValidEmail(email)){
            editText_emailRegister.error = getString(R.string.str_errorEmail)
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
            RegistrationUser(username, name, surname, email, pax)
        } else {
            LottieLoad!!.cancelAnimation()
            LottieLoad!!.visibility = LottieAnimationView.INVISIBLE
        }
    }
    // RECUPERO DI TUTTI I NICKNAME
    private fun allUsername() {
        val ref = FirebaseDatabase.getInstance().getReference("users")
        val listenerRef = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ListUser.clear()
                for (i in snapshot.children){
                    ListUser.add(i.getValue(User::class.java)!!)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        ref.addValueEventListener(listenerRef)
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
    private fun isUsernameFree(user:String?) : Boolean{
        for (i in ListUser) {
            if(user.equals(i.nicknameU))
                return false
        }
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
    private fun isValidPassword(pax:String?):Boolean {
        var UpperCase:Boolean=false
        var LowerCase:Boolean=false
        var Number:Boolean=false
        var Length:Boolean=false
        if (pax != null) {
            for (i in pax){
                if (i.equals(" ")) return false
                if (i.isUpperCase()) UpperCase=true
                if (i.isLowerCase()) LowerCase=true
                if (i.isDigit()) Number=true
            }
            if (pax.length>=8)  Length=true
        }
        return ( UpperCase && LowerCase && Number && Length)
    }
    // REGISTRAZIONE DELL'UTENTE...
    private fun RegistrationUser(username: String , name: String , surname: String, email: String, pax: String){
        auth.createUserWithEmailAndPassword(email,pax).addOnCompleteListener(this){ task ->
            if(task.isSuccessful){
                val currentUser = auth.currentUser
                currentUser?.sendEmailVerification()?.addOnSuccessListener {
                    Toast.makeText(this@SigninActivity, R.string.str_EmailVerifSent, Toast.LENGTH_LONG).show();
                    storeUser(currentUser)
                }?.addOnFailureListener {
                    Toast.makeText(this@SigninActivity, "Error! Email not sent. " + it.message, Toast.LENGTH_LONG).show();
                }
                UpdateUI(currentUser)
            } else {
                Toast.makeText(this@SigninActivity, R.string.str_errorEmailNotFree, Toast.LENGTH_SHORT).show();
                UpdateUI(null)
            }
        }
        LottieLoad!!.cancelAnimation()
        LottieLoad!!.visibility = LottieAnimationView.INVISIBLE
    }
    // GESTIONE DEL CAMBIAMENTO DELLO STATO DELL'UTENTE
    private fun UpdateUI(currentUser : FirebaseUser?) {
        currentUser?.reload()
        if (currentUser != null){
            val intentHome = Intent(this@SigninActivity, WaitingEmailActivity::class.java)
            startActivity(intentHome)
            finish()
        }
    }
    fun openLogin(v: View){
        val intentHome = Intent(this@SigninActivity, LoginActivity::class.java)
        startActivity(intentHome)
        finish()
    }
    // MEMORIZZAZIONE DATI DELL'UTENTE REGISTRATO SUL DATABASE (FIREBASE)
    private fun storeUser(currentUser : FirebaseUser?){
        val newUser = User(currentUser!!.uid, editText_UsernameRegister.text.toString(), editText_NameRegister.text.toString(), editText_SurnameRegister.text.toString(), editText_emailRegister.text.toString(),"PHOTOPROFILE${currentUser.uid}", 0, 0)
        userRef.child(newUser.id).setValue(newUser)
        val refStorage = FirebaseStorage.getInstance().getReference("PhotoProfile/${newUser.photoProfile}")
        refStorage.putFile(Uri.parse("android.resource://Package.tyBeer/drawable/logo"))
    }

}