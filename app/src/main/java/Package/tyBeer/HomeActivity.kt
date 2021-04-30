package Package.tyBeer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun onLogOut(v: View?){
        FirebaseAuth.getInstance().signOut();
        val intentHome = Intent(this, LoginActivity::class.java)
        startActivity(intentHome)
        finish()
    }
}