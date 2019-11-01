
package com.example.animecalendar.data.remote.pojos.anime_character_detail;

import com.example.animecalendar.data.remote.pojos.anime.Dimensions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("dimensions")
    @Expose
    private Dimensions dimensions;

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

}
