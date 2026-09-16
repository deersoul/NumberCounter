package com.todaysoft.ymshin.numbercounter.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.todaysoft.ymshin.numbercounter.MainActivity;
import com.todaysoft.ymshin.numbercounter.R;
import com.todaysoft.ymshin.numbercounter.component.BasicFragment;

public class AboutFragment extends BasicFragment {

    private static final String TAG = "AboutFragment";
    private TextView mTvBlog;

    //새 인스턴스 get 메소드. 네비게이션에서 선택시 사용
    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        mTvBlog = (TextView) rootView.findViewById(R.id.txtBlog);
        //TODO 이벤트 설정
        if (((MainActivity) getActivity()).getMenu() != null) {
            ((MainActivity) getActivity()).getMenu().clear();
            getActivity().getMenuInflater().inflate(R.menu.main_empty, ((MainActivity) getActivity()).getMenu());
        }

        //블로그 클릭하면 브라우저로 이동
        mTvBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/deersoul6662"));
                startActivity(intent);

            }
        });
        return rootView;
    }

}
