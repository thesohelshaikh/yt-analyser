package com.thesohelshaikh.ytanalyser.adapter;

import android.view.View;
import android.widget.TextView;

import com.thesohelshaikh.ytanalyser.R;

public class DurationViewHolder {
    TextView playbackTextView;
    TextView duration;
    TextView completeByTextView;

    DurationViewHolder(View v) {
        playbackTextView = v.findViewById(R.id.tv_playback);
        duration = v.findViewById(R.id.tv_updated_duration);
        completeByTextView = v.findViewById(R.id.tv_complete_by);
    }
}
