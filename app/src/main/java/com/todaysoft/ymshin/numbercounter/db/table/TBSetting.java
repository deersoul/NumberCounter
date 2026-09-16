package com.todaysoft.ymshin.numbercounter.db.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class TBSetting {

    private SQLiteDatabase mDatabase;

    public static final String TABLE = "TB_SETTING"; //테이블명
    public static final String IS_VIBE = "IS_VIBE";  //진동여부
    public static final String IS_SOUND = "IS_SOUND";//사운드여부

    public TBSetting(SQLiteDatabase database) {
        this.mDatabase = database;
    }

    //테이블 CREATE문
    public static final String CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
                    IS_VIBE      + "       INTEGER   DEFAULT 0, " +
                    IS_SOUND     + "       INTEGER   DEFAULT 0); ";

    /**
     * 처음 설정 데이터 등록. 한 레코드 가지고 설정을 컨트롤 한다.
     * @return 성공여부
     */
    public long InsertFirstSetting() {
        ContentValues cv = new ContentValues();
        cv.put("IS_VIBE", 0);
        cv.put("IS_SOUND", 0);
        return mDatabase.insert(TABLE, null, cv);
    }

    /**
     * 설정 업데이트
     * @param isVibe
     * @param isSound
     * @return 성공여부
     */
    public long updateSetting(int isVibe, int isSound) {
        ContentValues cv = new ContentValues();
        cv.put("IS_VIBE", isVibe);
        cv.put("IS_SOUND", isSound);
        return mDatabase.update(TABLE, cv, "", new String[]{});
    }

}
