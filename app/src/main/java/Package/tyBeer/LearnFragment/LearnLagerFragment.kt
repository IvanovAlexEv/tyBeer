package Package.tyBeer.LearnFragment

import Package.tyBeer.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class LearnLagerFragment : Fragment(R.layout.fragment_learn_lager) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v =inflater.inflate(R.layout.fragment_learn_lager, container, false)
        // SETTING INTESTAZIONE
        val percorso : TextView = v.findViewById(R.id.PercorsoLagerType)
        percorso.text = "${resources.getString(R.string.str_Percorso)} ${resources.getString(R.string.main)} / ${resources.getStringArray(R.array.MainTypeOfBeerList)[0]}"
        val TitleLager: TextView = v.findViewById(R.id.TitleLagerType)
        TitleLager.text = resources.getStringArray(R.array.MainTypeOfBeerList)[0]
        // SETTING DELLA LISTA
        val lista : ListView = v.findViewById(R.id.ListLagerType)
        lista.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, resources.getStringArray(R.array.LagerTypeBeerList))
        return v
    }

}