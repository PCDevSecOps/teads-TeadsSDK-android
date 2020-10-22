package tv.teads.teadssdkdemo.format.mediation.admob

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_inread_recyclerview.*
import tv.teads.helper.TeadsBannerAdapterListener
import tv.teads.teadssdkdemo.MainActivity
import tv.teads.teadssdkdemo.R
import tv.teads.teadssdkdemo.format.mediation.adapter.AdMobRecyclerViewAdapter
import tv.teads.teadssdkdemo.format.mediation.identifier.AdMobIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment

import tv.teads.teadssdkdemo.format.mediation.identifier.AdMobIdentifier.ADMOB_TEADS_APP_ID

/**
 * Display inRead as Banner within a RecyclerView using AdMob Mediation.
 */
class AdMobGridRecyclerViewFragment : BaseFragment() {
    private lateinit var mListener: TeadsBannerAdapterListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inread_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerViewAdapter(recyclerView)
    }

    private fun setRecyclerViewAdapter(recyclerView: RecyclerView) {
        recyclerView.layoutManager = GridLayoutManager(activity,1, GridLayoutManager.VERTICAL, false)

        val adUnit = AdMobIdentifier.getAdUnitFromPid(pid)

        recyclerView.adapter = AdMobRecyclerViewAdapter(adUnit, ADMOB_TEADS_APP_ID, context, getTitle())
    }

    override fun getTitle(): String = "InRead AdMob RecyclerView Grid"
}
