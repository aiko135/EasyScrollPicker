package ktepin.android.easyscrollpicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView

class Sample1Adapter(
//    private var elemWidth: Int
) : RecyclerView.Adapter<Sample1Adapter.SelectorViewHolder>() {

    private var items: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14 ,15, 16, 17)

    fun getItemAtPos(pos: Int): Int = items[pos]

    class SelectorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView

        init {
            text = view.findViewById(R.id.text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item_number, parent, false)

//        view.rootView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//            width = elemWidth
//        }

        val viewHolder = SelectorViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SelectorViewHolder, position: Int) {
        val item = items[position]
        holder.text.text = item.toString()
    }
}