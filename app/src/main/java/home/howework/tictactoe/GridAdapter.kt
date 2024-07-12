package home.howework.tictactoe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class GridAdapter(context: Context,   private val tilesor: ArrayList<GridViewModal>) : BaseAdapter() {

    var tiles=tilesor
    val mContext=context
    private var layoutInflater: LayoutInflater? = null
    private lateinit var tilesTV: TextView

    fun clearAdapter(){
        tiles.clear()
    }
    fun addingElement(elements:ArrayList<GridViewModal>){
        tiles=elements
    }

    override fun getCount(): Int {
        return tiles.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView

        if (layoutInflater == null) {
            layoutInflater =
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.gridview_item, null)
        }
        tilesTV = convertView!!.findViewById(R.id.idTile)

        tilesTV.setText(tiles[position].shapeNumber.toString())
        // at last we are returning our convert view.
        return convertView!!
    }
}