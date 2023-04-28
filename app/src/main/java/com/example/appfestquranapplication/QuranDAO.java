package com.example.appfestquranapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class QuranDAO extends SQLiteAssetHelper {
    public static final String SURAH_TABLE = "tsurah";
    public static final String PARAH_TABLE = "tparah";
    public static final String AYAT_TABLE = "tayah";
    public static final String AYAT_ID = "AyaID";
    public static final String PARAH_ID = "ParaID";
    public static final String SURAH_ID = "SuraID";
    public static final String ARABIC = "ArabicText";
    public static final String BOOKMARK = "Bookmark";
    public static final String [] TRANSLATE = {"DrMohsinKhan","MuftiTaqiUsmani","FatehMuhammadJalandhri","MehmoodulHassan"};

    private int TranslationEng;
    private int TranslationUrdu;
    //The Android's default system path of your application database.
    private static final String DATABASE_NAME = "Quran.db";
    private static final int DATABASE_VERSION = 1;

    public QuranDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void setTranslation(int eng, int urdu){
        this.TranslationEng = eng;
        this.TranslationUrdu = urdu;
    }

    public ArrayList<SurahNames> getSurahNames() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorSurah = db.rawQuery("SELECT * FROM " + SURAH_TABLE, null);

        ArrayList<SurahNames> surahArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorSurah.moveToFirst()) {
            do {
                int nameE, nameU, id;
                nameU = cursorSurah.getColumnIndex("SurahNameU");
                nameE = cursorSurah.getColumnIndex("SurahNameE");
                id = cursorSurah.getColumnIndex("SurahID");
                String nameEng = cursorSurah.getString(nameE);
                nameEng = nameEng.split("\\(")[0];
                surahArrayList.add(new SurahNames(cursorSurah.getString(nameU),
                        nameEng,
                        cursorSurah.getInt(id)));
            } while (cursorSurah.moveToNext());

        }

        cursorSurah.close();
        return surahArrayList;
    }

    public SurahNames getSurahById(int surahId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorSurah = db.rawQuery("SELECT * FROM " + SURAH_TABLE + " WHERE SurahID="+surahId, null);

        SurahNames surah ;

        // moving our cursor to first position.
        if (cursorSurah.moveToFirst()) {
            int nameE, nameU, id;
            nameU = cursorSurah.getColumnIndex("SurahNameU");
            nameE = cursorSurah.getColumnIndex("SurahNameE");
            id = cursorSurah.getColumnIndex("SurahID");
            String nameEng = cursorSurah.getString(nameE);
            nameEng = nameEng.split("\\(")[0];
            surah = new SurahNames(cursorSurah.getString(nameU),
                    nameEng,
                    cursorSurah.getInt(id));
        }
        else{
            surah = null;
        }

        cursorSurah.close();
        return surah;
    }

    public ArrayList<Ayat> getAyatBySurah(int id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorAyat;
        if(id == 9){
            cursorAyat = db.rawQuery("SELECT * FROM " + AYAT_TABLE + " WHERE " + SURAH_ID + "=" + id + " ORDER BY " + AYAT_ID,null);
        }
        else {
            cursorAyat = db.rawQuery("SELECT * FROM " + AYAT_TABLE + " WHERE " + SURAH_ID + "=" + id + " OR " + AYAT_ID + "=1 ORDER BY " + AYAT_ID,null);
        }

        ArrayList<Ayat> ayatArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorAyat.moveToFirst()) {
            do {
                int arabic = cursorAyat.getColumnIndex(ARABIC);
                int translateEng = cursorAyat.getColumnIndex(TRANSLATE[TranslationEng]);
                int translateUrdu = cursorAyat.getColumnIndex(TRANSLATE[TranslationUrdu+2]);
                int num = cursorAyat.getColumnIndex("AyaNo");
                int ayaNo = cursorAyat.getInt(num);
                int surah = cursorAyat.getColumnIndex(SURAH_ID);
                int Id = cursorAyat.getColumnIndex(AYAT_ID);
                int ayaId = cursorAyat.getInt(Id);
                if(ayaId == 1)
                    ayaNo = 0;

                int bookMarkCol = cursorAyat.getColumnIndex(BOOKMARK);
                boolean bookmark = cursorAyat.getInt(bookMarkCol) != 0;
                ayatArrayList.add(new Ayat(cursorAyat.getString(arabic),cursorAyat.getString(translateEng),cursorAyat.getString(translateUrdu),ayaNo,cursorAyat.getInt(surah),bookmark));
            } while (cursorAyat.moveToNext());

        }

        cursorAyat.close();
        return ayatArrayList;
    }

    public void setBookmark(int surahId, int ayaNo){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(BOOKMARK, 1);
        db.update(AYAT_TABLE,cv,SURAH_ID +"="+surahId+" AND AyaNo="+ayaNo,null);
    }

    public void unsetBookmark(int surahId, int ayaNo){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(BOOKMARK, 0);
        db.update(AYAT_TABLE,cv,SURAH_ID +"="+surahId+" AND AyaNo="+ayaNo,null);
    }

    public ArrayList<Ayat> getAyatByBookmark(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorAyat = db.rawQuery("SELECT "+ARABIC+", "+SURAH_ID+", SurahNameU, AyaNo FROM " + AYAT_TABLE + ","+SURAH_TABLE+" WHERE " + BOOKMARK + "=1 AND "+SURAH_ID+"=SurahID ORDER BY " + AYAT_ID,null);

        ArrayList<Ayat> ayatArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorAyat.moveToFirst()) {
            do {
                int arabic = cursorAyat.getColumnIndex(ARABIC);
                int nameUrdu = cursorAyat.getColumnIndex("SurahNameU");
                int num = cursorAyat.getColumnIndex("AyaNo");
                int ayaNo = cursorAyat.getInt(num);
                int surah = cursorAyat.getColumnIndex(SURAH_ID);

                ayatArrayList.add(new Ayat(cursorAyat.getString(arabic),null,cursorAyat.getString(nameUrdu),ayaNo,cursorAyat.getInt(surah),true));
            } while (cursorAyat.moveToNext());

        }

        cursorAyat.close();
        return ayatArrayList;
    }
}