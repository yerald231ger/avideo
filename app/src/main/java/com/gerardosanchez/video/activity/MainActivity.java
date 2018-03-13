package com.gerardosanchez.video.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gerardosanchez.video.R;
import com.gerardosanchez.video.fragment.VideoFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnprotiposys).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, VideoViewActivity.class).setData(Uri.parse("http://ws.mobile.terra.com/Descargas/Storage/000000000/055000/055528/ESPN20180301201538_1920x1080_.mp4")));
            }
        });

        findViewById(R.id.btnexoplayer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ExoPlayerActivity.class).putExtra("f", 1));
            }
        });

        findViewById(R.id.btnexoplayerfullscreenactivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ExoPlayerActivity.class).putExtra("f", 2));
            }
        });


//        VideoFragment videoFragment = VideoFragment.newInstance("","");
//
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.frmVideo, videoFragment, "ok").commit();
    }

}
