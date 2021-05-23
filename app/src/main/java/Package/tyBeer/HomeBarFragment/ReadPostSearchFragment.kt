package Package.tyBeer.HomeBarFragment

import Package.tyBeer.HomeActivity
import Package.tyBeer.Post
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Package.tyBeer.R
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView

class ReadPostSearchFragment : Fragment() {

    // POST SELEZIONATO DA VISUALIZZARE DI UN'ALTRO PROFILO, NON IL PROPRIO
    private lateinit var postRead : Post

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_read_post_search, container, false)
        val star1 = v.findViewById<LottieAnimationView>(R.id.Star1S)
        val star2 = v.findViewById<LottieAnimationView>(R.id.Star2S)
        val star3 = v.findViewById<LottieAnimationView>(R.id.Star3S)
        val star4 = v.findViewById<LottieAnimationView>(R.id.Star4S)
        val star5 = v.findViewById<LottieAnimationView>(R.id.Star5S)
        val descPost = v.findViewById<TextView>(R.id.textDescPostReadS)
        val photoPost = v.findViewById<ImageView>(R.id.imagePostReadS)
        val postDate = v.findViewById<TextView>(R.id.textDatePostS)
        val postAuthor = v.findViewById<TextView>(R.id.textAuthorPostS)
        // RECUPERO DEL POST SELEZIONATO
        postRead = ReadSearchFragment.utenteSelez!!.postList[ReadSearchFragment.SELECTEDSEARCHPOST!!]
        // SETTING DEI DATI DEL POST
        photoPost.setImageBitmap(BitmapFactory.decodeFile(ReadSearchFragment.PhotoPostUS[ReadSearchFragment.SELECTEDSEARCHPOST!!]!!.path))
        postDate.text = postRead.date
        postAuthor.text = ReadSearchFragment.utenteSelez!!.nicknameU
        if (postRead.star>=1){
            star1.playAnimation()
        }
        if (postRead.star>=2){
            star2.playAnimation()
        }
        if (postRead.star>=3){
            star3.playAnimation()
        }
        if (postRead.star>=4){
            star4.playAnimation()
        }
        if (postRead.star==5){
            star5.playAnimation()
        }
        descPost.text = postRead.desc
        return v
    }

}