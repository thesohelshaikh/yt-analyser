package com.thesohelshaikh.ytanalyser;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AnalyseFragment extends Fragment {
    ClipboardManager clipboardManager;
    Button analyseButton;
    EditText edURL;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_analyse, container, false);
        analyseButton = root.findViewById(R.id.btn_analyse);
        edURL = root.findViewById(R.id.ed_url);
        clipboardManager =
                (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        String stringFromClipboard = getStringFromClipboard();
        edURL.setText(stringFromClipboard);
        analyseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iDfromURL = UtilitiesManger.getIDfromURL(edURL.getText().toString());
                Navigation.findNavController(root).navigate(AnalyseFragmentDirections.toInformationFragment(iDfromURL));
            }
        });

        return root;
    }

    public String getStringFromClipboard() {
        //TODO: verify this works
        ClipData primaryClip = clipboardManager.getPrimaryClip();
        if (primaryClip != null) {
            return primaryClip.getItemAt(0).getText().toString();
        }
        return "";
    }

}