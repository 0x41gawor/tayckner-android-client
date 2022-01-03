package pl.gawor.android.tayckner.habit_tracker.adapter

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
import pl.gawor.android.tayckner.common.util.Util
import pl.gawor.android.tayckner.databinding.HabitTrackerItemHabitEventBinding
import pl.gawor.android.tayckner.databinding.HabitTrackerItemUpdateHabitEventBinding
import pl.gawor.android.tayckner.habit_tracker.model.Habit
import pl.gawor.android.tayckner.habit_tracker.model.HabitEvent
import pl.gawor.android.tayckner.habit_tracker.repository.HabitEventRepository

class HabitEventAdapter(val context: Context) : RecyclerView.Adapter<HabitEventAdapter.HabitEventViewHolder>() {
    inner class HabitEventViewHolder(val binding: HabitTrackerItemHabitEventBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.cardView.setOnClickListener {
                popUpMenu(context, binding.root)
            }
        }

        private val repository = Repository()

        private fun popUpMenu(context: Context, view: View) {
            Log.i(TAG, "HabitEventAdapter.popUpMenu()")
            val item = habitEvents[adapterPosition]
            val popupMenus = PopupMenu(context, view)
            popupMenus.inflate(R.menu.habit_tracker_menu_item_habit_event)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.editText -> {
                        val bindingAddHabitEvent = HabitTrackerItemUpdateHabitEventBinding.inflate(LayoutInflater.from(context))

                        val editTextHabitId = bindingAddHabitEvent.editTextHabit
                        val editTextDate = bindingAddHabitEvent.editTextDate
                        val editTextComment = bindingAddHabitEvent.editTextComment
                        val editTextValue = bindingAddHabitEvent.editTextValue

                        editTextHabitId.setText(item.habit.id.toString())
                        editTextDate.setText(item.date)
                        editTextComment.setText(item.comment)
                        editTextValue.setText(item.value.toString())

                        val dialogAddHabitEvent = AlertDialog.Builder(context)

                        dialogAddHabitEvent.setView(bindingAddHabitEvent.root)

                        dialogAddHabitEvent.setPositiveButton("Update") {
                                dialog,_->
                            repository.sendHabitEventsUpdateRequest(item.id, editTextHabitId, editTextDate, editTextComment, editTextValue)
                            Thread.sleep(500)
                            repository.sendHabitEventsListRequest()
                            dialog.dismiss()
                        }
                        dialogAddHabitEvent.setNegativeButton("Cancel") {
                                dialog,_->
                            dialog.dismiss()
                        }
                        dialogAddHabitEvent.create()
                        dialogAddHabitEvent.show()
                        true
                    }
                    R.id.delete -> {
                        repository.sendHabitEventsDeleteRequest(item.id)
                        Thread.sleep(500)
                        repository.sendHabitEventsListRequest()
                        true
                    }
                    else -> true
                }
            }
            popupMenus.show()
        }
    }

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
        val binding = HabitTrackerItemHabitEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitEventViewHolder, position: Int) {
        holder.binding.apply {
            val habitEvent = habitEvents[position]
            textViewHabitName.text = habitEvent.habit.name
            val date = habitEvent.date.substring(8, 10) + " " + Util.convertMonth(habitEvent.date.substring(5, 7))
            textViewDate.text =  date
            textViewYear.text = habitEvent.date.substring(0,4)
            textViewComment.text = habitEvent.comment
            frameLayoutHabitName.background.current.setTint(Color.parseColor((habitEvent.habit.color)))
        }
    }

    override fun getItemCount(): Int {
        return habitEvents.size
    }

    inner class Repository {
        private val habitEventRepository = HabitEventRepository()

        fun sendHabitEventsUpdateRequest(habitEventId: Int, editTextHabitId: EditText, editTextDate: EditText, editTextComment: EditText, editTextValue: EditText) {
            Log.i(TAG, "HabitEventAdapter.sendHabitEventsUpdateRequest()")
            val habitId = editTextHabitId.text.toString().toLong()
            val date = editTextDate.text.toString()
            val comment = editTextComment.text.toString()
            val value = editTextValue.text.toString().toInt()
            val habit = Habit(habitId,"","", null)
            val habitEvent = HabitEvent(comment, date, habit, 0, value)

            CoroutineScope(Dispatchers.IO).launch {
                habitEventRepository.update(habitEvent, habitEventId)
            }
        }

        fun sendHabitEventsListRequest() {
            Log.i(TAG, "HabitEventAdapter.sendHabitEventsListRequest()")
            CoroutineScope(Dispatchers.IO).launch {
                val list :List<HabitEvent> = habitEventRepository.list()
                habitEvents = list
            }
        }

        fun sendHabitEventsDeleteRequest(habitEventId: Int) {
            Log.i(TAG, "HabitEventAdapter.sendHabitEventsDeleteRequest()")
            CoroutineScope(Dispatchers.IO).launch {
                habitEventRepository.delete(habitEventId)
            }
        }
    }
}