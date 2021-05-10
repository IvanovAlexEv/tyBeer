package Package.tyBeer.LearnFragment

import Package.tyBeer.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.commit
import androidx.fragment.app.replace

class LearnLagerFragment : Fragment(R.layout.fragment_learn_lager) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v =inflater.inflate(R.layout.fragment_learn_lager, container, false)

        val TitleLager: TextView = v.findViewById(R.id.TitleLagerType)
        TitleLager.text = resources.getStringArray(R.array.MainTypeOfBeerList)[0]
        val subTitleLager: TextView = v.findViewById(R.id.SubtitleLagerType)
        subTitleLager.text = resources.getString(R.string.bassaFerm)
        val category: TextView = v.findViewById(R.id.textCategory)
        category.text = resources.getString(R.string.main)
        category.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<LearnMainFragment>(R.id.FragmentContainer)
            }
        }

        val lista : ListView = v.findViewById(R.id.ListLagerType)
        lista.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, resources.getStringArray(R.array.LagerTypeBeerList))

        return v
    }

}