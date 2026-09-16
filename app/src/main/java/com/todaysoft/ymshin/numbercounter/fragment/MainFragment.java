package com.todaysoft.ymshin.numbercounter.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.todaysoft.ymshin.numbercounter.Constant;
import com.todaysoft.ymshin.numbercounter.MainActivity;
import com.todaysoft.ymshin.numbercounter.R;
import com.todaysoft.ymshin.numbercounter.component.BasicFragment;
import com.todaysoft.ymshin.numbercounter.component.Preferences;
import com.todaysoft.ymshin.numbercounter.db.DBHelper;
import com.todaysoft.ymshin.numbercounter.vo.CounterVO;

/**
 * 숫자 +, - 메인 프레그먼트
 * @author ymshin
 * @Date 2019-02-06
 */
public class MainFragment extends BasicFragment {

    private static final String TAG = "MainFragment";
    private Button mBtnPlus, mBtnMinus;
    private TextView mTxtNum;
    private LinearLayout mLlNum;
    private int val = 0;
    private String seq = null;
    private String name = null;
    private AdView mAdView;

    //새 인스턴스 get 메소드. 네비게이션에서 선택시 사용
    public static MainFragment newInstance(String seq) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString("SEQ", seq);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "온크레이트 seq = " + seq);
        if (getArguments() != null) {
            seq = getArguments().getString("SEQ");
            Log.e(TAG, "만들고나서 = " + seq);
        } else {
            Log.e(TAG, "없는데?");
        }
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mBtnPlus = (Button) rootView.findViewById(R.id.btnPlus);
        mBtnMinus = (Button) rootView.findViewById(R.id.btnMinus);
        mTxtNum = (TextView) rootView.findViewById(R.id.txtNum);
        mLlNum = (LinearLayout) rootView.findViewById(R.id.llNum);
        final Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE); //바이브레이터

        //배너 광고 추가(2022-01-30)
      /*  MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        */
        //배너 광고 추가(2024-10-04)
        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(getActivity(), initializationStatus -> {});
                })
                .start();


        mAdView = (AdView)rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });

        //배경을 눌러도 숫자가 증가되게
        mLlNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Preferences.getBoolean(getActivity(), Constant.IS_INCREASE)) {
                    val += 1;
                } else {
                    val -= 1;
                }
                mTxtNum.setText(String.valueOf(val));

                //진동 울리기. 오레오 버전 이상에서는 이렇게 써주어야함
                if (Preferences.getBoolean(getActivity(), Constant.IS_VIBE)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(200);
                    }
                }
            }
        });
        // 숫자 플러스
        mBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val += 1;
                mTxtNum.setText(String.valueOf(val));

                //진동 울리기. 오레오 버전 이상에서는 이렇게 써주어야함
                if (Preferences.getBoolean(getActivity(), Constant.IS_VIBE)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(100);
                    }
                }
            }
        });

        // 숫자 마이너스
        mBtnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val -= 1;
                mTxtNum.setText(String.valueOf(val));

                //진동 울리기. 오레오 버전 이상에서는 이렇게 써주어야함
                if (Preferences.getBoolean(getActivity(), Constant.IS_VIBE)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(50);
                    }
                }
            }
        });

        if (seq != null) {
            //프레그먼트에 넘겨받은 시퀀스가 있으면 DB에서 SELECT해서 세팅한다.
            CounterVO vo = DBHelper.useCounter(getActivity()).selectCounterDataDetail(seq);
            //값 세팅, 제목 변경
            ((MainActivity)getActivity()).getToolbar().setTitle(vo.getName());
            val = Integer.parseInt(vo.getValue());
            name = vo.getName();
            mTxtNum.setText(String.valueOf(val));
        }
        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((MainActivity) getActivity()).getMenu() != null) {
            ((MainActivity) getActivity()).getMenu().clear();
            if (seq != null) {
                getActivity().getMenuInflater().inflate(R.menu.main, ((MainActivity) getActivity()).getMenu());
            } else {
                getActivity().getMenuInflater().inflate(R.menu.main_no_delete, ((MainActivity) getActivity()).getMenu());
            }
        }
    }

    /**
     * 화면 뷰 초기화. 0으로 만듬
     */
    public void reset() {
        ((MainActivity)getActivity()).getToolbar().setTitle(getString(R.string.app_name));
        mTxtNum.setText("0");
        val = 0;
        seq = null;
    }

    /**
     * DB에 저장
     */
    public void save() {

    }

    public void setVal(String val) {
        this.val = Integer.parseInt(val);
        mTxtNum.setText(val);
    }
    /**
     * value 리턴
     * @return 숫자 value
     */
    public String getVal() {
        return String.valueOf(val);
    }

    /**
     * 시퀀스 반환. DB에서 읽어온 데이터인지 new인지 구분하기 위해
     * @return
     */
    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    /**
     * 삭제
     */
    public void delete() {
        Log.e(TAG, "삭제 ? = " + seq);
        if (seq != null) {
            //예외 한번 체크하고
            DBHelper.useCounter(getActivity()).deleteCounterData(seq);
            ((MainActivity)getActivity()).initSaveList();
        }
        reset();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity)getActivity()).getToolbar().setTitle(getString(R.string.app_name));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
