
package com.example.animecalendar.data.remote.pojos.anime_episode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinksPagination {

    @SerializedName("first")
    @Expose
    private String first;
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("last")
    @Expose
    private String last;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

}
