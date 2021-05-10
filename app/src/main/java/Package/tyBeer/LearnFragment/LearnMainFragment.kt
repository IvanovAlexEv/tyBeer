package Package.tyBeer.LearnFragment

import Package.tyBeer.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.commit
import androidx.fragment.app.replace

class LearnMainFragment : Fragment(R.layout.fragment_learn_main) {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_learn_main, container, false)
        setButton()

        val lista : ListView = v.findViewById(R.id.ListMainType)
        lista.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, resources.getStringArray(R.array.MainTypeOfBeerList))
        lista.setOnItemClickListener { parent, view, position, id -> openFragment(position) }

        return v
    }

    fun setButton(){
        val btnHome = requireActivity().findViewById<ImageButton>(R.id.btnBarHome)
        val btnFriends = requireActivity().findViewById<ImageButton>(R.id.btnBarFriends)
        val btnAdd = requireActivity().findViewById<ImageButton>(R.id.btnBarAdd)
        val btnProfile = requireActivity().findViewById<ImageButton>(R.id.btnBarProfile)
        btnHome.setImageResource(R.drawable.ic_bar_home_sel)
        btnFriends.setImageResource(R.drawable.ic_bar_friends)
        btnAdd.setImageResource(R.drawable.ic_bar_add)
        btnProfile.setImageResource(R.drawable.ic_bar_profile)
    }

    private fun openFragment(click: Int) {
        if (click==0){
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<LearnLagerFragment>(R.id.FragmentContainer)
            }
        }
        if (click==1){
        }
        if (click==2){
        }
        if (click==3){
        }
    }
}