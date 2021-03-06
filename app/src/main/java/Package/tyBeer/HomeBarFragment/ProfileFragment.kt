package Package.tyBeer.HomeBarFragment

import Package.tyBeer.AdapterPost
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
import android.widget.GridView
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.concurrent.thread

class ProfileFragment : Fragment() {

    companion object{
        // VARIABILE STATICA CHE SERVE A ReadPostFragment PER APRIRE IL POST SELEZIONATO
        var SELECTEDPOST : Int? = null
    }

    private lateinit var v : View

    // FUNZIONE CHE SERVE PER SETTARE LA FOTO PROFILO SELEZIONATA DALLA GALLERIA (ANCHE SU FIREBASE)
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
        SELECTEDPOST = null

        // SETTING DATI PROFILO
        val textUsernameProf = v.findViewById<TextView>(R.id.textUsernameProfile)
        textUsernameProf.text = HomeActivity.thisUser!!.nicknameU
        val textNameProf = v.findViewById<TextView>(R.id.textNameProfile)
        textNameProf.text = HomeActivity.thisUser!!.nameU
        val textSurnameProf = v.findViewById<TextView>(R.id.textSurnameProfile)
        textSurnameProf.text = HomeActivity.thisUser!!.surnameU
        val textFollower = v.findViewById<TextView>(R.id.textFollower)
        textFollower.text = HomeActivity.thisUser!!.followerListU.size.toString()
        val textEmailProf = v.findViewById<TextView>(R.id.textEmailProfile)
        textEmailProf.text = HomeActivity.thisUser!!.emailU
        val profilePhoto = v.findViewById<CircleImageView>(R.id.imageProfile)
        profilePhoto.setImageBitmap(BitmapFactory.decodeFile(HomeActivity.thisPhotoProfile!!.path))
        profilePhoto.setOnClickListener {  onClickChangePhoto() }
        // SETTING POST
        val GridListPost = v.findViewById<GridView>(R.id.GridViewListPost)
        GridListPost.adapter = AdapterPost(requireContext(), HomeActivity.thisPhotoPost)
        GridListPost.setOnItemClickListener { parent, view, position, id ->
            SELECTEDPOST= HomeActivity.thisPhotoPost.size - position -1
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<ReadPostFragment>(R.id.FragmentContainer).addToBackStack(null)
            }
        }
        // SETTING VISUALIZZAZIONE FOLLOWER
        val textTitleFollower = v.findViewById<TextView>(R.id.textTitleFollower)
        textTitleFollower.setOnClickListener { openFollower() }
        textFollower.setOnClickListener { openFollower() }
        return v
    }

    // APRE LA SCHERMATA PER VISUALIZZARE I PROPRI FOLLOWER
    private fun openFollower() {
        requireActivity().supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<FollowerFragment>(R.id.FragmentContainer).addToBackStack(null)
        }
    }

    // SETTA LA BARRA DEL MENU'
    private fun setButton(){
        val btnHome = requireActivity().findViewById<ImageButton>(R.id.btnBarHome)
        val btnFriends = requireActivity().findViewById<ImageButton>(R.id.btnBarFriends)
        val btnAdd = requireActivity().findViewById<ImageButton>(R.id.btnBarAdd)
        val btnProfile = requireActivity().findViewById<ImageButton>(R.id.btnBarProfile)
        btnHome.setImageResource(R.drawable.ic_bar_home)
        btnFriends.setImageResource(R.drawable.ic_bar_friends)
        btnAdd.setImageResource(R.drawable.ic_bar_add)
        btnProfile.setImageResource(R.drawable.ic_bar_profile_sel)
    }

    // RICHIAMA L'INTENT DELLA GALLERIA
    private fun onClickChangePhoto(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        FunctionPhoto.launch(intent)
    }

}