package com.todaysoft.ymshin.numbercounter.component;

import android.widget.Toast;

import androidx.fragment.app.Fragment;

/**
 * 기본 프레그먼트
 * @author ymshin
 * @Date 2019-02-04
 */
public class BasicFragment extends Fragment {

    public void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
