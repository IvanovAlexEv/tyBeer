package Package.tyBeer.HomeBarFragment

import Package.tyBeer.HomeActivity
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

class ReadPostFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_read_post, container, false)
        val star1 = v.findViewById<LottieAnimationView>(R.id.Star1)
        val star2 = v.findViewById<LottieAnimationView>(R.id.Star2)
        val star3 = v.findViewById<LottieAnimationView>(R.id.Star3)
        val star4 = v.findViewById<LottieAnimationView>(R.id.Star4)
        val star5 = v.findViewById<LottieAnimationView>(R.id.Star5)
        val descPost = v.findViewById<TextView>(R.id.textDescPostRead)
        val photoPost = v.findViewById<ImageView>(R.id.imagePostRead)
        val postDate = v.findViewById<TextView>(R.id.textDatePost)
        val postAuthor = v.findViewById<TextView>(R.id.textAuthorPost)
        val postRead = HomeActivity.thisUser!!.postList[ProfileFragment.SELECTEDPOST!!]
        photoPost.setImageBitmap(BitmapFactory.decodeFile(HomeActivity.thisPhotoPost[ProfileFragment.SELECTEDPOST!!]!!.path))
        postDate.text = postRead.date
        postAuthor.text = HomeActivity.thisUser!!.nicknameU
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