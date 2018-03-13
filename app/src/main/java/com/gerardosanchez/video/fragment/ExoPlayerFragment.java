package com.gerardosanchez.video.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.gerardosanchez.video.R;
import com.gerardosanchez.video.activity.ExoPlayerActivity;
import com.gerardosanchez.video.activity.FullscreenVideoActivity;
import com.gerardosanchez.video.exoplayer.VideoHandler;

import java.io.InputStream;

public class ExoPlayerFragment extends Fragment implements View.OnClickListener, VideoHandler.EventListener {
    public static final String FRAGMENT_IN_FULL_SCREEN_ACTIVITY = "FRAGMENT_IN_FULL_SCREEN_ACTIVITY";
    public static final String FULL_SCREEN_ACTIVITY = "FULL_SCREEN_ACTIVITY";
    public static final String ASPECT_RATIO = "ASPECT_RATIO";
    public static final String VIDEO_URI = "VIDEO_URI";
    public static final String IMG_URI = "IMG_URI";

    private static int mWidthPixels;
    private static int mHeightPixels;
    private Uri mVideoUri, mImgUri;
    private double mAspectRatio;
    private SurfaceView mPlayerView;
    private LinearLayout mMediaControlls;
    private ImageView mIcPlayPause, mIcRewind, mIcForward;
    private ProgressBar mProgressBar;
    private boolean mFullScreenActivity;
    private boolean mFragmentInFullScreenActivity;
    private boolean mFromFullScreenActivity;

    public ExoPlayerFragment() {
        // Required empty public constructor
    }

    public static ExoPlayerFragment newInstance(double aspectRatio, String videoUri, String imgUri) {
        return ExoPlayerFragment.newInstance(aspectRatio, videoUri, imgUri, false, true);
    }

    public static ExoPlayerFragment newInstance(double aspectRatio, String videoUri, String imgUri, boolean fullScreenActivity) {
        return ExoPlayerFragment.newInstance(aspectRatio, videoUri, imgUri, fullScreenActivity, false);
    }

    public static ExoPlayerFragment newInstance(double aspectRatio, String videoUri, String imgUri, boolean fullScreenActivity, boolean fragmentInFullScreenActivity) {
        ExoPlayerFragment fragment = new ExoPlayerFragment();
        VideoHandler.getInstance().addListener(fragment);
        Bundle args = new Bundle();
        args.putBoolean(FRAGMENT_IN_FULL_SCREEN_ACTIVITY, fragmentInFullScreenActivity);
        args.putBoolean(FULL_SCREEN_ACTIVITY, fullScreenActivity);
        args.putDouble(ASPECT_RATIO, aspectRatio);
        args.putString(VIDEO_URI, videoUri);
        args.putString(IMG_URI, imgUri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setDimensions(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAspectRatio = getArguments().getDouble(ASPECT_RATIO);
            mVideoUri = Uri.parse(getArguments().getString(VIDEO_URI));
            mImgUri = Uri.parse(getArguments().getString(IMG_URI));
            mFullScreenActivity = getArguments().getBoolean(FULL_SCREEN_ACTIVITY);
            mFragmentInFullScreenActivity = getArguments().getBoolean(FRAGMENT_IN_FULL_SCREEN_ACTIVITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exo_player, container, false);
        mPlayerView = view.findViewById(R.id.player_video);
        mMediaControlls = view.findViewById(R.id.player_mediacontroller);
        mIcRewind = mMediaControlls.findViewById(R.id.player_ic_rewind);
        mIcPlayPause = mMediaControlls.findViewById(R.id.player_ic_playpuse);
        mIcForward = mMediaControlls.findViewById(R.id.player_ic_forward);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VideoHandler.getInstance().isPlaying()) {
                    if (mMediaControlls.getVisibility() == View.GONE)
                        mMediaControlls.setVisibility(View.VISIBLE);
                    else if (mMediaControlls.getVisibility() == View.VISIBLE)
                        mMediaControlls.setVisibility(View.GONE);
                } else {
                    VideoHandler.getInstance().Start();
                }
            }
        });

        mProgressBar = createProgressBar(getContext());
        ((RelativeLayout) view).addView(mProgressBar);

        setLayoutParams(view);

        mIcForward.setOnClickListener(this);
        mIcPlayPause.setOnClickListener(this);
        mIcRewind.setOnClickListener(this);
        new DownloadImageTask((ImageView) view.findViewById(R.id.player_thumbile))
                .execute(mImgUri);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        VideoHandler.getInstance().setPlayerForUri(
                getContext(),
                mVideoUri, mPlayerView);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mFromFullScreenActivity)
            VideoHandler.getInstance().setPlayerForUri(
                    getContext(),
                    mVideoUri, mPlayerView);

        VideoHandler.getInstance().goToForeground();
    }

    @Override
    public void onPause() {
        super.onPause();
        VideoHandler.getInstance().goToBackground();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        changeLayoutMediaControllerWight();
        setLayoutParams(getView());

        if (mFullScreenActivity && !mFragmentInFullScreenActivity && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mFromFullScreenActivity = true;
            goToFullScreenActivity();
        }
    }

    public void goToFullScreenActivity() {
        ExoPlayerActivity exoPlayerActivity = ((ExoPlayerActivity) getContext());
        Intent intent = new Intent(exoPlayerActivity, FullscreenVideoActivity.class).setData(mVideoUri);
        intent.putExtra(VIDEO_URI, mVideoUri.toString());
        intent.putExtra(IMG_URI, mImgUri.toString());
        exoPlayerActivity.startActivity(intent);
    }
    //region implemetation

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.player_ic_rewind:
                VideoHandler.getInstance().Rewind();
                break;
            case R.id.player_ic_playpuse:
                if (VideoHandler.getInstance().isPlaying())
                    VideoHandler.getInstance().Pause();
                else
                    VideoHandler.getInstance().Start();
                break;
            case R.id.player_ic_forward:
                VideoHandler.getInstance().Forward();
                break;
        }
    }

    @Override
    public void onLoadingChange(Context context, boolean isLoading) {
        if (isLoading)
            mProgressBar.setVisibility(View.VISIBLE);
        else
            mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPlayerStateChanged(Context context, boolean playWhenReady, int playbackState) {

        switch (playbackState) {
            case com.google.android.exoplayer2.Player.STATE_READY:
                if (playWhenReady) {
                    mIcPlayPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause));
                    ((Activity)context).findViewById(R.id.player_relative).setVisibility(View.GONE);
                } else
                    mIcPlayPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                break;
            case com.google.android.exoplayer2.Player.STATE_BUFFERING:
                if (playWhenReady) {
                    mIcPlayPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause));
                    ((Activity)context).findViewById(R.id.player_relative).setVisibility(View.GONE);
                } else
                    mIcPlayPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play));
                break;
            default:
                break;
        }
    }

    //endregion implemetation

    //region works in layouts

    public void changeLayoutMediaControllerWight() {
        if (mMediaControlls.getVisibility() == View.VISIBLE)
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ((LinearLayout.LayoutParams) (mMediaControlls.findViewById(R.id.player_mediacontroller_view)).getLayoutParams()).weight = .2f;
                ((LinearLayout.LayoutParams) (mMediaControlls.findViewById(R.id.player_mediacontroller_controls)).getLayoutParams()).weight = .8f;
            } else {
                ((LinearLayout.LayoutParams) (mMediaControlls.findViewById(R.id.player_mediacontroller_view)).getLayoutParams()).weight = .3f;
                ((LinearLayout.LayoutParams) (mMediaControlls.findViewById(R.id.player_mediacontroller_controls)).getLayoutParams()).weight = .7f;
            }
    }

    public void setLayoutParams(View parentView) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            parentView.getLayoutParams().height = (int) (getHeight() * mAspectRatio);
        else
            parentView.getLayoutParams().height = (int) (getWidth() * mAspectRatio);

        parentView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
    }

    private ProgressBar createProgressBar(Context context) {
        ProgressBar progressBar = new ProgressBar(context);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(140, 140);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressBar.setLayoutParams(layoutParams);

        progressBar.setVisibility(View.GONE);
        return progressBar;
    }
    //endregion works in layouts

    //region Utils

    public static int getWidth() {
        return mWidthPixels;
    }

    public static int getHeight() {
        return mHeightPixels;
    }


    public static void setDimensions(Context context) {
        int[] dims = getDimension(context);
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mWidthPixels = dims[1];
            mHeightPixels = dims[0];

        } else {
            mWidthPixels = dims[0];
            mHeightPixels = dims[1];
        }
    }

    private static int[] getDimension(Context c) {

        WindowManager w = ((Activity) c).getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
        // includes window decorations (statusbar bar/menu bar)

        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored) {
            }

        return new int[]{widthPixels, heightPixels};
    }


    private class DownloadImageTask extends AsyncTask<Uri, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(Uri... urls) {
            String urldisplay = urls[0].toString();
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    //endregion Utils
}