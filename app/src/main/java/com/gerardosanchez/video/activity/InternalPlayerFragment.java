package com.gerardosanchez.video.activity;


import com.gerardosanchez.video.fragment.PlayerFragment;

/**
 * Created by Gerardo.Sanchez on 13/03/2018.
 */

public class InternalPlayerFragment extends PlayerFragment {

    static PlayerFragment newInternal(double aspectRatio, String videoUri, String imgUri, boolean fullScreenActivity, boolean fragmentInFullScreenActivity) {
        return PlayerFragment.newInstance(aspectRatio, videoUri, imgUri, fullScreenActivity, fragmentInFullScreenActivity, null);
    }
}
