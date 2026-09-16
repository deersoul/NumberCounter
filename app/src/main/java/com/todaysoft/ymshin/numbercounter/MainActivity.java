package com.todaysoft.ymshin.numbercounter;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.SubMenu;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.todaysoft.ymshin.numbercounter.db.DBHelper;
import com.todaysoft.ymshin.numbercounter.fragment.AboutFragment;
import com.todaysoft.ymshin.numbercounter.fragment.MainFragment;
import com.todaysoft.ymshin.numbercounter.fragment.SettingFragment;
import com.todaysoft.ymshin.numbercounter.util.CommonUtil;
import com.todaysoft.ymshin.numbercounter.vo.CounterVO;

import java.util.List;

/**
 * 네비게이션을 포함하는 근간의 메인 액티비티
 * @author ymshin
 * @Date 2019-02-06
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "MainActivity";
    private SettingFragment mSettingFragment;
    private MainFragment mMainFragment;
    private Toolbar toolbar;
    private boolean isDBLoad = false;
    private NavigationView navigationView;
    private Menu mMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //TODO 삭제하거나 edit해도 바꿔줘야함

        //처음엔 메인 탭하는 화면으로 이동한다.
        mMainFragment = MainFragment.newInstance(null);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, mMainFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        Log.e(TAG, "테스트 카운터 데이터 = " + DBHelper.useCounter(MainActivity.this).selectCounterData());
        initSaveList();
    }

    /**
     * DB 저장된 리스트가지고 네비게이션 메뉴를 새로 그림
     */
    public void initSaveList() {
        List<CounterVO> list = DBHelper.useCounter(MainActivity.this).selectCounterData();
        Log.e(TAG, "디비 리스트 = " + list + " 사이즈 = " + list.size());
        //저장 그룹이 이미 있었을수 있으니 삭제하고
        // navigationView.getMenu().removeGroup(Constant.GROUP_ID_NO);
        navigationView.getMenu().removeItem(Constant.GROUP_ID_NO);
        //DB 조회 결과가 있을경우 네비게이션에 추가
        if (list.size() > 0) {
            //서브메뉴 다시 달고
            SubMenu submenu = navigationView.getMenu().addSubMenu(Constant.GROUP_ID_NO, Constant.GROUP_ID_NO, Constant.GROUP_ID_NO, R.string.save_list);
            //밑에 리스트를 추가
            for (CounterVO vo : list) {
                int seq = vo.getSeq();
                submenu.add(seq, seq, seq, vo.getName());
            }
        }
    }
    /**
     * 메인화면에서 백 버튼을 눌렀을때. 종료하시겠습니까?물어본다.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.app_name)).setMessage(getString(R.string.msg_quit));
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //확인 버튼
                    finishAffinity();
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //취소 버튼
                }
            }).create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_no_delete, menu);
        Log.e(TAG, "크리에이트 옵션");
        return true;
    }

    /**
     * 상단 액션바 버튼 클릭 시 이벤트
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            if (mMainFragment != null) mMainFragment.reset();
        } else if (id == R.id.action_save) {
            mMainFragment.save();
            final RelativeLayout layout = CommonUtil.getViewForSave(MainActivity.this, mMainFragment.getName(), mMainFragment.getVal());
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            //DB에서 읽어온게 아니면 이름이 저장이고
            if (mMainFragment.getSeq() != null) isDBLoad = true;
            if (!isDBLoad) {
                builder.setTitle(getString(R.string.save));
            } else {
                //DB에서 읽어온거면 수정이다.
                builder.setTitle(getString(R.string.edit));
            }
            builder.setView(layout);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText etCountNm = (EditText) layout.findViewById(R.id.etCountNm);
                    EditText etCountVal = (EditText) layout.findViewById(R.id.etCountVal);
                    //DB 저장
                    String countNm = etCountNm.getText().toString();
                    String countVal = etCountVal.getText().toString();
                    if (isDBLoad) {
                        //업데이트
                        DBHelper.useCounter(MainActivity.this).updateCounterData(mMainFragment.getSeq(),
                                countNm.equals(null) || countNm.equals("") ? getString(R.string.no_name) : countNm,
                                countVal.equals(null) || countVal.equals("") ? "0" : countVal);
                        getToolbar().setTitle(countNm);
                        mMainFragment.setVal(countVal.equals(null) || countVal.equals("") ? "0" : countVal);
                    } else {
                        //삽입
                        DBHelper.useCounter(MainActivity.this).insertCounterData(countNm.equals(null) || countNm.equals("") ? getString(R.string.no_name) : countNm,
                                countVal.equals(null) || countVal.equals("") ? "0" : countVal);
                        if (countNm.equals(null) || countNm.equals("")) {
                            toolbar.setTitle(getString(R.string.no_name));
                        } else {
                            toolbar.setTitle(countNm); //액션바 이름 바꿔주기
                        }
                        int recentSeq = DBHelper.useCounter(MainActivity.this).selectRecentSeq();
                        if (recentSeq != 0) mMainFragment.setSeq(String.valueOf(recentSeq));
                        //TODO 저장하고나면 이름 바꿔주고, 저장한거 불러올 방법 생각
                    }
                    Toast.makeText(MainActivity.this, getString(R.string.msg_save), Toast.LENGTH_SHORT).show();
                    initSaveList();
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //취소
                }
            }).create().show();
        } else if (id == R.id.action_trash) {
            if (mMainFragment != null) {
                if (mMainFragment.getSeq() != null && mMainFragment.getSeq().length() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getString(R.string.app_name)).setMessage(getString(R.string.msg_delete_confirm));
                    builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //확인 버튼
                            mMainFragment.delete();
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //취소 버튼
                        }
                    }).create().show();

                }
            } else {

            }

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 네비게이션 선택시 메소드
     * @param item
     * @return true
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //준비중이면 네비게이션을 닫지 않는다.
        boolean isPrepare = false;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

      if (id == R.id.nav_add) {
          mMainFragment = MainFragment.newInstance(null);
          //+ 추가
          transaction.replace(R.id.container, mMainFragment);
          transaction.addToBackStack(null);
          transaction.commit();
      } else if (id == R.id.nav_setting) {
          transaction.replace(R.id.container, SettingFragment.newInstance());
          transaction.addToBackStack(null);
          transaction.commit();
      } else if (id == R.id.nav_about) {
        //about
          transaction.replace(R.id.container, AboutFragment.newInstance());
          transaction.addToBackStack(null);
          transaction.commit();
      } else if (id == R.id.nav_email) {
          //앱 문의하기. 이메일로 이동한다.
          connectEmail(Constant.DEV_EMAIL);
      } else if (id == R.id.nav_app_eval) {
          //앱 평가하기는 일단 앱을 배포한다음에 가능하기 때문에 1.0.0에서는 준비중임
          //TODO 다음 업데이트에 바로 적용
          //Toast.makeText(MainActivity.this, getString(R.string.msg_prepare), Toast.LENGTH_SHORT).show();
          //isPrepare = true;
          //2.0에서 업데이트 됨
          Intent intent = new Intent(Intent.ACTION_VIEW);
          intent.setData(Uri.parse("market://details?id=" + "com.todaysoft.ymshin.numbercounter"));
          startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK));
      }  else {
          //저장 목록을 눌렀을시
          mMainFragment = MainFragment.newInstance(String.valueOf(item.getItemId()));
          transaction.replace(R.id.container, mMainFragment);
          transaction.addToBackStack(null);
          transaction.commit();
      }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!isPrepare) drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * 액티비티 스타트 메소드
     * @param cls
     */
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    /**
     * 메일 전송 페이지로 연결
     * @param receiveEmail
     */
    public void connectEmail(String receiveEmail) {
        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setType("plain/text");
        // email setting 배열로 해놔서 복수 발송 가능
        email.setData(Uri.parse("mailto:" + receiveEmail));
        email.putExtra(Intent.EXTRA_SUBJECT,"");
        email.putExtra(Intent.EXTRA_TEXT,"\n");
        if (email.resolveActivity(getPackageManager()) != null) {
            startActivity(email);
        } else {
            // do something
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public Menu getMenu() {
        return mMenu;
    }
}
