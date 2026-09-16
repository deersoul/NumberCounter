package com.todaysoft.ymshin.numbercounter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.Nullable;

import com.todaysoft.ymshin.numbercounter.Constant;
import com.todaysoft.ymshin.numbercounter.IntroActivity;
import com.todaysoft.ymshin.numbercounter.MainActivity;
import com.todaysoft.ymshin.numbercounter.R;
import com.todaysoft.ymshin.numbercounter.component.BasicFragment;
import com.todaysoft.ymshin.numbercounter.component.Preferences;

/**
 * 설정 프레그먼트
 * @author ymshin
 * @Date 2019-02-06
 */
public class SettingFragment extends BasicFragment {

    private static final String TAG = "SettingFragment";
    private CheckBox mChkVibe, mChkIncrease;

    //새 인스턴스 get 메소드. 네비게이션에서 선택시 사용
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 처음 화면 그릴시. 초기화 담당.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        mChkVibe = rootView.findViewById(R.id.chkVibe);
        mChkIncrease = rootView.findViewById(R.id.chkIncrease);
        initView();
        return rootView;
    }

    /**
     * 기존 설정을 읽어와서 체크박스 선택상태를 잡아줌
     */
    private void initView() {

        //진동 울릴지 여부
        if (!Preferences.getBoolean(getActivity(), Constant.IS_VIBE)) mChkVibe.setChecked(false);
        //터치시 숫자 증가할지 여부
        if (!Preferences.getBoolean(getActivity(), Constant.IS_INCREASE)) mChkIncrease.setChecked(false);

        //진동 여부
        mChkVibe.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    Preferences.setValue(getActivity(), Constant.IS_VIBE, true);
                } else {
                    Preferences.setValue(getActivity(), Constant.IS_VIBE, false);
                }
            }
        }) ;

        //배경 터치시 숫자 증가 여부
        mChkIncrease.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    Preferences.setValue(getActivity(), Constant.IS_INCREASE, true);
                } else {
                    Preferences.setValue(getActivity(), Constant.IS_INCREASE, false);
                }
            }
        }) ;

        if (((MainActivity) getActivity()).getMenu() != null) {
            ((MainActivity) getActivity()).getMenu().clear();
                getActivity().getMenuInflater().inflate(R.menu.main_empty, ((MainActivity) getActivity()).getMenu());
        }
    }
}
