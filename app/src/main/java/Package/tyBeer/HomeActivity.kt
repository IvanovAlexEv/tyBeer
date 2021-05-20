package Package.tyBeer

import Package.tyBeer.HomeBarFragment.AddFragment
import Package.tyBeer.HomeBarFragment.FriendsFragment
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

// ACTIVITY PRINCIPALE CONTENENTE TUTT I DATI NECESSARI PER IL CORRETTO FUNZIONAMENTO

class HomeActivity : AppCompatActivity() {

    private var exit = 0
    lateinit var auth : FirebaseAuth

    companion object{
        // VARIABILI STATICHE CHE SERVONO PER MANTENERE COSTANTEMENTE AGGIORNATI I DATI DEL DATABASE (FIREBASE)
        //                      RICHIAMABILI NEI FARI FRAGMENT DOVE OCCORRE
        var idUser : String? = null
        var thisUser: User? = null
        var thisPhotoProfile : File? = File.createTempFile("${thisUser?.photoProfile}", "jpg")
        var thisPhotoPost : MutableList<File?> = ArrayList()
        var readyPhoto: Boolean = false
        var readyPost: Int = 0
        var UserList : HashMap<String, User> = HashMap()
        // UTENTE SELEZIONATO DA VISUALIZZARE NEI FRAGMENT DOVE OCCORRE
        var SELECTEDUSERID : String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        clearAll()
        // CARICAMENTO PRIMO FRAGMENT
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

    // PULIZIA DELLO STATO DELL'ACTIVITY
    private fun clearAll() {
        idUser = null
        thisUser = null
        thisPhotoProfile  = File.createTempFile("${thisUser?.photoProfile}", "jpg")
        thisPhotoPost  = ArrayList()
        readyPhoto = false
        readyPost = 0
        UserList = HashMap()
        SELECTEDUSERID = null
    }

    private fun recuperoDati() {
        // RECUPERO DATI UTENTE LOGGATO
        val refDB = FirebaseDatabase.getInstance().getReference("users")
        refDB.addChildEventListener( object : ChildEventListener {
            // AGGIUNTA UTENTE
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                // PROPRIO UTENTE
                if (snapshot.key.equals("$idUser")) {
                    updateProfile(snapshot)
                // ALTRI UTENTI
                } else {
                    val foundUser = snapshot.getValue(User::class.java)!!
                    UserList.put(foundUser.id, foundUser)
                }
            }
            // MODIFICA UTENTE
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // PROPRIO UTENTE
                if (snapshot.key.equals("$idUser")) {
                    updateProfile(snapshot)
                // ALTRI UTENTI
                } else {
                    val foundUser = snapshot.getValue(User::class.java)!!
                    UserList.remove(foundUser.id)
                    UserList.put(foundUser.id, foundUser)
                }
            }
            override fun onChildRemoved(snapshot: DataSnapshot) { }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }
            override fun onCancelled(error: DatabaseError) { }
            // FUNZIONE DI AGGIORNAMENTO DEL PROPRIO PROFILO
            private fun updateProfile(snapshot: DataSnapshot){
                thisUser = snapshot.getValue(User::class.java)
                readyPhoto=false
                val refStorage = FirebaseStorage.getInstance().getReference("PhotoProfile/${thisUser!!.photoProfile}")
                refStorage.getFile(thisPhotoProfile!!).addOnCompleteListener { readyPhoto=true }
                thisPhotoPost.clear()
                readyPost = readyPost - thisUser!!.postList.size
                for ((index, post) in thisUser!!.postList.withIndex()){
                    val filePost = File.createTempFile(post.idPost, "jpg")
                    thisPhotoPost.add(filePost)
                    val refStoragePost = FirebaseStorage.getInstance().getReference("PhotoPost/${thisUser!!.id}/${post.idPost}")
                    refStoragePost.getFile(filePost).addOnSuccessListener {
                        readyPost++
                        thisPhotoPost.removeAt(index)
                        thisPhotoPost.add(index, filePost)
                    }
                }

            }
        })
    }

    // CREAZIONE DEL MENU
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // GESTIONE DEL BOTTONE "BACK" DI ANDROID
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

    //LOGOUT
    fun onLogout(item: MenuItem){
        FirebaseAuth.getInstance().signOut();
        val intentHome = Intent(this, LoginActivity::class.java)
        startActivity(intentHome)
        finish()
    }

    // GESTIONE DELLA BARRA PRINCIPALE DELLE FUNZIONI
    fun onClickBar(v: View) {
        // NEL CAMBIAMENTO DI SEZIONE PULISCO IL BACKSTACK
        val setBack : Int = supportFragmentManager.backStackEntryCount
        for (i in 1..setBack){
            supportFragmentManager.popBackStack()

        }
        when (v.id) {
            // BOTTONE STUDIO BIRRA
            R.id.btnBarHome -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<LearnMainFragment>(R.id.FragmentContainer)
                }
            }
            // BOTTONE GESTIONE AMICI E UTENTI
            R.id.btnBarFriends -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<FriendsFragment>(R.id.FragmentContainer)
                }
            }
            // BOTTONE AGGIUNGI POST
            R.id.btnBarAdd -> {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<AddFragment>(R.id.FragmentContainer)
                }
            }
            // BOTTONE PROFILO
            R.id.btnBarProfile -> {
                // SE I DATI NON SONO PRONTI APRO IL CARICAMENTO
                if (thisUser == null || !readyPhoto ||  0 != readyPost) {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<LoadingProfileFragment>(R.id.FragmentContainer)
                    }
                // SE I DATI SONO PRONTI APRO IL PROFILO
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