package com.example.animecalendar.ui.assignment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.R;
import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.model.MyAnimeEpisodeListWithAnimeTitle;
import com.example.animecalendar.ui.main.MainActivityViewModel;
import com.example.animecalendar.utils.CustomTimeUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AssignmentFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;
    private List<Calendar> assignableDates;
    private String schedule;

    public AssignmentFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<List<MyAnimeEpisodeListWithAnimeTitle>> getEpisodes(int id) {
        return viewModel.getLocalRepository().getAnimeEpisodesToAssignDate(id);
    }

    List<Calendar> getAssignableDates() {
        return assignableDates;
    }

    void setAssignableDates(List<Calendar> assignableDates) {
        this.assignableDates = assignableDates;
    }

    void commitEpisodesDateAssignation(List<Calendar> days, List<MyAnimeEpisodeListWithAnimeTitle> caps) {
        viewModel.assignDateToEpisodes(days, caps);
    }

    void updateStatus(int id) {
        viewModel.getLocalRepository().updateAnimeStatus(LocalRepository.STATUS_FOLLOWING, id);
    }

    //TODO PROBAR
    String assignDateToEpisodes(List<Calendar> days, List<MyAnimeEpisodeListWithAnimeTitle> caps) {
        StringBuilder builder = new StringBuilder();
        float daysCount = (float) days.size();
        float capsCount = (float) caps.size();
        float capsPerDay = (capsCount / daysCount);

        float restAux = 0;
        float capsPerDayAux;
        float totalCapsAux = capsCount;

        if (capsCount >= daysCount) {
            for (int i = 0; i < daysCount; i++) {
                if (i == 0) { //día 1
                    builder.append(episodeResumed(i, (int) capsPerDay)).append("\n");
                    restAux = capsPerDay - ((int) capsPerDay); //parte decimal que pasa al día siguiente
                    totalCapsAux = totalCapsAux - (int) capsPerDay; //se resta X parte entera capítulo del total
                } else if (i != (daysCount - 1)) { //días entre el primero y el último
                    capsPerDayAux = capsPerDay + restAux;
                    builder.append(episodeResumed(i, (int) capsPerDayAux)).append("\n");
                    restAux = capsPerDayAux - ((int) capsPerDayAux); //parte decimal que pasa al día siguiente
                    totalCapsAux = totalCapsAux - ((int) capsPerDayAux); //se resta la parte entera del total de caps
                } else {
                    builder.append(episodeResumed(i, (int) totalCapsAux));
                }
            }
        }
        return builder.toString();
    }

    private String episodeResumed(int day, int caps) {
        return viewModel.getApplication().getResources().getString(R.string.asign_format, day + 1, caps);
    }

    String getSchedule() {
        return schedule;
    }

    void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
