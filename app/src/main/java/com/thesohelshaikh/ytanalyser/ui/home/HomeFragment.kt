package com.thesohelshaikh.ytanalyser.ui.home

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.thesohelshaikh.ytanalyser.UtilitiesManger.getIDfromURL
import com.thesohelshaikh.ytanalyser.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    var clipboardManager: ClipboardManager? = null
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        clipboardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val stringFromClipboard = stringFromClipboard
        binding.edUrl.setText(stringFromClipboard)

        binding.btnAnalyse.setOnClickListener {
            val iDfromURL = getIDfromURL(binding.edUrl.text.toString())
            Navigation.findNavController(binding.root)
                .navigate(
                    (HomeFragmentDirections.toInformationFragment(
                        iDfromURL
                    ) as NavDirections)
                )
        }
        return binding.root
    }

    //TODO: verify this works
    private val stringFromClipboard: String
        get() {
            //TODO: verify this works
            val primaryClip = clipboardManager!!.primaryClip
            return primaryClip?.getItemAt(0)?.text?.toString() ?: ""
        }
}