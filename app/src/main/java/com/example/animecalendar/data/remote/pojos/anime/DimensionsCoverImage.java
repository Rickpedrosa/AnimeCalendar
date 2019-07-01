
package com.example.animecalendar.data.remote.pojos.anime;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DimensionsCoverImage {

    @SerializedName("tiny")
    @Expose
    private TinyCoverImage tiny;
    @SerializedName("small")
    @Expose
    private SmallCoverImage small;
    @SerializedName("large")
    @Expose
    private LargeCoverImage large;

    public TinyCoverImage getTiny() {
        return tiny;
    }

    public void setTiny(TinyCoverImage tiny) {
        this.tiny = tiny;
    }

    public SmallCoverImage getSmall() {
        return small;
    }

    public void setSmall(SmallCoverImage small) {
        this.small = small;
    }

    public LargeCoverImage getLarge() {
        return large;
    }

    public void setLarge(LargeCoverImage large) {
        this.large = large;
    }

}
