package Package.tyBeer.HomeBarFragment

import Package.tyBeer.AdapterUser
import Package.tyBeer.HomeActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Package.tyBeer.R
import Package.tyBeer.User
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {

    // FRAGMENT PER LA RICERCA DEGLI UTENTI REGISTRATI (MA CHE NON SEGUI)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_search, container, false)
        val listViewSearch = v.findViewById<ListView>(R.id.ListViewSearch)
        // CREAZIONE LISTA UTENTI REGISTRATI CHE NON SEGUI
        val listautenti : MutableList<User> = ArrayList()
        for (user : User in HomeActivity.UserList.values){
            if ( !(HomeActivity.thisUser!!.friendsListU.contains(user.id)) )
                listautenti.add(user)
        }
        listViewSearch.adapter = AdapterUser(requireContext(), listautenti)
        listViewSearch.setOnItemClickListener { parent, view, position, id ->
            HomeActivity.SELECTEDUSERID = listautenti[position].id
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<ReadSearchFragment>(R.id.FragmentContainer).addToBackStack(null)
            }
        }
        // GESTIONE DELLA RICERCA DEGLI UTENTI
        val btnFilter = v.findViewById<Button>(R.id.btnFilter)
        val textFilter = v.findViewById<EditText>(R.id.editTextFilter)
        btnFilter.setOnClickListener {
            val testoDiRicerca: String = textFilter.text.toString()
            textFilter.setText("")
            val listautentiFilter : MutableList<User> = ArrayList()
            for (user : User in HomeActivity.UserList.values){
                if ( !(HomeActivity.thisUser!!.friendsListU.contains(user.id)) && ( user.nicknameU.contains(testoDiRicerca) || user.nameU.contains(testoDiRicerca) || user.surnameU.contains(testoDiRicerca) || user.emailU.contains(testoDiRicerca) )   )
                    listautentiFilter.add(user)
            }
            listViewSearch.adapter = AdapterUser(requireContext(), listautentiFilter)
            listViewSearch.setOnItemClickListener { parent, view, position, id ->
                HomeActivity.SELECTEDUSERID = listautenti[position].id
                requireActivity().supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<ReadSearchFragment>(R.id.FragmentContainer).addToBackStack(null)
                }
            }
        }
        return v
    }

}