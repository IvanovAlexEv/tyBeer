package Package.tyBeer.HomeBarFragment

import Package.tyBeer.HomeActivity
import Package.tyBeer.Post
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Package.tyBeer.R
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ReadPostFragment : Fragment() {

    // POST SELEZIONATO DA VISUALIZZARE DEL PROPRIO PROFILO
    private lateinit var postRead : Post

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
        // RECUPERO DEL POST SELEZIONATO
        postRead = HomeActivity.thisUser!!.postList[ProfileFragment.SELECTEDPOST!!]
        // SETTING DEI DATI DEL POST
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
        val btnDeletePost = v.findViewById<Button>(R.id.btnDeletePost)
        btnDeletePost.setOnClickListener { deleteConfirm() }
        return v
    }

    // FUNZIONE PER ELIMINARE IL POST (DA FIREBASE)
    private fun deleteConfirm() {
        // APERTURA DI UN DIALOG PER LA CONFERMA
        val DialogConfirm = AlertDialog.Builder(requireContext())
        DialogConfirm.setTitle(R.string.str_ConfirmDeletePostTitle)
        DialogConfirm.setMessage(R.string.str_ConfirmDeletePostDesc)
        DialogConfirm.setPositiveButton(R.string.str_DeletePost, DialogInterface.OnClickListener { dialog, which ->
            HomeActivity.thisUser!!.postList.remove(postRead)
            HomeActivity.thisUser!!.nPost--
            val PostRef = FirebaseStorage.getInstance().getReference("PhotoPost/${HomeActivity.thisUser!!.id}/${postRead.idPost}")
            PostRef.delete().addOnSuccessListener {
                Toast.makeText(requireContext(), R.string.str_OkDeletePost , Toast.LENGTH_SHORT).show();
                val userRef = FirebaseDatabase.getInstance().getReference("users")
                userRef.child(HomeActivity.idUser!!).setValue(HomeActivity.thisUser)
            }
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<LoadingProfileFragment>(R.id.FragmentContainer)
            }
        })
        DialogConfirm.setNegativeButton(R.string.str_Chiudi, DialogInterface.OnClickListener { dialog, which ->})
        DialogConfirm.create().show()
    }

}