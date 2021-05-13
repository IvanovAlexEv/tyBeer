package Package.tyBeer

import Package.tyBeer.HomeBarFragment.AddFragment
import Package.tyBeer.LearnFragment.LearnMainFragment
import Package.tyBeer.HomeBarFragment.ProfileFragment
import Package.tyBeer.HomeBarFragment.LoadingProfileFragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File
import java.lang.Thread.sleep
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class HomeActivity : AppCompatActivity() {

    private var exit = 0
    lateinit var auth : FirebaseAuth

    companion object{
        var idUser : String? = null
        var thisUser: User? = null
        var thisPhotoProfile : File? = File.createTempFile("${thisUser?.photoProfile}", "jpg")
        var thisPhotoPost : MutableList<File?> = ArrayList()
        var readyPhoto: Boolean = false
        var readyPost: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (savedInstanceState==null){
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<LearnMainFragment>(R.id.FragmentContainer)
            }
        }
        auth = FirebaseAuth.getInstance();
        idUser = auth.currentUser!!.uid
        recuperoDati()
    }

    private fun recuperoDati() {

        // RECUPERO DATI UTENTE LOGGATO
        val refDB = FirebaseDatabase.getInstance().getReference("users/$idUser")
        val refDBListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                readyPhoto=false
                thisUser = snapshot.getValue(User::class.java)!!
                val refStorage = FirebaseStorage.getInstance().getReference("PhotoProfile/${thisUser!!.photoProfile}")
                refStorage.getFile(thisPhotoProfile!!).addOnCompleteListener { readyPhoto=true }

                //RECUPERO POST UTENTE
                thisPhotoPost.clear()
                readyPost=0
                for ((index, post) in thisUser!!.postList.withIndex()){
                    val filePost = File.createTempFile(post.idPost, "jpg")
                    thisPhotoPost.add(filePost)
                    val refStoragePost = FirebaseStorage.getInstance().getReference("PhotoPost/${thisUser!!.id}/${post.idPost}")
                    refStoragePost.getFile(filePost).addOnSuccessListener {
                        readyPost++
                        thisPhotoPost.removeAt(index)
                        thisPhotoPost.add(index, filePost)
                        //Toast.makeText(this@HomeActivity, "situazione: post=${HomeActivity.readyPost}/${HomeActivity.thisUser?.postList?.size}, fotoProfilo: ${HomeActivity.readyPhoto}" , Toast.LENGTH_SHORT).show();
                    }
                }

            }
            override fun onCancelled(error: DatabaseError) {}
        }
        refDB.addValueEventListener(refDBListener)

        /*
        // RECUPERO POST ????
        val refDB2 = FirebaseDatabase.getInstance().getReference("users")
        refDB2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ListUser.clear()
                for (i in snapshot.children) {
                    val foundUser = i.getValue(User::class.java)!!
                    for (userInList in ListUser) {
                        if (userInList.id.equals(foundUser.id))
                            ListUser.remove(userInList)
                    }
                    ListUser.add(foundUser)
                    //     val refStorage = FirebaseStorage.getInstance().getReference("ProfilePicture/${foundUser.id}")
                    // AGGIUNGO A LISTA MA PRIMA CONTROLLO ESISTENZA     refStorage.getFile(thisPictureUser!!)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        */

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount==0){
            if (exit == 0) {
                exit++
                Toast.makeText(this@HomeActivity, R.string.str_EXIT, Toast.LENGTH_SHORT).show();
            } else if (exit == 1) {
                super.onBackPressed()
            }
            thread {
                sleep(3500)
                exit = 0
            }
        } else{
            super.onBackPressed()
        }
    }

    fun onLogout(item: MenuItem){
        FirebaseAuth.getInstance().signOut();
        val intentHome = Intent(this, LoginActivity::class.java)
        startActivity(intentHome)
        finish()
    }

    fun onClickBar(v: View) {
        supportFragmentManager.popBackStack()
        when (v.id) {
            R.id.btnBarHome -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<LearnMainFragment>(R.id.FragmentContainer)
                }
            }
            R.id.btnBarFriends -> {
            }
            R.id.btnBarAdd -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<AddFragment>(R.id.FragmentContainer)
                }
            }
            R.id.btnBarProfile -> {
                if (thisUser == null || !readyPhoto ||  thisUser?.postList?.size != readyPost) {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<LoadingProfileFragment>(R.id.FragmentContainer)
                    }
                } else {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<ProfileFragment>(R.id.FragmentContainer)
                    }
                }
            }
        }

    }
}