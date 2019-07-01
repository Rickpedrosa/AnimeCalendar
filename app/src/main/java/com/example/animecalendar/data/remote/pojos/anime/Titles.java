
package com.example.animecalendar.data.remote.pojos.anime;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Titles {

    @SerializedName("en")
    @Expose
    private String en;
    @SerializedName("en_jp")
    @Expose
    private String enJp;
    @SerializedName("ja_jp")
    @Expose
    private String jaJp;
    @SerializedName("ko_kr")
    @Expose
    private String koKr;
    @SerializedName("th_th")
    @Expose
    private String thTh;
    @SerializedName("zh_cn")
    @Expose
    private String zhCn;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getEnJp() {
        return enJp;
    }

    public void setEnJp(String enJp) {
        this.enJp = enJp;
    }

    public String getJaJp() {
        return jaJp;
    }

    public void setJaJp(String jaJp) {
        this.jaJp = jaJp;
    }

    public String getKoKr() {
        return koKr;
    }

    public void setKoKr(String koKr) {
        this.koKr = koKr;
    }

    public String getThTh() {
        return thTh;
    }

    public void setThTh(String thTh) {
        this.thTh = thTh;
    }

    public String getZhCn() {
        return zhCn;
    }

    public void setZhCn(String zhCn) {
        this.zhCn = zhCn;
    }

}
