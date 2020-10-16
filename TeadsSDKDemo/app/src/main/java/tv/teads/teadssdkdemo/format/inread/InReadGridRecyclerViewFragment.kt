package tv.teads.teadssdkdemo.format.inread

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_inread_recyclerview.*
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.adapter.SimpleRecyclerViewAdapter
import tv.teads.teadssdkdemo.utils.BaseFragment
import java.util.*

class InReadGridRecyclerViewFragment : BaseFragment() {
    private lateinit var adapter: SimpleRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerViewAdapter(recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        val data = ArrayList<String>()

        for (i in 0..5) {
            data.add("")
        }

        recyclerView.layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)

        adapter = SimpleRecyclerViewAdapter(data, context, pid)
        recyclerView.adapter = adapter
    }

    override fun getTitle(): String = "RecyclerView Grid"
}
