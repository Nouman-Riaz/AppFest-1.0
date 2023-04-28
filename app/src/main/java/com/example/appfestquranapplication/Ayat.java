package com.example.appfestquranapplication;

public class Ayat {
    private int ayatId;
    private int surahId;
    private String arabic;
    private String translateEng;
    private String translateUrdu;
    private int parahId;
    private int ayatNo;
    private boolean bookmark;

    public boolean isBookmark() {
        return bookmark;
    }

    public int getAyatId() {
        return ayatId;
    }

    public int getSurahId() {
        return surahId;
    }

    public String getArabic() {
        return arabic;
    }

    public String getTranslateEng() {
        return translateEng;
    }

    public String getTranslateUrdu() {
        return translateUrdu;
    }

    public int getParahId() {
        return parahId;
    }

    public int getAyatNo() {
        return ayatNo;
    }

    public Ayat(String arabic, String translateEng, String translateUrdu, int ayatNo, int surahId, boolean bookmark) {
        this.surahId = surahId;
        this.arabic = arabic;
        this.translateEng = translateEng;
        this.translateUrdu = translateUrdu;
        this.ayatNo = ayatNo;
        this.bookmark = bookmark;
    }
}
