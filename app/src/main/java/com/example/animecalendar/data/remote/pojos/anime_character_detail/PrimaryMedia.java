
package com.example.animecalendar.data.remote.pojos.anime_character_detail;

import com.example.animecalendar.data.remote.pojos.anime.Links;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrimaryMedia {

    @SerializedName("links")
    @Expose
    private Links links;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

}
