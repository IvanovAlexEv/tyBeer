package Package.tyBeer

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import java.io.File

class AdapterPost(private val context: Context, private val data: MutableList<File?>) : BaseAdapter(){

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
            newView = LayoutInflater.from(context).inflate(R.layout.post,parent,false)
        if (newView!=null){
            val element = newView.findViewById<ImageView>(R.id.imageProfilePost)
            element.setImageBitmap(BitmapFactory.decodeFile(data[data.size - position - 1]?.path))
        }
        return newView!!
    }
}