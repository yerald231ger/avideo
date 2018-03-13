package com.gerardosanchez.video.fragment;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.gerardosanchez.video.R;
import com.gerardosanchez.video.activity.Utils;
import com.gerardosanchez.video.mediaplayer.MediaSource;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "movstore-player";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private com.gerardosanchez.video.mediaplayer.VideoView mVideoView;
    private ProgressBar mProgress;

    private MediaController.MediaPlayerControl mMediaPlayerControl;
    private MediaController mMediaController;

    private Uri mVideoUri;
    private int mVideoPosition;
    private float mVideoPlaybackSpeed;
    private boolean mVideoPlaying;
    private MediaSource mMediaSource;


    public VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        final VideoView videoView = view.findViewById(R.id.vidvVideo_Video);
        final VideoView videoView2 = view.findViewById(R.id.vidvVideo_Video2);
        final Button btnStop = view.findViewById(R.id.btnVideo_Stop);

        view.findViewById(R.id.btnVideo_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.setVideoPath("/sdcard/download/barca.mp4");
                videoView.setMediaController( new MediaController(getActivity()));
                videoView.requestFocus();
                videoView.start();
            }
        });


        view.findViewById(R.id.btnVideo_play2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView2.setVideoURI(Uri.parse("http://ws.mobile.terra.com/Descargas/Storage/000000000/055000/055528/ESPN_terra20180228113101_1920x1080_.mp4"));
                videoView2.setMediaController( new MediaController(getActivity()));
                videoView2.requestFocus();
                videoView2.start();
            }
        });

        view.findViewById(R.id.rlyVideo_VideoContainer).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (mMediaController.isShowing()) {
                        mMediaController.hide();
                    } else {
                        mMediaController.show();
                    }
                }
                return false;
            }
        });

        mVideoView =  view.findViewById(R.id.vv);
        mProgress = view.findViewById(R.id.progress);

        mMediaPlayerControl = mVideoView; //new MediaPlayerDummyControl();
        mMediaController = new MediaController(getContext());
        mMediaController.setAnchorView(view.findViewById(R.id.container));
        mMediaController.setMediaPlayer(mMediaPlayerControl);
        mMediaController.setEnabled(false);

        mProgress.setVisibility(View.VISIBLE);

        mVideoUri = Uri.parse("https://neo.terra.com/public/ESPN_terra20180228113101_1920x1080_-new.mp4");
        mVideoPosition = 0;
        mVideoPlaybackSpeed = 1;
        mVideoPlaying = false;

        //region touch
        btnStop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    a = motionEvent.getDownTime();
                }

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    b = motionEvent.getEventTime();

                    long t = b - a;

                    if(t < 500){
                        videoView.stopPlayback();
                        Toast.makeText(getContext(), "Stop first", Toast.LENGTH_SHORT).show();
                    }else if(t > 500){
                        videoView2.stopPlayback();
                        Toast.makeText(getContext(), "Stop two", Toast.LENGTH_SHORT).show();
                    }

                }
                return true;
            }
        });
        //endregion touch
        if(!mVideoView.isPlaying()) {
            initPlayer();
        }
        return view;
    }

    private void initPlayer() {

        mVideoView.setOnPreparedListener(new com.gerardosanchez.video.mediaplayer.MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(com.gerardosanchez.video.mediaplayer.MediaPlayer vp) {
                mProgress.setVisibility(View.GONE);
                mMediaController.setEnabled(true);
            }
        });
        mVideoView.setOnErrorListener(new com.gerardosanchez.video.mediaplayer.MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(com.gerardosanchez.video.mediaplayer.MediaPlayer mp, int what, int extra) {
                Toast.makeText(getContext(),
                        "Cannot play the video, see logcat for the detailed exception",
                        Toast.LENGTH_LONG).show();
                mProgress.setVisibility(View.GONE);
                mMediaController.setEnabled(false);
                return true;
            }
        });
        mVideoView.setOnInfoListener(new com.gerardosanchez.video.mediaplayer.MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(com.gerardosanchez.video.mediaplayer.MediaPlayer mp, int what, int extra) {
                String whatName = "";
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        whatName = "MEDIA_INFO_BUFFERING_END";
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        whatName = "MEDIA_INFO_BUFFERING_START";
                        break;
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        whatName = "MEDIA_INFO_VIDEO_RENDERING_START";
                        break;
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                        whatName = "MEDIA_INFO_VIDEO_TRACK_LAGGING";
                        break;
                }
                Log.d(TAG, "onInfo " + whatName);
                return false;
            }
        });
        mVideoView.setOnSeekListener(new com.gerardosanchez.video.mediaplayer.MediaPlayer.OnSeekListener() {
            @Override
            public void onSeek(com.gerardosanchez.video.mediaplayer.MediaPlayer mp) {
                Log.d(TAG, "onSeek");
                mProgress.setVisibility(View.VISIBLE);
            }
        });
        mVideoView.setOnSeekCompleteListener(new com.gerardosanchez.video.mediaplayer.MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(com.gerardosanchez.video.mediaplayer.MediaPlayer mp) {
                Log.d(TAG, "onSeekComplete");
                mProgress.setVisibility(View.GONE);
            }
        });
        mVideoView.setOnBufferingUpdateListener(new com.gerardosanchez.video.mediaplayer.MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(com.gerardosanchez.video.mediaplayer.MediaPlayer mp, int percent) {
                Log.d(TAG, "onBufferingUpdate " + percent + "%");
            }
        });

        Utils.MediaSourceAsyncCallbackHandler mMediaSourceAsyncCallbackHandler =
                new Utils.MediaSourceAsyncCallbackHandler() {
                    @Override
                    public void onMediaSourceLoaded(MediaSource mediaSource) {
                        mMediaSource = mediaSource;
                        mVideoView.setVideoSource(mediaSource);
                        mVideoView.seekTo(mVideoPosition);
                        mVideoView.setPlaybackSpeed(mVideoPlaybackSpeed);
                        if (mVideoPlaying) {
                            mVideoView.start();
                        }
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.e(TAG, "error loading video", e);
                    }
                };
        if(mMediaSource == null) {
            // Convert uri to media source asynchronously to avoid UI blocking
            // It could take a while, e.g. if it's a DASH source and needs to be preprocessed
            Utils.uriToMediaSourceAsync(getContext(), mVideoUri, mMediaSourceAsyncCallbackHandler);
        } else {
            // Media source is already here, just use it
            mMediaSourceAsyncCallbackHandler.onMediaSourceLoaded(mMediaSource);
        }
    }

    public long a = 0,b = 0;
}
