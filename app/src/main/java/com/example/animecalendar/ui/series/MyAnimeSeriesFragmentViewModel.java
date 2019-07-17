package com.example.animecalendar.ui.series;

import android.annotation.SuppressLint;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.base.Event;
import com.example.animecalendar.data.local.LocalRepository;
import com.example.animecalendar.data.remote.pojos.anime.Anime;
import com.example.animecalendar.data.remote.pojos.anime_episode.AnimeEpisode;
import com.example.animecalendar.model.AnimesForSeries;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.List;
import java.util.Objects;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.example.animecalendar.ui.series.MyAnimeSeriesFragment.ALL_CATEGORY;

public class MyAnimeSeriesFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;
    private int itemPosition;
    private Disposable disposable;
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

    void setCategoryToLiveData(String val) {
        categoryTrigger.setValue(val);
    }

    void checkIfCanBeUpdated(AnimesForSeries animesForSeries) {
        viewModel.checkIfCanBeUpdated(animesForSeries);
    }

    LiveData<Event<Boolean>> getUpdateTrigger() {
        return viewModel.getUpdateTrigger();
    }

    LiveData<String> getCategoryTrigger() {
        return categoryTrigger;
    }
}
