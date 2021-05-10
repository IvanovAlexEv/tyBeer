package Package.tyBeer.HomeBarFragment

import Package.tyBeer.HomeActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Package.tyBeer.R
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.concurrent.thread

class ProfileFragment : Fragment() {

    private lateinit var v : View
    val FunctionPhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        thread{
            val profilePhoto = v.findViewById<CircleImageView>(R.id.imageProfile)
            var data : Uri? = null
            if (it.resultCode == Activity.RESULT_OK){
                data = it.data!!.data
                val refStorage = FirebaseStorage.getInstance().getReference("PhotoProfile/${HomeActivity.thisUser!!.photoProfile}")
                refStorage.putFile(data!!).addOnCompleteListener {
                    val userRef = FirebaseDatabase.getInstance().getReference("users")
                    HomeActivity.thisUser!!.nChangePP++
                    userRef.child(HomeActivity.idUser!!).setValue(HomeActivity.thisUser)
                }
            }
            profilePhoto.post {
                if (data!= null)
                    profilePhoto.setImageURI(data)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_profile, container, false)
        setButton()

        val testUsernameProf = v.findViewById<TextView>(R.id.textUsernameProfile)
        testUsernameProf.text = HomeActivity.thisUser!!.nicknameU
        val testNameProf = v.findViewById<TextView>(R.id.textNameProfile)
        testNameProf.text = HomeActivity.thisUser!!.nameU
        val testSurnameProf = v.findViewById<TextView>(R.id.textSurnameProfile)
        testSurnameProf.text = HomeActivity.thisUser!!.surnameU
        val testEmailProf = v.findViewById<TextView>(R.id.textEmailProfile)
        testEmailProf.text = HomeActivity.thisUser!!.emailU
        val profilePhoto = v.findViewById<CircleImageView>(R.id.imageProfile)
        profilePhoto.setImageBitmap(BitmapFactory.decodeFile(HomeActivity.thisPhotoProfile!!.path))
        profilePhoto.setOnClickListener {  onClickChangePhoto() }
        return v
    }

    fun setButton(){
        val btnHome = requireActivity().findViewById<ImageButton>(R.id.btnBarHome)
        val btnFriends = requireActivity().findViewById<ImageButton>(R.id.btnBarFriends)
        val btnAdd = requireActivity().findViewById<ImageButton>(R.id.btnBarAdd)
        val btnProfile = requireActivity().findViewById<ImageButton>(R.id.btnBarProfile)
        btnHome.setImageResource(R.drawable.ic_bar_home)
        btnFriends.setImageResource(R.drawable.ic_bar_friends)
        btnAdd.setImageResource(R.drawable.ic_bar_add)
        btnProfile.setImageResource(R.drawable.ic_bar_profile_sel)
    }

    fun onClickChangePhoto(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        FunctionPhoto.launch(intent)
    }

}