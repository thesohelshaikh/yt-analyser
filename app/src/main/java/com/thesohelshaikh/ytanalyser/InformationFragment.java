package com.thesohelshaikh.ytanalyser;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class InformationFragment extends Fragment {

    private YTService service;
    TextView videoTitleTextView;
    TextView channelTitleTextView;
    TextView durationTextView;
    ImageView thumbnailImageView;
    ListView durationsListView;

    public InformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        service = new YTService(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_information, container, false);
        videoTitleTextView = root.findViewById(R.id.tv_videoTitle);
        channelTitleTextView = root.findViewById(R.id.tv_channelTitle);
        durationTextView = root.findViewById(R.id.tv_duration);
        thumbnailImageView = root.findViewById(R.id.iv_thumbnail);
        durationsListView = root.findViewById(R.id.lv_durations);

        String videoID = InformationFragmentArgs.fromBundle(getArguments()).getVideoID();

        getInformation(videoID);
        return root;
    }

    private void getInformation(String videoID) {
        service.getDuration(videoID, new YTService.VolleyResponseListener() {
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(final VideoModel videoModel) {
                service.getVideoDetails(videoModel.getId(), videoModel,
                        new YTService.VideoDetailsListener() {
                            @Override
                            public void onError(String errorMessage) {

                            }

                            @Override
                            public void onResponse(VideoModel model) {
                                ArrayList<Long> durations =
                                        UtilitiesManger.parseTime(model.getDuration());
                                videoTitleTextView.setText(model.getTitle());
                                channelTitleTextView.setText(model.getChannelTitle());
                                durationTextView.setText(UtilitiesManger.getPrettyDuration(durations.get(0)));
                                Picasso.get().load(videoModel.getThumbnailURL()).into(thumbnailImageView);
                                DurationsAdapter adapter = new DurationsAdapter(getContext()
                                        , durations);
                                durationsListView.setAdapter(adapter);
                            }
                        });
            }
        });
    }
}