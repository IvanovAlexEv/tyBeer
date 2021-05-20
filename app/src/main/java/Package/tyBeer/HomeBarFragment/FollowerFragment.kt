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

class FollowerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_follower, container, false)

        val listViewSearchFollower = v.findViewById<ListView>(R.id.ListViewSearchFollower)
        val listaFollower : MutableList<User> = ArrayList()
        // CREAZIONE LISTA DEI PROPRI FOLLOWER
        for (user : User in HomeActivity.UserList.values){
            if ( HomeActivity.thisUser!!.followerListU.contains(user.id) )
                listaFollower.add(user)
        }
        listViewSearchFollower.adapter = AdapterUser(requireContext(), listaFollower)
        listViewSearchFollower.setOnItemClickListener { parent, view, position, id ->
            HomeActivity.SELECTEDUSERID = listaFollower[position].id
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<ReadSearchFragment>(R.id.FragmentContainer).addToBackStack(null)
            }
        }
        // GESTIONE DELLA RICERCA DEI FOLLOWER
        val btnFilterFollower = v.findViewById<Button>(R.id.btnFilterFollower)
        val textFilterFollower = v.findViewById<EditText>(R.id.editTextFilterFollower)
        btnFilterFollower.setOnClickListener {
            val testoDiRicerca: String = textFilterFollower.text.toString()
            textFilterFollower.setText("")
            val listaFollowerFilter : MutableList<User> = ArrayList()
            for (user : User in HomeActivity.UserList.values){
                if ( HomeActivity.thisUser!!.followerListU.contains(user.id) && ( user.nicknameU.contains(testoDiRicerca) || user.nameU.contains(testoDiRicerca) || user.surnameU.contains(testoDiRicerca) || user.emailU.contains(testoDiRicerca) )   )
                    listaFollowerFilter.add(user)
            }
            listViewSearchFollower.adapter = AdapterUser(requireContext(), listaFollowerFilter)
            listViewSearchFollower.setOnItemClickListener { parent, view, position, id ->
                HomeActivity.SELECTEDUSERID = listaFollowerFilter[position].id
                requireActivity().supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<ReadSearchFragment>(R.id.FragmentContainer).addToBackStack(null)
                }
            }
        }
        return v
    }

}