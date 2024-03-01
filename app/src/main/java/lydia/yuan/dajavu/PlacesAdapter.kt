package lydia.yuan.dajavu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lydia.yuan.dajavu.network.Place

class PlacesAdapter(private val mList: List<Place>) : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item_place, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // sets the text to the textview from our itemHolder class
        holder.displayNameTextView.text = mList[position].displayName.text
        holder.addressTextView.text = mList[position].formattedAddress
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val displayNameTextView: TextView = itemView.findViewById(R.id.displayName)
        val addressTextView: TextView = itemView.findViewById(R.id.formattedAddress)
    }
}