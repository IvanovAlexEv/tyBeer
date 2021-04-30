package Package.tyBeer

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_waiting_email.*

class WaitingEmailActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_email)
        auth = FirebaseAuth.getInstance();
        textView_emailAddress.text = auth.currentUser?.email.toString()
        LottieLoad.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                LottieLoad.visibility = LottieAnimationView.INVISIBLE
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        currentUser?.reload()
        UpdateUI(currentUser)
    }
    private fun UpdateUI(currentUser : FirebaseUser?) {
        if (currentUser != null && currentUser.isEmailVerified){
            val intentHome = Intent(this@WaitingEmailActivity, HomeActivity::class.java)
            startActivity(intentHome)
            finish()
        }
    }
    fun onReload(v: View?){
            LottieLoad.visibility = LottieAnimationView.VISIBLE
            LottieLoad.playAnimation()
            val currentUser = auth.currentUser
            currentUser?.reload()
            UpdateUI(currentUser)
    }
    fun onSendNewEmail(v: View?){
        LottieLoad.visibility = LottieAnimationView.VISIBLE
        LottieLoad.playAnimation()
        auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
            Toast.makeText(this@WaitingEmailActivity, R.string.str_EmailVerifSent, Toast.LENGTH_LONG).show();
        }?.addOnFailureListener {
            Toast.makeText(this@WaitingEmailActivity, "Error! Email not sent. " + it.message, Toast.LENGTH_LONG).show();
        }
    }
    fun onLogOut(v: View?){
        FirebaseAuth.getInstance().signOut();
        val intentHome = Intent(this, LoginActivity::class.java)
        startActivity(intentHome)
        finish()
    }
}