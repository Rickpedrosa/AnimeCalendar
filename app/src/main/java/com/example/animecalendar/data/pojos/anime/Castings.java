
package com.example.animecalendar.data.pojos.anime;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Castings {

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
