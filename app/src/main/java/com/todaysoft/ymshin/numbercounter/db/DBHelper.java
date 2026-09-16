package com.todaysoft.ymshin.numbercounter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.todaysoft.ymshin.numbercounter.db.table.TBCounter;
import com.todaysoft.ymshin.numbercounter.db.table.TBSetting;

/**
 * 데이터베이스 Helper 클래스. DB 초기화, 버전 관리 등 전반적인 DB 관련 기능 담당
 * @author ymshin
 * @Date 2019-02-04
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sDBHelper;
    private static final String DB_NAME = "DeerCounter.db";
    //private static final String DB_NAME = "//mnt/sdcard/sefiles/SEWellness.db"; //개발용
    private static final int VERSION = 1;

    private TBCounter mTbCounter;
    private TBSetting mTbSetting;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        mTbCounter = new TBCounter(getWritableDatabase());
    }

    /**
     * 싱글턴 인스턴스 get 메소드
     * @param context
     * @return
     */
    public static DBHelper getInstance(Context context) {
        if (sDBHelper == null) {
            sDBHelper = new DBHelper(context.getApplicationContext());
        }
        return sDBHelper;
    }

    //테이블 반환
    public static TBCounter useCounter(Context context) {
        return getInstance(context).mTbCounter;
    }

    /**
     * DB 생성시
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TBCounter.CREATE);
        db.execSQL(TBSetting.CREATE);
    }

    /**
     * DB 버전 업그레이드시
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i("DBAdapter", "Upgrading from version " +
                oldVersion + " to " +
                newVersion + ", which will destroy all old data");
        switch (oldVersion) {
            case 1:
            case 2:
        }

        //재차 검증
        onCreate(db);
    }

    /**
     * writable한 DB를 get하는 메소드
     * @param context
     * @return
     */
    public static SQLiteDatabase getWritableDatabase(Context context) {
        return getInstance(context).getWritableDatabase();
    }

}
