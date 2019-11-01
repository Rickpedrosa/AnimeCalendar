
package com.example.animecalendar.data.remote.pojos.anime_character_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Names {

    @SerializedName("en")
    @Expose
    private String en;
    @SerializedName("ja_jp")
    @Expose
    private String jaJp;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getJaJp() {
        return jaJp;
    }

    public void setJaJp(String jaJp) {
        this.jaJp = jaJp;
    }

}
