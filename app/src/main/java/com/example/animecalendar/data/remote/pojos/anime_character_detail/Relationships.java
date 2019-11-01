
package com.example.animecalendar.data.remote.pojos.anime_character_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Relationships {

    @SerializedName("primaryMedia")
    @Expose
    private PrimaryMedia primaryMedia;
    @SerializedName("castings")
    @Expose
    private Castings castings;
    @SerializedName("mediaCharacters")
    @Expose
    private MediaCharacters mediaCharacters;
    @SerializedName("quotes")
    @Expose
    private Quotes quotes;

    public PrimaryMedia getPrimaryMedia() {
        return primaryMedia;
    }

    public void setPrimaryMedia(PrimaryMedia primaryMedia) {
        this.primaryMedia = primaryMedia;
    }

    public Castings getCastings() {
        return castings;
    }

    public void setCastings(Castings castings) {
        this.castings = castings;
    }

    public MediaCharacters getMediaCharacters() {
        return mediaCharacters;
    }

    public void setMediaCharacters(MediaCharacters mediaCharacters) {
        this.mediaCharacters = mediaCharacters;
    }

    public Quotes getQuotes() {
        return quotes;
    }

    public void setQuotes(Quotes quotes) {
        this.quotes = quotes;
    }

}
