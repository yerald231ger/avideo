package com.gerardosanchez.video.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gerardosanchez.video.R;
import com.gerardosanchez.video.fragment.PlayerFragment;

import java.util.ArrayList;
import java.util.List;

public class ExoPlayerActivity extends AppCompatActivity {

    List<View> mViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);

        mViews.add(findViewById(R.id.topview));
        mViews.add(findViewById(R.id.bottomview));

        if (getIntent().getExtras().get("f").equals(1)) {
            PlayerFragment videoFragment = PlayerFragment.newInstance(0.5625,
                    "http://lablatam2.terra.com.mx/test/videos/videoplayback.mp4",
                    "http://ws.mobile.terra.com/Descargas/Storage/000000000/055000/055528/banner20170619190821_1440x810_.png");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frmExoPlayer_video, videoFragment, "InternalPlayerFragment").commit();
        } else {
            PlayerFragment videoFragment = PlayerFragment.newInstance(0.5625,
                    "http://lablatam2.terra.com.mx/test/videos/videoplayback.mp4",
                    "http://ws.mobile.terra.com/Descargas/Storage/000000000/055000/055528/banner20170619190821_1440x810_.png",
                    mViews);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frmExoPlayer_video, videoFragment, "InternalPlayerFragment").commit();
        }
    }
}
