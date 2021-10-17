package com.thesohelshaikh.ytanalyser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thesohelshaikh.ytanalyser.R;
import com.thesohelshaikh.ytanalyser.UtilitiesManger;

import java.util.ArrayList;
import java.util.List;

public class DurationsAdapter extends ArrayAdapter<Long> {
    private final Context context;
    private List<Long> durations;
    private List<String> playbacks;

    public DurationsAdapter(Context context, List<Long> durations) {
        super(context, R.layout.duration_layout, durations);
        this.context = context;
        this.durations = durations;
        this.playbacks = new ArrayList<>();
        this.playbacks.add("At 1x");
        this.playbacks.add("At 1.25x");
        this.playbacks.add("At 1.5x");
        this.playbacks.add("At 1.75x");
        this.playbacks.add("At 2x");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View root = convertView;
        DurationViewHolder holder = null;
        if (root == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = inflater.inflate(R.layout.duration_layout, parent, false);
            holder = new DurationViewHolder(root);
            root.setTag(holder);
        } else {
            holder = (DurationViewHolder) root.getTag();
        }
        holder.playbackTextView.setText(playbacks.get(position));
        holder.duration.setText(UtilitiesManger.getPrettyDuration(durations.get(position)));
        holder.completeByTextView.setText(UtilitiesManger.getDateAfter(durations.get(position)));
        return root;
    }
}