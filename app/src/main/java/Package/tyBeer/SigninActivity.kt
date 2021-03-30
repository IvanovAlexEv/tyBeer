package Package.tyBeer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
    }

    fun openLogin(v: View){
        finish()
    }
}