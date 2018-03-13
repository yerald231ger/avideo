package com.gerardosanchez.video.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.gerardosanchez.video.R;
import com.gerardosanchez.video.fragment.ExoPlayerFragment;

public class FullscreenVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_video);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();

        ExoPlayerFragment videoFragment = ExoPlayerFragment.newInstance(
                intent.getDoubleExtra(ExoPlayerFragment.ASPECT_RATIO, 0.5625),
                intent.getStringExtra(ExoPlayerFragment.VIDEO_URI),
                intent.getStringExtra(ExoPlayerFragment.IMG_URI),
                false,
                true);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frmExoPlayer_video, videoFragment, "PlayerFragment").commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
