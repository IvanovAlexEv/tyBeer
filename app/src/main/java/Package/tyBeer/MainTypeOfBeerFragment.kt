package Package.tyBeer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment


class MainTypeOfBeerFragment : Fragment(R.layout.fragment_main_type_of_beer) {

    //lateinit var ThisContext:Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_type_of_beer, container, false)



        //ListMainTypeOfBeer.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, resources.getStringArray(R.array.MainTypeOfBeerList));
    }

    // PER USARE, DOVE C'è IL CAMBIO
    /*
    supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace<codeFragment>(R.id.fragmentSuLayout)
    }
     */
}

/*
class myAdapter(val context : Context, val data : Array<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var newView = convertView
        if (newView==null)
            newView = LayoutInflater.from(context).inflate(R.layout.row, parent, false)
        if (newView!=null){
            val nome : TextView = newView.findViewById(R.id.textViewNome)
            val cognome : TextView = newView.findViewById(R.id.textViewCognome)
            val posizione : TextView = newView.findViewById(R.id.textViewN)
            val parts = data[position].split(" ")
            nome.text = parts[0]
            cognome.text = parts[1]

            posizione.text = (position+1).toString()

        }
        return newView
    }
}
*/
