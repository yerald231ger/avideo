package com.gerardosanchez.video.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gerardosanchez.video.R;
import com.gerardosanchez.video.exoplayer.VideoHandler;
import com.gerardosanchez.video.fragment.ExoPlayerFragment;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;

public class ExoPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);

        ExoPlayerFragment videoFragment = ExoPlayerFragment.newInstance(0.5625,
                "http://lablatam2.terra.com.mx/test/videos/videoplayback.mp4",
                "http://ws.mobile.terra.com/Descargas/Storage/000000000/055000/055528/banner20170619190821_1440x810_.png",
                true);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frmExoPlayer_video, videoFragment, "PlayerFragment").commit();
    }


}
