package Package.tyBeer.HomeBarFragment

import Package.tyBeer.HomeActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Package.tyBeer.R
import android.widget.ImageButton
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import java.lang.IllegalStateException
import kotlin.concurrent.thread

class LoadingProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_loading_profile, container, false)
        setButton()
        thread{
            try {
                while ( HomeActivity.thisUser == null || !HomeActivity.readyPhoto || HomeActivity.thisUser?.postList?.size != HomeActivity.readyPost) {}
                requireActivity().runOnUiThread {
                    requireActivity().supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<ProfileFragment>(R.id.FragmentContainer)
                    }
                }
            } catch (e:IllegalStateException){}
        }
        return v
    }
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

}