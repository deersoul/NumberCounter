package com.todaysoft.ymshin.numbercounter.db.table;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.todaysoft.ymshin.numbercounter.vo.CounterVO;

import java.util.ArrayList;
import java.util.List;

/**
 * TB_COUNTER 테이블
 * @author ymshin
 * @Date 2019-02-04
 */
public class TBCounter {

    private SQLiteDatabase mDatabase;

    public static final String TABLE = "TB_COUNTER"; //테이블명
    public static final String SEQ = "SEQ";           //시퀀스
    public static final String NAME = "NAME";         //카운터 이름
    public static final String VALUE = "VALUE";       //카운터 값
    public static final String INS_DATE = "INS_DATE";//생성일자
    public static final String MOD_DATE = "MOD_DATE";//수정일자

    public TBCounter(SQLiteDatabase database) {
        this.mDatabase = database;
    }

    //테이블 CREATE문
    public static final String CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
                    SEQ       + "       INTEGER    PRIMARY KEY AUTOINCREMENT, " +
                    NAME      + "       DATETIME   NOT NULL, " +
                    VALUE     + "       TEXT, " +
                    INS_DATE + "       DATETIME    DEFAULT (datetime('now', 'localtime')), " +
                    MOD_DATE + "       DATETIME    DEFAULT (datetime('now', 'localtime')));";

    public long insertCounterData(String name, String value) {
        ContentValues cv = new ContentValues();
        cv.put("NAME", name);
        cv.put("VALUE", value);
        return mDatabase.insert(TABLE, null, cv);
    }

    /**
     * 카운터 데이터를 DB에서 읽어서 가져온다.
     * @return 카운터 데이터
     */
    @SuppressLint("Range")
    public List<CounterVO> selectCounterData() {
        Cursor c = mDatabase.rawQuery("SELECT SEQ, NAME, VALUE FROM TB_COUNTER " , new String[]{});
        List<CounterVO> list = new ArrayList<CounterVO>();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                CounterVO vo = new CounterVO();
                vo.setSeq(c.getInt(c.getColumnIndex("SEQ")));
                vo.setName(c.getString(c.getColumnIndex("NAME")));
                vo.setValue(c.getString(c.getColumnIndex("VALUE")));
                list.add(vo);
            }
            c.close();
            return list;
        } else {
            c.close();
            return list;
        }
    }

    /**
     * 시퀀스 번호로 카운터 데이터 상세 조회
     * @param seq
     * @return
     */
    @SuppressLint("Range")
    public CounterVO selectCounterDataDetail(String seq) {
        Cursor c = mDatabase.rawQuery("SELECT SEQ, NAME, VALUE FROM TB_COUNTER WHERE SEQ = ?" , new String[]{seq});
        CounterVO vo = new CounterVO();
        if (c.getCount() > 0) {
            c.moveToNext();
            vo.setSeq(c.getInt(c.getColumnIndex("SEQ")));
            vo.setName(c.getString(c.getColumnIndex("NAME")));
            vo.setValue(c.getString(c.getColumnIndex("VALUE")));
        }
        c.close();
        return vo;
    }

    /**
     * 시퀀스 번호로 테이블 데이터 삭제
     * @param seq
     */
    public void deleteCounterData(String seq) {
        mDatabase.delete(TABLE, "SEQ = ?", new String[]{seq});
    }

    /**
     * 최근 시퀀스를 가져온다. 저장 후 set해주기 위해
     * @return 최근 시퀀스
     */
    @SuppressLint("Range")
    public int selectRecentSeq() {
        int result = 0;
        Cursor c = mDatabase.rawQuery("SELECT SEQ FROM TB_COUNTER ORDER BY SEQ DESC", new String[]{});
        c.moveToNext();
        result = c.getInt(c.getColumnIndex(SEQ));
        c.close();
        return result;
    }

    /**
     * 카운터 데이터 업데이트
     * @param seq
     * @param countNm
     * @param countVal
     */
    public void updateCounterData(String seq, String countNm, String countVal) {
        ContentValues cv = new ContentValues();
        cv.put("NAME", countNm);
        cv.put("VALUE", countVal);
        cv.put("SEQ", seq);
        mDatabase.update(TABLE, cv, "SEQ = ?", new String[]{seq});
    }
}
