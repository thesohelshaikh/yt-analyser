package com.thesohelshaikh.ytanalyser.ui.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import com.thesohelshaikh.ytanalyser.UtilitiesManger.calculateAlternateDurations
import com.thesohelshaikh.ytanalyser.UtilitiesManger.getPrettyDuration
import com.thesohelshaikh.ytanalyser.adapter.DurationsAdapter
import com.thesohelshaikh.ytanalyser.databinding.FragmentInformationBinding
import com.thesohelshaikh.ytanalyser.network.YTService
import com.thesohelshaikh.ytanalyser.ui.details.InformationViewModel.DetailsScreenState
import java.util.*

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class InformationFragment : Fragment() {
    private var service: YTService? = null
    private lateinit var binding: FragmentInformationBinding
    private var videoID: String = ""
    private val viewModel by viewModels<InformationViewModel>()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        service = YTService(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoID = InformationFragmentArgs.fromBundle(requireArguments()).videoID

        lifecycleScope.launchWhenCreated {
            fetchDetails()
        }
        observeDetailsResponse()
    }

    private fun observeDetailsResponse() {
        viewModel.detailsScreenState.observe(viewLifecycleOwner) {

            when (it) {
                is DetailsScreenState.SuccessState -> {
                    hideProgressBar()
                    Picasso.get().load(it.thumbnailUrl).into(binding.ivThumbnail)
                    binding.tvVideoTitle.text = it.title
                    binding.tvChannelTitle.text = it.channelTitle
                    val durations = calculateAlternateDurations(Date(it.duration))
                    binding.tvDuration.text = getPrettyDuration(durations.first())
                    val adapter = DurationsAdapter(requireContext(), durations)
                    binding.lvDurations.adapter = adapter
                }
                is DetailsScreenState.ErrorState -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is DetailsScreenState.LoadingState -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun fetchDetails() {
        // checks if it is playlist or video
        if (videoID.startsWith("PL")) {
            getPlaylistInformation(videoID)
        } else {
            viewModel.getVideoDetails(videoID)
        }
    }

    private fun getPlaylistInformation(videoID: String) {
        showProgressBar()
        viewModel.getPlaylistVideoIds(playlistId = videoID)
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