package pl.gawor.android.tayckner.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.gawor.android.tayckner.R
import pl.gawor.android.tayckner.databinding.ItemHabitEventBinding
import pl.gawor.android.tayckner.model.HabitEvent

class HabitEventAdapter(val context: Context) : RecyclerView.Adapter<HabitEventAdapter.HabitEventViewHolder>() {
    inner class HabitEventViewHolder(val binding: ItemHabitEventBinding) : RecyclerView.ViewHolder(binding.root)

    private  val TAG = "TAYCKNER"

    private val diffCallback = object : DiffUtil.ItemCallback<HabitEvent>() {
        override fun areItemsTheSame(oldItem: HabitEvent, newItem: HabitEvent): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HabitEvent, newItem: HabitEvent): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var habitEvents: List<HabitEvent>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitEventViewHolder {
        val binding = ItemHabitEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.cardView.setOnClickListener {
            Log.i("TAYCKNER", "ELOELO")
            popUpMenu(context, binding.root)
        }
        return HabitEventViewHolder(binding)
    }

    private fun popUpMenu(context: Context, view: View) {
        Log.i(TAG, "HabitEventAdapter.popUpMenu()")
        val popupMenus = PopupMenu(context, view)
        popupMenus.inflate(R.menu.menu_item_habit_event)
        popupMenus.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.editText -> {
                    Toast.makeText(context, "Edit option is clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.delete -> {
                    Toast.makeText(context, "Delete option is clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> true
            }
        }
        popupMenus.show()
    }

    override fun onBindViewHolder(holder: HabitEventViewHolder, position: Int) {
        holder.binding.apply {
            val habitEvent = habitEvents[position]
            textViewHabitName.text = habitEvent.habit.name
            val date = habitEvent.date.substring(8, 10) + " " + convertMonth(habitEvent.date.substring(5, 7))
            textViewDate.text =  date
            textViewYear.text = habitEvent.date.substring(0,4)
            textViewComment.text = habitEvent.comment
            frameLayoutHabitName.background.current.setTint(Color.parseColor((habitEvent.habit.color)))
        }
    }

    override fun getItemCount(): Int {
        return habitEvents.size
    }

    private fun convertMonth(number: String): String {
            return when(number) {
                "01" -> "JAN"
                "02" -> "FEB"
                "03" -> "MAR"
                "04" -> "APR"
                "05" -> "MAY"
                "06" -> "JUN"
                "07" -> "JUL"
                "08" -> "AUG"
                "09" -> "SEP"
                "10" -> "OCT"
                "11" -> "NOV"
                "12" -> "DEC"
                else -> "XXX"
            }
    }
}