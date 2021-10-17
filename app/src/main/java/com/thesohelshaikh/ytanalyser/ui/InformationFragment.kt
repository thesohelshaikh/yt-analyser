package com.thesohelshaikh.ytanalyser.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.thesohelshaikh.ytanalyser.UtilitiesManger.calculateAlternateDurations
import com.thesohelshaikh.ytanalyser.UtilitiesManger.getPrettyDuration
import com.thesohelshaikh.ytanalyser.UtilitiesManger.parseTime
import com.thesohelshaikh.ytanalyser.adapter.DurationsAdapter
import com.thesohelshaikh.ytanalyser.databinding.FragmentInformationBinding
import com.thesohelshaikh.ytanalyser.model.PlaylistModel
import com.thesohelshaikh.ytanalyser.model.VideoModel
import com.thesohelshaikh.ytanalyser.network.YTService
import com.thesohelshaikh.ytanalyser.network.YTService.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class InformationFragment : Fragment() {
    private var service: YTService? = null
    private lateinit var binding: FragmentInformationBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        service = YTService(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
         binding = FragmentInformationBinding.inflate(layoutInflater)
        val videoID = InformationFragmentArgs.fromBundle(
            requireArguments()
        ).videoID

        // checks if it is playlist or video
        if (videoID.startsWith("PL")) {
            getPlaylistInformation(videoID)
        } else {
            getInformation(videoID)
        }
        return binding.root
    }

    private fun getInformation(videoID: String) {
        showProgressBar()
        service!!.getDuration(videoID, object : VolleyResponseListener {
            override fun onError(errorMessage: String) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(videoModel: VideoModel) {
                service!!.getVideoDetails(videoModel.id, videoModel,
                    object : VideoDetailsListener {
                        override fun onError(errorMessage: String) {}
                        override fun onResponse(model: VideoModel) {
                            hideProgressBar()
                            val durations = parseTime(model.duration)
                            binding.tvVideoTitle.text = model.title
                            binding.tvChannelTitle.text = model.channelTitle
                            binding.tvDuration.text = getPrettyDuration(durations[0])
                            Picasso.get().load(videoModel.thumbnailURL).into(binding.ivThumbnail)
                            val adapter = DurationsAdapter(
                                context!!, durations
                            )
                            binding.lvDurations.adapter = adapter
                        }
                    })
            }
        })
    }

    private fun getPlaylistInformation(videoID: String) {
        showProgressBar()
        service!!.getPlaylistDetails(videoID, object : PlaylistDetailsListener {
            override fun onError(errorMessage: String) {}
            override fun onResponse(playlist: PlaylistModel) {
                hideProgressBar()
                Picasso.get().load(playlist.thumbnailURL).into(binding.ivThumbnail)
                binding.tvVideoTitle.text = playlist.title
                binding.tvChannelTitle.text = playlist.createdBy
                val durations = calculateAlternateDurations(Date(playlist.totalDuration))
                binding.tvDuration.text = getPrettyDuration(durations[0])
                val adapter = DurationsAdapter(
                    context!!, durations
                )
                binding.lvDurations.adapter = adapter
            }
        })
    }

    private fun showProgressBar() {
        binding.progressGetData.visibility = View.VISIBLE
        binding.groupMain.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressGetData.visibility = View.GONE
        binding.groupMain.visibility = View.VISIBLE
    }
}