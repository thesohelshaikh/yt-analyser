package com.thesohelshaikh.ytanalyser;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

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

    Group mainLayout;
    ProgressBar progressBar;

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

        mainLayout = root.findViewById(R.id.groupMain);
        progressBar = root.findViewById(R.id.progressGetData);

        String videoID = InformationFragmentArgs.fromBundle(getArguments()).getVideoID();

        // checks if it is playlist or video
        if (videoID.startsWith("PL")) {
            getPlaylistInformation(videoID);
        } else {
            getInformation(videoID);
        }

        return root;
    }

    private void getInformation(String videoID) {
        showProgressBar();
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
                                hideProgressBar();
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

    private void getPlaylistInformation(String videoID) {
        showProgressBar();
        service.getPlaylistDetails(videoID, new YTService.PlaylistDetailsListener() {
            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onResponse(final PlaylistModel playlist) {
                hideProgressBar();
                Picasso.get().load(playlist.getThumbnailURL()).into(thumbnailImageView);
                videoTitleTextView.setText(playlist.getTitle());
                channelTitleTextView.setText(playlist.getCreatedBy());

                ArrayList<Long> durations = UtilitiesManger.calculateAlternateDurations
                        (new Date(playlist.getTotalDuration()));
                durationTextView.setText(UtilitiesManger.getPrettyDuration(durations
                        .get(0)));
                DurationsAdapter adapter = new DurationsAdapter(getContext()
                        , durations);
                durationsListView.setAdapter(adapter);

            }
        });
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
    }

}