package Package.tyBeer

import android.animation.Animator
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // ANIMAZIONE APERTURA APP
        LottieBeer.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                openHome()
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
        val SoundBeer: MediaPlayer = MediaPlayer.create(this,R.raw.sound_beer)
        SoundBeer.start()
    }

    // APERTURA APP
    fun openHome(){
        val intentHome = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intentHome)
        finish()
    }
}

