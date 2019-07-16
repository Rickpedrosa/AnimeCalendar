package com.example.animecalendar.ui.series;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.List;

import static com.example.animecalendar.ui.series.MyAnimeSeriesFragment.ALL_CATEGORY;

public class MyAnimeSeriesFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;
    private int itemPosition;
    private MutableLiveData<String> categoryTrigger = new MutableLiveData<>();


    public MyAnimeSeriesFragmentViewModel(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    LiveData<List<AnimesForSeries>> getAnimesToExposeByCategory() {
        return Transformations.switchMap(categoryTrigger, input -> {
            switch (input) {
                case ALL_CATEGORY:
                    return viewModel.getLocalRepository().getAnimesToExpose();
                case LocalRepository.STATUS_FOLLOWING:
                    return viewModel.getLocalRepository().getAnimesToExposeByCategory(LocalRepository.STATUS_FOLLOWING);
                case LocalRepository.STATUS_FINISHED:
                    return viewModel.getLocalRepository().getAnimesToExposeByCategory(LocalRepository.STATUS_FINISHED);
                case LocalRepository.STATUS_CURRENT:
                    return viewModel.getLocalRepository().getAnimesToExposeByCategory(LocalRepository.STATUS_CURRENT);
                case LocalRepository.STATUS_COMPLETED:
                    return viewModel.getLocalRepository().getAnimesToExposeByCategory(LocalRepository.STATUS_COMPLETED);
                default:
                    return viewModel.getLocalRepository().getAnimesToExpose();
            }
        });
    }

    int getItemPosition() {
        return itemPosition;
    }

    void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    void deleteAnime(int id) {
        viewModel.getLocalRepository().deleteAnime(id);
    }

    void updateStatus(@SuppressWarnings("SameParameterValue") String status, int id) {
        viewModel.getLocalRepository().updateAnimeStatus(status, id);
    }

    void setCategoryToLiveData(String val){
        categoryTrigger.setValue(val);
    }
}
