
package com.example.animecalendar.data.remote.pojos.anime_character_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnimeCharacterDetail {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
