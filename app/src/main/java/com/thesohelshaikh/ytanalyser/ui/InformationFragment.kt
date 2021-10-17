package com.thesohelshaikh.ytanalyser.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.UtilitiesManger.calculateAlternateDurations
import com.thesohelshaikh.ytanalyser.UtilitiesManger.getPrettyDuration
import com.thesohelshaikh.ytanalyser.UtilitiesManger.parseTime
import com.thesohelshaikh.ytanalyser.adapter.DurationsAdapter
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
    var videoTitleTextView: TextView? = null
    var channelTitleTextView: TextView? = null
    var durationTextView: TextView? = null
    var thumbnailImageView: ImageView? = null
    var durationsListView: ListView? = null
    var mainLayout: Group? = null
    var progressBar: ProgressBar? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        service = YTService(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_information, container, false)
        videoTitleTextView = root.findViewById(R.id.tv_videoTitle)
        channelTitleTextView = root.findViewById(R.id.tv_channelTitle)
        durationTextView = root.findViewById(R.id.tv_duration)
        thumbnailImageView = root.findViewById(R.id.iv_thumbnail)
        durationsListView = root.findViewById(R.id.lv_durations)
        mainLayout = root.findViewById(R.id.groupMain)
        progressBar = root.findViewById(R.id.progressGetData)
        val videoID = InformationFragmentArgs.fromBundle(
            requireArguments()
        ).videoID

        // checks if it is playlist or video
        if (videoID.startsWith("PL")) {
            getPlaylistInformation(videoID)
        } else {
            getInformation(videoID)
        }
        return root
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
                            videoTitleTextView!!.text = model.title
                            channelTitleTextView!!.text = model.channelTitle
                            durationTextView!!.text = getPrettyDuration(durations[0])
                            Picasso.get().load(videoModel.thumbnailURL).into(thumbnailImageView)
                            val adapter = DurationsAdapter(
                                context!!, durations
                            )
                            durationsListView!!.adapter = adapter
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
                Picasso.get().load(playlist.thumbnailURL).into(thumbnailImageView)
                videoTitleTextView!!.text = playlist.title
                channelTitleTextView!!.text = playlist.createdBy
                val durations = calculateAlternateDurations(Date(playlist.totalDuration))
                durationTextView!!.text = getPrettyDuration(durations[0])
                val adapter = DurationsAdapter(
                    context!!, durations
                )
                durationsListView!!.adapter = adapter
            }
        })
    }

    private fun showProgressBar() {
        progressBar!!.visibility = View.VISIBLE
        mainLayout!!.visibility = View.GONE
    }

    private fun hideProgressBar() {
        progressBar!!.visibility = View.GONE
        mainLayout!!.visibility = View.VISIBLE
    }
}