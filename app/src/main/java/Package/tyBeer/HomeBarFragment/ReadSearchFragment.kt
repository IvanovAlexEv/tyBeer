package Package.tyBeer.HomeBarFragment

import Package.tyBeer.AdapterPost
import Package.tyBeer.HomeActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Package.tyBeer.R
import Package.tyBeer.User
import android.graphics.BitmapFactory
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import kotlin.concurrent.thread

class ReadSearchFragment : Fragment() {

    companion object{
        // VARIABILI STATICHE CHE SERVONO PER MEMORIZZARE I DATI DELL'UTENTE SELEZIONATO DA VISUALIZZARE
        var SELECTEDSEARCHPOST : Int? = null
        var utenteSelez : User? = null
        var PhotoProfileUS : File? = null
        var PhotoPostUS : MutableList<File?> = ArrayList()
        var readyPhotoUS: Boolean = false
        var readyPostUS: Int = 0
    }

    lateinit var btnFollow : Button
    lateinit var btnUnfollow : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_read_search, container, false)
        clearState()
        // RECUPERO DELl'UTENTE SELEZIONATO
        utenteSelez = HomeActivity.UserList.get(HomeActivity.SELECTEDUSERID)
        // SETTING DEI DATI DELL'UTENTE
        PhotoProfileUS = File.createTempFile(utenteSelez!!.photoProfile, "jpg")
        val textUsernameProfSearch = v.findViewById<TextView>(R.id.textUsernameProfileSearch)
        textUsernameProfSearch.text = utenteSelez!!.nicknameU
        val textNameProfSearch = v.findViewById<TextView>(R.id.textNameProfileSearch)
        textNameProfSearch.text = utenteSelez!!.nameU
        val textSurnameProfSearch = v.findViewById<TextView>(R.id.textSurnameProfileSearch)
        textSurnameProfSearch.text = utenteSelez!!.surnameU
        val textFollowerSearch = v.findViewById<TextView>(R.id.textFollowerSearch)
        textFollowerSearch.text = utenteSelez!!.followerListU.size.toString()
        val textEmailProfSearch = v.findViewById<TextView>(R.id.textEmailProfileSearch)
        textEmailProfSearch.text = utenteSelez!!.emailU
        recuperoDatiUtenteSelezionato()
        btnFollow = v.findViewById(R.id.btnFollow)
        btnFollow.setOnClickListener { onClickFollow() }
        btnUnfollow = v.findViewById(R.id.btnUnfollow)
        btnUnfollow.setOnClickListener { onClickUnfollow() }
        btnUnfollow.visibility = Button.INVISIBLE
        btnFollow.visibility = Button.INVISIBLE

        // THREAD CHE SI OCCUPA DI SETTARE LE FOTO, PROCESSO ABBASTANZA LUNGO
        thread {
            val profilePhotoSearch = v.findViewById<CircleImageView>(R.id.imageProfileSearch)
            val GridListPostUS = v.findViewById<GridView>(R.id.GridViewListPostSearch)
            val loadProfile = v.findViewById<LottieAnimationView>(R.id.LottieLoadProfileSearch)
            val loadPost = v.findViewById<LottieAnimationView>(R.id.LottieLoadPostSearch)
            // ASPETTA I DATI DELLA FOTO PROFILO
            while( !readyPhotoUS ){ }
            loadProfile.post {
                loadProfile.cancelAnimation()
                loadProfile.visibility = LottieAnimationView.INVISIBLE
            }
            profilePhotoSearch.post { profilePhotoSearch.setImageBitmap(BitmapFactory.decodeFile(PhotoProfileUS!!.path)) }
            // ASPETTA I DATI DELLA FOTO DEI POST
            while( 0 != readyPostUS ){ }
            loadPost.post {
                loadPost.cancelAnimation()
                loadPost.visibility = LottieAnimationView.INVISIBLE
            }
            // SETTING DATI POST
            GridListPostUS.post {
                GridListPostUS.adapter = AdapterPost(requireContext(), PhotoPostUS)
                GridListPostUS.setOnItemClickListener { parent, view, position, id ->
                    SELECTEDSEARCHPOST = PhotoPostUS.size - position -1
                    requireActivity().supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<ReadPostSearchFragment>(R.id.FragmentContainer).addToBackStack(null)
                    }
                }
            }
            // SETTING OPPORTUNO DEL BOTTONE "SEGUI/NONSEGUIRE"
            btnFollow.post {
                if ( HomeActivity.thisUser!!.friendsListU.contains(utenteSelez!!.id) ){
                    btnUnfollow.visibility = Button.VISIBLE
                } else {
                    btnFollow.visibility = Button.VISIBLE
                }
            }
        }
        return v
    }

    // PULIZIA DELLO STATO DEL FRAGMENT
    private fun clearState() {
        SELECTEDSEARCHPOST  = null
        utenteSelez  = null
        PhotoProfileUS = null
        PhotoPostUS.clear()
        readyPhotoUS = false
        readyPostUS = 0
    }

    //  FUNZIONE CHE SI OCCUPA DI RECUPERARE I DATI NECESSARI DAL DATABASE (FIREBASE)
    private fun recuperoDatiUtenteSelezionato() {
        val refDB = FirebaseDatabase.getInstance().getReference("users/${utenteSelez!!.id}")
        val refDBListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                readyPhotoUS = false
                utenteSelez = snapshot.getValue(User::class.java)!!
                val refStorage = FirebaseStorage.getInstance().getReference("PhotoProfile/${utenteSelez!!.photoProfile}")
                refStorage.getFile(PhotoProfileUS!!).addOnCompleteListener { readyPhotoUS=true }
                //RECUPERO POST UTENTE
                PhotoPostUS.clear()
                readyPostUS = readyPostUS - utenteSelez!!.postList.size
                for ((index, post) in utenteSelez!!.postList.withIndex()){
                    val filePost = File.createTempFile(post.idPost, "jpg")
                    PhotoPostUS.add(filePost)
                    val refStoragePost = FirebaseStorage.getInstance().getReference("PhotoPost/${utenteSelez!!.id}/${post.idPost}")
                    refStoragePost.getFile(filePost).addOnSuccessListener {
                        readyPostUS++
                        PhotoPostUS.removeAt(index)
                        PhotoPostUS.add(index, filePost)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        refDB.addListenerForSingleValueEvent(refDBListener)
    }

    // GESTIONE DEL BOTTONE "SEGUI", AGGIORNAMENTO DATABASE (FIREBASE)
    fun onClickFollow(){
        btnFollow.visibility = Button.INVISIBLE
        btnUnfollow.visibility = Button.VISIBLE
        // AGGIORNAMENTO DB
        val userRef = FirebaseDatabase.getInstance().getReference("users/${HomeActivity.thisUser!!.id}")
        HomeActivity.thisUser!!.friendsListU.add(utenteSelez!!.id);
        userRef.child("friendsListU").setValue(HomeActivity.thisUser!!.friendsListU)
        val followerRef = FirebaseDatabase.getInstance().getReference("users/${utenteSelez!!.id}")
        utenteSelez!!.followerListU.add(HomeActivity.thisUser!!.id)
        followerRef.child("followerListU").setValue(utenteSelez!!.followerListU)
    }

    // GESTIONE DEL BOTTONE "NON SEGUIRE",  AGGIORNAMENTO DATABASE (FIREBASE)
    fun onClickUnfollow(){
        btnUnfollow.visibility = Button.INVISIBLE
        btnFollow.visibility = Button.VISIBLE
        // AGGIORNAMENTO DB
        val userRef = FirebaseDatabase.getInstance().getReference("users/${HomeActivity.thisUser!!.id}")
        HomeActivity.thisUser!!.friendsListU.remove(utenteSelez!!.id);
        userRef.child("friendsListU").setValue(HomeActivity.thisUser!!.friendsListU)
        val followerRef = FirebaseDatabase.getInstance().getReference("users/${utenteSelez!!.id}")
        utenteSelez!!.followerListU.remove(HomeActivity.thisUser!!.id)
        followerRef.child("followerListU").setValue(utenteSelez!!.followerListU)
    }
}