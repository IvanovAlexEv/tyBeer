package Package.tyBeer.HomeBarFragment

import Package.tyBeer.HomeActivity
import Package.tyBeer.Post
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Package.tyBeer.R
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class AddFragment : Fragment() {

    private lateinit var v : View
    private lateinit var star1 : LottieAnimationView
    private lateinit var star2 : LottieAnimationView
    private lateinit var star3 : LottieAnimationView
    private lateinit var star4 : LottieAnimationView
    private lateinit var star5 : LottieAnimationView
    private lateinit var photoPost : ImageView

    private var valutazione : Int = 1
    private var dataPhoto : Uri? = null

    val FunctionPhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        thread{
            if (it.resultCode == Activity.RESULT_OK){
                dataPhoto = it.data!!.data
            }
            photoPost.post {
                if (dataPhoto!= null)
                    photoPost.setImageURI(dataPhoto)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_add, container, false)
        setButton()
        photoPost = v.findViewById(R.id.imagePost)
        photoPost.setOnClickListener {  onClickChangePhoto() }
        star1 = v.findViewById(R.id.LottieStar1)
        star1.setOnClickListener { setStar(1) }
        star2 = v.findViewById(R.id.LottieStar2)
        star2.setOnClickListener { setStar(2) }
        star3 = v.findViewById(R.id.LottieStar3)
        star3.setOnClickListener { setStar(3) }
        star4 = v.findViewById(R.id.LottieStar4)
        star4.setOnClickListener { setStar(4) }
        star5 = v.findViewById(R.id.LottieStar5)
        star5.setOnClickListener { setStar(5) }
        val btnPublish = v.findViewById<Button>(R.id.btnPublish)
        btnPublish.setOnClickListener { publish() }
        val btnBack = v.findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener { clear() }
        onClickChangePhoto()
        return v
    }

    private fun setButton(){
        val btnHome = requireActivity().findViewById<ImageButton>(R.id.btnBarHome)
        val btnFriends = requireActivity().findViewById<ImageButton>(R.id.btnBarFriends)
        val btnAdd = requireActivity().findViewById<ImageButton>(R.id.btnBarAdd)
        val btnProfile = requireActivity().findViewById<ImageButton>(R.id.btnBarProfile)
        btnHome.setImageResource(R.drawable.ic_bar_home)
        btnFriends.setImageResource(R.drawable.ic_bar_friends)
        btnAdd.setImageResource(R.drawable.ic_bar_add_sel)
        btnProfile.setImageResource(R.drawable.ic_bar_profile)
    }

    private fun onClickChangePhoto(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        FunctionPhoto.launch(intent)
    }

    private fun setStar(n : Int){
        valutazione=n
        star1.progress= 0F
        star1.cancelAnimation()
        star2.progress= 0F
        star2.cancelAnimation()
        star3.progress= 0F
        star3.cancelAnimation()
        star4.progress= 0F
        star4.cancelAnimation()
        star5.progress= 0F
        star5.cancelAnimation()
        if (n>=1){
            star1.playAnimation()
        }
        if (n>=2){
            star2.playAnimation()
        }
        if (n>=3){
            star3.playAnimation()
        }
        if (n>=4){
            star4.playAnimation()
        }
        if (n==5){
            star5.playAnimation()
        }
    }
    private fun publish(){
        if (dataPhoto == null){
            Toast.makeText(requireContext(), R.string.str_errorPost, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Uploading...", Toast.LENGTH_SHORT).show();
            val descPost = v.findViewById<TextInputEditText>(R.id.textDescPost)
            val namePost = UUID.randomUUID().toString()
            val formatDate = SimpleDateFormat("dd/M/yyyy")
            val currentDate = formatDate.format(Date())
            val thisPost = Post(namePost, valutazione, descPost.text.toString(), currentDate)
            val refStorage = FirebaseStorage.getInstance().getReference("PhotoPost/${HomeActivity.thisUser!!.id}/$namePost")
            refStorage.putFile(dataPhoto!!).addOnCompleteListener {
                val userRef = FirebaseDatabase.getInstance().getReference("users")
                HomeActivity.thisUser!!.nPost++
                HomeActivity.thisUser!!.postList.add( thisPost )
                userRef.child(HomeActivity.idUser!!).setValue(HomeActivity.thisUser)
            }
        }
    }

    private fun clear(){
        dataPhoto = null
        photoPost.setImageResource(R.drawable.logo)
        valutazione=1
        star1.progress= 0F
        star1.cancelAnimation()
        star2.progress= 0F
        star2.cancelAnimation()
        star3.progress= 0F
        star3.cancelAnimation()
        star4.progress= 0F
        star4.cancelAnimation()
        star5.progress= 0F
        star5.cancelAnimation()
        val descPost = v.findViewById<TextInputEditText>(R.id.textDescPost)
        descPost.text?.clear()
        onClickChangePhoto()
    }
}