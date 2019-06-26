
package com.example.animecalendar.data.pojos.anime;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnimeStaff {

    @SerializedName("links")
    @Expose
    private LinksRelated links;

    public LinksRelated getLinks() {
        return links;
    }

    public void setLinks(LinksRelated links) {
        this.links = links;
    }

}
