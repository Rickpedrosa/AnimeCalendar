package com.example.animecalendar.data.remote.pojos.anime_character_ids;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnimeCharacterIDs {
    @SerializedName("links")
    @Expose
    private LinksCharacter links;
    @SerializedName("data")
    @Expose
    private List<DatumCharacter> data = null;

    public LinksCharacter getLinks() {
        return links;
    }

    public void setLinks(LinksCharacter links) {
        this.links = links;
    }

    public List<DatumCharacter> getData() {
        return data;
    }

    public void setData(List<DatumCharacter> data) {
        this.data = data;
    }
}
