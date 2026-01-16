package com.opera.ads.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opera.ads.AdFormat
import com.opera.ads.OperaAds
import com.opera.ads.demo.R
import com.opera.ads.demo.databinding.FragmentMainBinding
import com.opera.ads.demo.util.Constant.samplePlacementId

class MainFragment : Fragment() {
    private class MainRecyclerAdapter(
        private val items: List<MainItem>,
        private var itemClickListener: (MainItem) -> Unit,
    ) :
        RecyclerView.Adapter<MainViewHolder?>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MainViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.fragment_main_item,
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.apply {
                itemView.setBackgroundResource(R.drawable.ripple_background)
                val item = items[position]
                formatTv.text = item.adFormat.name.lowercase()
                placementTv.text = item.placementId
                itemView.setOnClickListener { _: View? ->
                    itemClickListener(items[position])
                }
            }
        }

        override fun getItemCount() = items.size
    }

    private class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var formatTv: TextView = itemView.findViewById(R.id.item_format)
        var placementTv: TextView = itemView.findViewById(R.id.item_placement)
    }

    private class MainItem(val adFormat: AdFormat, val placementId: String)

    private lateinit var mainBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMainBinding.inflate(inflater, container, false).also {
        mainBinding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainBinding.sdkVersion.text =
            requireContext().getString(R.string.sdk_version, OperaAds.getVersion())

        val recyclerView: RecyclerView = mainBinding.rvMain
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MainRecyclerAdapter(
            AdFormat.entries.map { it.toMainItem() }
        ) { item ->
            val destination: Int = when (item.adFormat) {
                AdFormat.NATIVE -> R.id.action_main_to_native
                AdFormat.BANNER -> R.id.action_main_to_banner
                AdFormat.INTERSTITIAL -> R.id.action_main_to_interstitial
                AdFormat.REWARDED -> R.id.action_main_to_rewarded
                AdFormat.REWARDED_INTERSTITIAL -> R.id.action_main_to_rewarded_interstitial
                AdFormat.APP_OPEN -> R.id.action_main_to_app_open
            }
            val bundle = Bundle()
            bundle.putString("placementId", item.placementId)
            NavHostFragment.findNavController(this).navigate(destination, bundle)
        }
    }

    private fun AdFormat.toMainItem() = MainItem(this, samplePlacementId())
}
