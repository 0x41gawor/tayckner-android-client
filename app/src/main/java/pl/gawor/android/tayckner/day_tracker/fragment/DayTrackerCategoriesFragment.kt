package pl.gawor.android.tayckner.day_tracker.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.gawor.android.tayckner.R
import pl.gawor.android.tayckner.common.util.SharedPrefManager
import pl.gawor.android.tayckner.databinding.DayTrackerDialogAddCategoryBinding
import pl.gawor.android.tayckner.databinding.DayTrackerFragmentCategoriesBinding
import pl.gawor.android.tayckner.day_tracker.adapter.CategoryAdapter
import pl.gawor.android.tayckner.day_tracker.model.Category
import pl.gawor.android.tayckner.day_tracker.repository.CategoryRepository
import pl.gawor.android.tayckner.day_tracker.repository.CategoryRepositoryDB
import java.util.*


class DayTrackerCategoriesFragment : Fragment() {

    private  val TAG = "TAYCKNER"

    private lateinit var binding: DayTrackerFragmentCategoriesBinding

    private lateinit var categoriesAdapter: CategoryAdapter

    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DayTrackerFragmentCategoriesBinding.inflate(layoutInflater)
        setupCategoriesRecyclerView()

        Log.i(tag, "activity = ${requireActivity()}")

        repository.init(requireContext())

        repository.refreshCategoriesList()

        binding.imageButtonOptions.setOnClickListener {
            optionsMenu(binding.imageButtonOptions)
        }
        binding.imageButtonDayPlanner.setOnClickListener {
            findNavController().navigate(R.id.action_dayTrackerCategoriesFragment_to_dayPlannerFragment)
        }
        binding.imageButtonDayTracker.setOnClickListener {
            findNavController().navigate(R.id.action_dayTrackerCategoriesFragment_to_dayTrackerFragment)
        }
        binding.imageButtonHabitTracker.setOnClickListener {
            findNavController().navigate(R.id.action_dayTrackerCategoriesFragment_to_habitTrackerFragment)
        }

        binding.imageButtonAdd.setOnClickListener {
            addCategory()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        repository.refreshCategoriesList()
    }

    private fun optionsMenu(view: View) {
        val optionsMenu = PopupMenu(context, view)
        optionsMenu.inflate(R.menu.habit_tracker_menu_top_bar_options)
        optionsMenu.setOnMenuItemClickListener{
            when(it.itemId) {
                R.id.logout -> {
                    SharedPrefManager.logout(this)
                    findNavController().navigate(R.id.action_dayTrackerCategoriesFragment_to_loginFragment)
                    true}
                else -> {true}
            }
        }
        optionsMenu.show()
    }

    private fun setupCategoriesRecyclerView()= binding.recyclerView.apply {
        categoriesAdapter = CategoryAdapter(context)
        adapter = categoriesAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun addCategory() {
        Log.i(TAG, "DayTrackerCategoriesFragment.addCategory()")

        val bindingAddCategory = DayTrackerDialogAddCategoryBinding.inflate(layoutInflater)

        val editTextName = bindingAddCategory.editTextName
        val editTextDescription = bindingAddCategory.editTextDescription
        val editTextColor = bindingAddCategory.editTextColor

        val dialogAddCategory = AlertDialog.Builder(context)

        dialogAddCategory.setView(bindingAddCategory.root)

        dialogAddCategory.setPositiveButton("Add") {
                dialog,_->
            repository.sendCategoryCreateRequest(editTextName, editTextDescription, editTextColor)
            Thread.sleep(500)
            repository.refreshCategoriesList()
            dialog.dismiss()
        }
        dialogAddCategory.setNegativeButton("Cancel") {
                dialog,_->
            Toast.makeText(context, "Adding canceled", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialogAddCategory.create()
        dialogAddCategory.show()
        Log.e(TAG, "DayTrackerCategoriesFragment.addCategory() = void")
    }

    inner class Repository {
        private var categoryRepository: CategoryRepositoryDB? = null
//        private val categoryRepository = CategoryRepository()

        fun init(context: Context) {
            categoryRepository = CategoryRepositoryDB(context)
            Log.i(tag, "context = $context")
        }

        fun refreshCategoriesList() {
            Log.i(TAG, "DayTrackerCategoriesFragment.Repository.refreshCategoriesList()")
            lifecycleScope.launchWhenCreated {
                var list: List<Category> = categoryRepository!!.list()
                Toast.makeText(context, "list = $list", Toast.LENGTH_LONG).show()
                categoriesAdapter.categories = list
                return@launchWhenCreated
            }
            Log.i(TAG, "DayTrackerFragment.Repository.refreshCategoriesList() = void")
        }

        fun sendCategoryCreateRequest(editTextName: EditText, editTextDescription: EditText, editTextColor: EditText) {
            Log.i(TAG, "CategoryAdapter.sendCategoriesUpdateRequest()")
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()
            val color = editTextColor.text.toString()

            val category = Category(0, name, description, color, null)
            lifecycleScope.launchWhenCreated {
                categoryRepository!!.create(category)
                return@launchWhenCreated
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DayTrackerCategoriesFragment().apply {
            }
    }
}