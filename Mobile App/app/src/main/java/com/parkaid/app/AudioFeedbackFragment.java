package com.parkaid.app;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import nl.changer.audiowife.AudioWife;

public class AudioFeedbackFragment extends Fragment {

    private View mPlayMedia;
    private View mPauseMedia;
    private SeekBar mMediaSeekBar;
    private TextView mRunTime;
    private TextView mTotalTime;
    private TextView mPlaybackTime;

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.fragment_audio_feedback, container, false);

        // initialize the player controls
        mPlayMedia = mainView.findViewById(R.id.play);
        mPauseMedia = mainView.findViewById(R.id.pause);
        mMediaSeekBar = (SeekBar) mainView.findViewById(R.id.media_seekbar);
        mRunTime = (TextView) mainView.findViewById(R.id.run_time);
        mTotalTime = (TextView) mainView.findViewById(R.id.total_time);

        AudioWife.getInstance()
                .init(getActivity(), Uri.parse("android.resource://com.parkaid.app/" + R.raw.feedback))
                .setPlayView(mPlayMedia)
                .setPauseView(mPauseMedia)
                .setSeekBar(mMediaSeekBar)
                .setRuntimeView(mRunTime)
                .setTotalTimeView(mTotalTime);
        AudioWife.getInstance().play();

        return mainView;
    }

    @Override
    public void onPause() {
        super.onPause();

        // when done playing, release the resources
        AudioWife.getInstance().release();
    }
}