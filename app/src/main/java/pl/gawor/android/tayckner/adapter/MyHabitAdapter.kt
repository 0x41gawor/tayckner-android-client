package pl.gawor.android.tayckner.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.gawor.android.tayckner.R
import pl.gawor.android.tayckner.databinding.ItemMyHabitBinding
import pl.gawor.android.tayckner.databinding.ItemUpdateHabitBinding
import pl.gawor.android.tayckner.model.Habit
import pl.gawor.android.tayckner.repository.HabitRepository

class MyHabitAdapter(val context: Context) : RecyclerView.Adapter<MyHabitAdapter.HabitViewHolder>() {

    inner class HabitViewHolder(val binding: ItemMyHabitBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cardView.setOnClickListener {
                popUpMenu(context, binding.root)
            }
        }

        private val repository = Repository()

        private fun popUpMenu(context: Context, view: View) {
            Log.i(TAG, "HabitEventAdapter.popUpMenu()")
            val item = habits[adapterPosition]
            val popupMenus = PopupMenu(context, view)
            popupMenus.inflate(R.menu.menu_item_habit)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.editText -> {
                        val bindingUpdateHabit = ItemUpdateHabitBinding.inflate(LayoutInflater.from(context))

                        val editTextName = bindingUpdateHabit.editTextName
                        val editTextColor = bindingUpdateHabit.editTextColor
                        val id = item.id

                        editTextName.setText(item.name)
                        editTextColor.setText(item.color)
                        val dialogUpdateHabit = AlertDialog.Builder(context)

                        dialogUpdateHabit.setView(bindingUpdateHabit.root)

                        dialogUpdateHabit.setPositiveButton("Update") {
                                dialog,_->
                            repository.sendHabitsUpdateRequest(editTextName, editTextColor, id)
                            Thread.sleep(500)
                            repository.sendHabitsListRequest()
                            dialog.dismiss()
                        }
                        dialogUpdateHabit.setNegativeButton("Cancel") {
                                dialog,_->
                            dialog.dismiss()
                        }
                        dialogUpdateHabit.create()
                        dialogUpdateHabit.show()
                        true
                    }
                    R.id.delete -> {
                        repository.sendHabitsDeleteRequest(item.id.toInt())
                        Thread.sleep(500)
                        repository.sendHabitsListRequest()
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
        private val habitRepository = HabitRepository()

        fun sendHabitsUpdateRequest(editTextName: EditText, editTextColor: EditText, habitId: Long) {
            Log.i(TAG, "HabitAdapter.Repository.sendHabitsUpdateRequest()")
            val name = editTextName.text.toString()
            val color = editTextColor.text.toString()
            val habit = Habit(habitId, name, color, null)
            CoroutineScope(Dispatchers.IO).launch {
                habitRepository.update(habit, habitId.toInt())
            }
        }

        fun sendHabitsDeleteRequest(habitId: Int) {
            Log.i(TAG, "HabitAdapter.Repository.sendHabitsDeleteRequest()")
            CoroutineScope(Dispatchers.IO).launch {
                habitRepository.delete(habitId)
            }
        }

        fun sendHabitsListRequest() {
            Log.i(TAG, "HabitAdapter.Repository.sendHabitsListRequest()")
            CoroutineScope(Dispatchers.IO).launch {
                val list :List<Habit> = habitRepository.list()
                habits = list
            }
        }
    }

}