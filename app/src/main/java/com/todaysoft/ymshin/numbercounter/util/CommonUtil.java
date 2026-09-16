package com.todaysoft.ymshin.numbercounter.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.todaysoft.ymshin.numbercounter.R;

import org.apache.commons.lang3.time.FastDateFormat;

import java.security.MessageDigest;
import java.text.ParseException;
import java.util.Date;

/**
 * 공통 유틸 클래스
 * @author ymshin
 * @Date 2019-02-04
 */
public class CommonUtil {

    /**
     * 퍼미션 요청이 필요한지 체크
     */
    public static boolean needRequestPermission(Context context, String[] permissions) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }

        for (int i = 0; i < permissions.length; i++) {
            int grant = ContextCompat.checkSelfPermission(context, permissions[i]);
            // 권한 없는 퍼미션 존재
            if (grant != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 퍼미션 요청 결과가 모두 허용인지 체크
     */
    public static boolean isAllGranted(int[] grantResults) {

        if(grantResults == null || grantResults.length == 0) {
            return false;
        }
        for (int i = 0; i < grantResults.length; i++) {
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 저장 팝업창 RelativeLayout 가져오기
     * @param context
     * @param countNm
     * @param countVal
     * @return
     */
    public static RelativeLayout getViewForSave(Context context, String countNm, String countVal) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_save, null);
        EditText etCountNm = (EditText) layout.findViewById(R.id.etCountNm);
        EditText etCountVal = (EditText) layout.findViewById(R.id.etCountVal);

        //값 기본 세팅
        etCountVal.setText(countVal);
       // NumberPicker num1 = (NumberPicker) layout.findViewById(R.id.num1);

        return layout;
    }

    /**
     * millseconds를 yyyy-MM-dd 형식의 문자열로 변환
     * @param millis
     * @return 변환된 날짜 문자열
     */
    public static String convertDateStr(long millis) {
        return FastDateFormat.getInstance("yyyy-MM-dd").format(millis);
    }

    /**
     * milliseconds를 yyyy-MM-dd HH:mm:ss 형식의 문자열로 변환
     * @param millis
     * @return 변환된 날짜 문자열
     */
    public static String convertDateTimeStr(long millis) {
        return FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").format(millis);
    }

    /**
     * 글 긴 경우 ...붙이기
     * @param temp
     * @return ...을 붙인 문자열
     */
    public static String stringDot(String temp, int num) {
        if (temp.length() > num) {
            temp = temp.substring(0, num) + "...";
        }
        return temp;
    }

    /**
     * sha256 헥사 암호화 값 반환
     * @param str
     * @return 단방향 암호화된 문자열
     */
    public static String getHmacSha256(String str) {
        byte[] binary = null;
        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes("UTF-8"));
            binary = sh.digest();
        }catch(Exception e){
            e.printStackTrace();
        }

        return binaryToHex(binary);
    }

    /**
     * 바이트배열을 헥사값으로 변환
     * @param ba
     * @return 헥사값
     */
    public static String binaryToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    }

    /**
     * 'yyyy-mm-dd'형식의 날짜를 long 형의 utc로 반환한다.
     * @param day
     * @return utc
     */
    public static long dateToUtc(String day) {
        return convertTimeMillis(day + " 00:00:00");
    }

    /**
     * utc를 HH:mm:ss의 시간값만 String으로 반환한다.
     * @param utc
     * @return
     */
    public static String utcToTime(long utc) {
        return FastDateFormat.getInstance("HH:mm:ss").format(utc);
    }

    /**
     * String을 millseconds로 변환한다.
     * @param datetime
     * @return millseconds로 변환된 시간 값
     */
    public static long convertTimeMillis(String datetime) {

        try {
            Date origin = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(datetime);
            return origin.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
