package com.example.animecalendar.ui.calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.model.CalendarAnimeEpisodes;
import com.example.animecalendar.model.CalendarAnimeEpisodesRecycled;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class CalendarFragmentViewModel extends ViewModel {
    private final MainActivityViewModel viewModel;

    public CalendarFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<List<CalendarAnimeEpisodes>> getAnimeWithEpisodes() {
        return viewModel.getLocalRepository().getAnimeEpisodesForCalendar();
    }

    void updateEpisodeStatus(int value, int episodeId) {
        viewModel.getLocalRepository().updateEpisodeStatus(value, episodeId);
    }

    void updateEpisodeViewType(int viewType, int episodeId) {
        viewModel.getLocalRepository().updateEpisodeViewType(viewType, episodeId);
    }

    void updateEpisodeCollapse(int collapse, int episodeId) {
        viewModel.getLocalRepository().updateEpisodeCollapse(collapse, episodeId);
    }


//    List<CalendarAnimeEpisodesRecycled> listFormatted(List<CalendarAnimeEpisodes> eps) {
//        int id_marker;
//        int id_temp = -1;
//        List<CalendarAnimeEpisodes> temp = new ArrayList<>(eps);
//        for (int i = 0; i < temp.size(); i++) {
//            if (temp.get(i).getNumber() == 1 && temp.get(i).getAnimeId() != id_temp) {
//                id_marker = 0;
//            } else {
//                id_marker = i;
//            }
//            if (id_marker == 0) {
//                temp.add(i, temp.get(i));
//            }
//            id_temp = temp.get(i).getAnimeId();
//        }
//
//        id_temp = -1;
//        List<CalendarAnimeEpisodesRecycled> finalList = new ArrayList<>();
//        CalendarAnimeEpisodesRecycled item;
//        for (int i = 0; i < temp.size(); i++) {
//            item = new CalendarAnimeEpisodesRecycled(
//                    temp.get(i).getAnimeId(),
//                    temp.get(i).getAnimeTitle(),
//                    temp.get(i).getEpisodeId(),
//                    temp.get(i).getTitle(),
//                    temp.get(i).getLength(),
//                    temp.get(i).getNumber(),
//                    temp.get(i).getWatchToDate(),
//                    temp.get(i).getWasWatched()
//            );
//            if (temp.get(i).getNumber() == 1 && temp.get(i).getAnimeId() != id_temp) {
//                item.setViewtype(CalendarFragmentViewAdapter.ANIME_TYPE);
//                item.setCollapse(CalendarAnimeEpisodesRecycled.EXPAND_TITLE);
//            } else {
//                item.setViewtype(CalendarFragmentViewAdapter.EPISODE_TYPE);
//                item.setCollapse(CalendarAnimeEpisodesRecycled.DUMMY_COLLAPSE);
//            }
//            id_temp = temp.get(i).getAnimeId();
//            finalList.add(item);
//        }
//        return finalList;
//    }
}
