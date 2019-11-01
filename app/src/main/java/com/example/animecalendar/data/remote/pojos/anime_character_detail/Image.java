
package com.example.animecalendar.data.remote.pojos.anime_character_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("original")
    @Expose
    private String original;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}
