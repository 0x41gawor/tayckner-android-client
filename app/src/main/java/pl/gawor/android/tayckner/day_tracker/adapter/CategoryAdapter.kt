package pl.gawor.android.tayckner.day_tracker.adapter

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
import pl.gawor.android.tayckner.databinding.DayTrackerDialogUpdateCategoryBinding
import pl.gawor.android.tayckner.databinding.DayTrackerItemCategoryBinding
import pl.gawor.android.tayckner.day_tracker.model.Category
import pl.gawor.android.tayckner.day_tracker.repository.CategoryRepository
import pl.gawor.android.tayckner.day_tracker.repository.CategoryRepositoryDB

class CategoryAdapter(val context: Context) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(val binding: DayTrackerItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cardView.setOnClickListener {
                popUpMenu(context, binding.root)
            }
        }

        private val repository = Repository()

        private fun popUpMenu(context: Context, view: View) {
            Log.i(TAG, "CategoryAdapter.popUpMenu()")
            val item = categories[adapterPosition]
            val popupMenus = PopupMenu(context, view)
            popupMenus.inflate(R.menu.common_menu_item)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.editText -> {
                        val bindingUpdateCategory = DayTrackerDialogUpdateCategoryBinding.inflate(
                            LayoutInflater.from(context))

                        val editTextName = bindingUpdateCategory.editTextName
                        val editTextDescription = bindingUpdateCategory.editTextDescription
                        val editTextColor = bindingUpdateCategory.editTextColor

                        editTextName.setText(item.name)
                        editTextDescription.setText(item.description)
                        editTextColor.setText(item.color)

                        val dialogUpdateCategory = AlertDialog.Builder(context)

                        dialogUpdateCategory.setView(bindingUpdateCategory.root)

                        dialogUpdateCategory.setPositiveButton("Update") {
                                dialog,_->
                            repository.sendCategoriesUpdateRequest(item.id, editTextName, editTextDescription, editTextColor)
                            Thread.sleep(500)
                            repository.sendCategoriesListRequest()
                            dialog.dismiss()
                        }
                        dialogUpdateCategory.setNegativeButton("Cancel") {
                                dialog,_->
                            dialog.dismiss()
                        }
                        dialogUpdateCategory.create()
                        dialogUpdateCategory.show()
                        true
                    }
                    R.id.delete -> {
                        repository.sendCategoriesDeleteRequest(item.id)
                        Thread.sleep(500)
                        repository.sendCategoriesListRequest()
                        true
                    }
                    else -> true
                }
            }
            popupMenus.show()
        }
    }

    private  val TAG = "TAYCKNER"

    private val diffCallback = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var categories: List<Category>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = DayTrackerItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.apply {
            val category = categories[position]
            textViewName.text = category.name
            textViewDescription.text = category.description

            linearLayout.background.current.setTint(Color.parseColor(category.color))
        }
    }

    inner class Repository {
        private val categoriesRepository = CategoryRepositoryDB(context)

        fun sendCategoriesUpdateRequest(id: Int, editTextName: EditText, editTextDescription: EditText, editTextColor: EditText) {
            Log.i(TAG, "CategoryAdapter.sendCategoriesUpdateRequest()")
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()
            val color = editTextColor.text.toString()

            val category = Category(id, name, description, color, null)
            CoroutineScope(Dispatchers.IO).launch {
              //  categoriesRepository.update(category, id)
            }
        }

        fun sendCategoriesListRequest() {
            Log.i(TAG, "CategoryAdapter.sendCategoriesListRequest()")
            CoroutineScope(Dispatchers.IO).launch {
                val list :List<Category> = categoriesRepository.list()
                categories = list
            }
        }

        fun sendCategoriesDeleteRequest(id: Int) {
            Log.i(TAG, "CategoryAdapter.sendCategoriesDeleteRequest()")
            CoroutineScope(Dispatchers.IO).launch {
                categoriesRepository.delete(id)
            }
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

}