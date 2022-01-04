package pl.gawor.android.tayckner.day_tracker.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.gawor.android.tayckner.R


class DayTrackerFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.day_tracker_fragment_main, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            DayTrackerFragment().apply {
            }
    }
}