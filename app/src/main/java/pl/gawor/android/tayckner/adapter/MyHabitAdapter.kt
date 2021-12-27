package pl.gawor.android.tayckner.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.gawor.android.tayckner.R
import pl.gawor.android.tayckner.databinding.ItemMyHabitBinding
import pl.gawor.android.tayckner.model.Habit
import pl.gawor.android.tayckner.repository.HabitEventRepository
import pl.gawor.android.tayckner.repository.HabitRepository

class MyHabitAdapter(val context: Context) : RecyclerView.Adapter<MyHabitAdapter.HabitViewHolder>() {

    inner class HabitViewHolder(val binding: ItemMyHabitBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cardView.setOnClickListener {
                popUpMenu(context, binding.root)
            }
        }

        private fun popUpMenu(context: Context, view: View) {
            Log.i(TAG, "HabitEventAdapter.popUpMenu()")
            val item = habits[adapterPosition]
            val popupMenus = PopupMenu(context, view)
            popupMenus.inflate(R.menu.menu_item_habit)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.editText -> {
                        Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.delete -> {
                        Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> true
                }
            }
            popupMenus.show()
        }
    }

    private  val TAG = "TAYCKNER"


    private val diffCallback = object : DiffUtil.ItemCallback<Habit>() {
        override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var habits: List<Habit>
        get() = differ.currentList
        set(value) {differ.submitList(value)}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder(
            ItemMyHabitBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.binding.apply {
            val habit = habits[position]
            textViewHabit.text = habit.name
            cardView.background.current.setTint(Color.parseColor(habit.color))
        }
    }

    override fun getItemCount(): Int {
        return habits.size
    }


    inner class Repository {
        fun sendHabitsUpdateRequest(editTextName: EditText, editTextColor: EditText, habitId: Int) {
            Log.i(TAG, "HabitAdapter.Repository.sendHabitsUpdateRequest()")
            val name = editTextName.text.toString()
            val color = editTextColor.text.toString()
            val habit = Habit(habitId.toLong(), name, color, null)
            CoroutineScope(Dispatchers.IO).launch {
                HabitRepository.update(habit, habitId)
            }
        }

        fun sendHabitsDeleteRequest(habitId: Int) {
            Log.i(TAG, "HabitAdapter.Repository.sendHabitsDeleteRequest()")
            CoroutineScope(Dispatchers.IO).launch {
                HabitRepository.delete(habitId)
            }
        }
    }

}