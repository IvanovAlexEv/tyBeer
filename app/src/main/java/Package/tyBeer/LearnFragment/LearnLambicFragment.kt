package Package.tyBeer.LearnFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Package.tyBeer.R
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

class LearnLambicFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_learn_lambic, container, false)
        // SETTING INTESTAZIONE
        val percorso : TextView = v.findViewById(R.id.PercorsoLambicType)
        percorso.text = "${resources.getString(R.string.str_Percorso)} ${resources.getString(R.string.main)} / ${resources.getStringArray(R.array.MainTypeOfBeerList)[2]}"
        val TitleLambic: TextView = v.findViewById(R.id.TitleLambicType)
        TitleLambic.text = resources.getStringArray(R.array.MainTypeOfBeerList)[2]
        // SETTING DELLA LISTA
        val lista : ListView = v.findViewById(R.id.ListLambicType)
        lista.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, resources.getStringArray(R.array.LambicTypeBeerList))
        return v
    }

}