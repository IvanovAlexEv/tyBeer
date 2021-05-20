package Package.tyBeer.HomeBarFragment

import Package.tyBeer.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.commit
import androidx.fragment.app.replace

class FriendsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_friends, container, false)
        setButton()

        // BOTTONE PER ANDARE A VISUALIZZARE TUTTI GLI UTENTI REGISTRATI
        val btnSearchPeople = v.findViewById<Button>(R.id.btnSearchPeople)
        btnSearchPeople.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<SearchFragment>(R.id.FragmentContainer).addToBackStack(null)
            }
        }
        // CREAZIONE LISTA DELLE PERSONE CHE SEGUI
        val listViewFriends = v.findViewById<ListView>(R.id.ListViewFriends)
        val listautenti : MutableList<User> = ArrayList()
        for (user : User in HomeActivity.UserList.values){
            if ( HomeActivity.thisUser!!.friendsListU.contains(user.id) )
                listautenti.add(user)
        }
        listViewFriends.adapter = AdapterUser(requireContext(), listautenti)
        listViewFriends.setOnItemClickListener { parent, view, position, id ->
            HomeActivity.SELECTEDUSERID = listautenti[position].id
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<ReadSearchFragment>(R.id.FragmentContainer).addToBackStack(null)
            }
        }
        // GESTIONE DELLA RICERCA DELLE PERSONE CHE SEGUI
        val btnFilterF = v.findViewById<Button>(R.id.btnFilterFriends)
        val textFilterF = v.findViewById<EditText>(R.id.editTextFilterFriends)
        btnFilterF.setOnClickListener {
            val testoDiRicerca: String = textFilterF.text.toString()
            textFilterF.setText("")
            val listautentiFilterF : MutableList<User> = ArrayList()
            for (user : User in HomeActivity.UserList.values){
                if ( HomeActivity.thisUser!!.friendsListU.contains(user.id) && ( user.nicknameU.contains(testoDiRicerca) || user.nameU.contains(testoDiRicerca) || user.surnameU.contains(testoDiRicerca) || user.emailU.contains(testoDiRicerca) )   )
                    listautentiFilterF.add(user)
            }
            listViewFriends.adapter = AdapterUser(requireContext(), listautentiFilterF)
            listViewFriends.setOnItemClickListener { parent, view, position, id ->
                HomeActivity.SELECTEDUSERID = listautenti[position].id
                requireActivity().supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<ReadSearchFragment>(R.id.FragmentContainer).addToBackStack(null)
                }
            }
        }
        return v
    }

    // SETTA LA BARRA DEL MENU'
    private fun setButton(){
        val btnHome = requireActivity().findViewById<ImageButton>(R.id.btnBarHome)
        val btnFriends = requireActivity().findViewById<ImageButton>(R.id.btnBarFriends)
        val btnAdd = requireActivity().findViewById<ImageButton>(R.id.btnBarAdd)
        val btnProfile = requireActivity().findViewById<ImageButton>(R.id.btnBarProfile)
        btnHome.setImageResource(R.drawable.ic_bar_home)
        btnFriends.setImageResource(R.drawable.ic_bar_friends_sel)
        btnAdd.setImageResource(R.drawable.ic_bar_add)
        btnProfile.setImageResource(R.drawable.ic_bar_profile)
    }

}