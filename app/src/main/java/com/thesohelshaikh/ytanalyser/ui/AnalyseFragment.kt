package com.thesohelshaikh.ytanalyser.ui

import com.thesohelshaikh.ytanalyser.UtilitiesManger.getIDfromURL
import android.widget.EditText
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.UtilitiesManger
import androidx.navigation.NavDirections
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class AnalyseFragment : Fragment() {
    var clipboardManager: ClipboardManager? = null
    var analyseButton: Button? = null
    var edURL: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_analyse, container, false)
        analyseButton = root.findViewById(R.id.btn_analyse)
        edURL = root.findViewById(R.id.ed_url)
        clipboardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val stringFromClipboard = stringFromClipboard
        edURL!!.setText(stringFromClipboard)
        analyseButton!!.setOnClickListener {
            val iDfromURL = getIDfromURL(edURL!!.getText().toString())
            Navigation.findNavController(root)
                .navigate((AnalyseFragmentDirections.toInformationFragment(iDfromURL) as NavDirections))
        }
        return root
    }

    //TODO: verify this works
    private val stringFromClipboard: String
        get() {
            //TODO: verify this works
            val primaryClip = clipboardManager!!.primaryClip
            return primaryClip?.getItemAt(0)?.text?.toString() ?: ""
        }
}