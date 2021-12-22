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
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.gawor.android.tayckner.R
import pl.gawor.android.tayckner.databinding.ItemHabitEventBinding
import pl.gawor.android.tayckner.databinding.ItemUpdateHabitEventBinding
import pl.gawor.android.tayckner.model.Habit
import pl.gawor.android.tayckner.model.HabitEvent
import pl.gawor.android.tayckner.model.ResponseModel
import pl.gawor.android.tayckner.repository.HabitEventRepository
import pl.gawor.android.tayckner.repository.JWT_TOKEN
import pl.gawor.android.tayckner.service.HabitEventApi
import pl.gawor.android.tayckner.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HabitEventAdapter(val context: Context) : RecyclerView.Adapter<HabitEventAdapter.HabitEventViewHolder>() {
    inner class HabitEventViewHolder(val binding: ItemHabitEventBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.cardView.setOnClickListener {
                popUpMenu(context, binding.root)
            }
        }

        private fun popUpMenu(context: Context, view: View) {
            Log.i(TAG, "HabitEventAdapter.popUpMenu()")
            val item = habitEvents[adapterPosition]
            val popupMenus = PopupMenu(context, view)
            popupMenus.inflate(R.menu.menu_item_habit_event)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.editText -> {
                        val bindingAddHabitEvent = ItemUpdateHabitEventBinding.inflate(LayoutInflater.from(context))

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
                            sendHabitEventsUpdateRequest(item.id, editTextHabitId, editTextDate, editTextComment, editTextValue)
                            Thread.sleep(500)
                            sendHabitEventsListRequest()
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
                        sendHabitEventsDeleteRequest(item.id)
                        Thread.sleep(500)
                        sendHabitEventsListRequest()
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
        val binding = ItemHabitEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HabitEventViewHolder(binding)
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

    private  fun sendHabitEventsUpdateRequest(habitEventId: Int, editTextHabitId: EditText, editTextDate: EditText, editTextComment: EditText, editTextValue: EditText) {
        Log.i(TAG, "HabitEventAdapter.sendHabitEventsUpdateRequest()")
        val habitId = editTextHabitId.text.toString().toLong()
        val date = editTextDate.text.toString()
        val comment = editTextComment.text.toString()
        val value = editTextValue.text.toString().toInt()
        val habit = Habit(habitId,"","", null)
        val habitEvent = HabitEvent(comment, date, habit, 0, value)

        CoroutineScope(Dispatchers.IO).launch {
            HabitEventRepository.update(habitEvent, habitEventId)
        }
    }

    private fun sendHabitEventsListRequest() {
        Log.i(TAG, "HabitEventAdapter.sendHabitEventsListRequest()")
        CoroutineScope(Dispatchers.IO).launch {
            val list :List<HabitEvent> = HabitEventRepository.list()
            habitEvents = list
        }
    }

    private fun sendHabitEventsDeleteRequest(habitEventId: Int) {
        Log.i(TAG, "HabitEventAdapter.sendHabitEventsDeleteRequest()")

        val habitEventApiClient: HabitEventApi = RetrofitInstance.retrofit.create(HabitEventApi::class.java)

        val call: Call<ResponseModel<Any>> = habitEventApiClient.deleteCall(JWT_TOKEN, habitEventId)
        call.enqueue(object : Callback<ResponseModel<Any>> {
            override fun onFailure(call: Call<ResponseModel<Any>>?, t: Throwable?) {
                Log.i(TAG, "HabitEventAdapter.sendHabitEventsListRequest():\t\tCall failed: ${t?.message}")
            }
            override fun onResponse(call: Call<ResponseModel<Any>>?, response: Response<ResponseModel<Any>>?) {
                Log.i(TAG, "HabitEventAdapter.sendHabitEventsListRequest():\t\tCall success: response.body = ${response?.body()}")
                val res = response?.body()

                when (res?.code) {
                    "XxX0" -> Toast.makeText(context, "Item deleted", Toast.LENGTH_LONG).show()
                    else -> Toast.makeText(context, res?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}