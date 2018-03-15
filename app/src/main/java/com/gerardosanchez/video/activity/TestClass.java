package com.gerardosanchez.video.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by Gerardo.Sanchez on 15/03/2018.
 */

public class TestClass extends Fragment {

    public int s = 0;

    public TestClass() {
        String ok = "";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
