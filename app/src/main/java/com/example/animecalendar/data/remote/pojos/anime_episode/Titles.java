
package com.example.animecalendar.data.remote.pojos.anime_episode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Titles {

    @SerializedName("en_jp")
    @Expose
    private String enJp;
    @SerializedName("en_us")
    @Expose
    private String enUs;
    @SerializedName("ja_jp")
    @Expose
    private String jaJp;

    public String getEnJp() {
        return enJp;
    }

    public void setEnJp(String enJp) {
        this.enJp = enJp;
    }

    public String getEnUs() {
        return enUs;
    }

    public void setEnUs(String enUs) {
        this.enUs = enUs;
    }

    public String getJaJp() {
        return jaJp;
    }

    public void setJaJp(String jaJp) {
        this.jaJp = jaJp;
    }

}
