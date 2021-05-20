package Package.tyBeer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AdapterUser(private val context: Context, private val data: MutableList<User>) : BaseAdapter(){

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var newView = convertView
        if(newView==null)
            newView = LayoutInflater.from(context).inflate(R.layout.user,parent,false)
        if (newView!=null){
            val nick = newView.findViewById<TextView>(R.id.textNicknameL)
            val NameSurname = newView.findViewById<TextView>(R.id.textNameSurnameL)
            val email = newView.findViewById<TextView>(R.id.textEmailL)
            nick.text= data[position].nicknameU
            NameSurname.text= "${data[position].nameU} ${data[position].surnameU}"
            email.text = data[position].emailU
        }
        return newView!!
    }
}