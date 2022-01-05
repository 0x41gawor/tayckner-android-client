package pl.gawor.android.tayckner.day_tracker.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import pl.gawor.android.tayckner.R
import pl.gawor.android.tayckner.databinding.DayPlannerFragmentMainBinding
import pl.gawor.android.tayckner.databinding.DayTrackerFragmentCategoriesBinding
import pl.gawor.android.tayckner.day_tracker.adapter.ActivityAdapter
import pl.gawor.android.tayckner.day_tracker.adapter.CategoryAdapter
import pl.gawor.android.tayckner.day_tracker.model.Activity
import pl.gawor.android.tayckner.day_tracker.model.Category
import pl.gawor.android.tayckner.day_tracker.repository.ActivityRepository
import pl.gawor.android.tayckner.day_tracker.repository.CategoryRepository
import java.util.*


class DayTrackerCategoriesFragment : Fragment() {

    private  val TAG = "TAYCKNER"

    private lateinit var binding: DayTrackerFragmentCategoriesBinding

    private lateinit var categoriesAdapter: CategoryAdapter

    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository.refreshCategoriesList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DayTrackerFragmentCategoriesBinding.inflate(layoutInflater)
        setupCategoriesRecyclerView()
        return binding.root
    }

    private fun setupCategoriesRecyclerView()= binding.recyclerView.apply {
        categoriesAdapter = CategoryAdapter(context)
        adapter = categoriesAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    inner class Repository {
        private val categoryRepository = CategoryRepository()

        fun refreshCategoriesList() {
            Log.i(TAG, "DayTrackerCategoriesFragment.Repository.refreshCategoriesList()")
            lifecycleScope.launchWhenCreated {
                val list: List<Category> = categoryRepository.list()
                categoriesAdapter.categories = list
                return@launchWhenCreated
            }
            Log.i(TAG, "DayTrackerFragment.Repository.refreshActivitiesList() = void")
        }

        fun sendCategoryCreateRequest() {

        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DayTrackerCategoriesFragment().apply {
            }
    }
}