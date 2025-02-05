package com.example.animecalendar.ui.search;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.animecalendar.R;
import com.example.animecalendar.base.Resource;
import com.example.animecalendar.data.local.entity.MyAnime;
import com.example.animecalendar.data.remote.pojos.animelist.AnimationList;
import com.example.animecalendar.data.remote.pojos.animelist.Datum;
import com.example.animecalendar.data.remote.services.AnimeService;
import com.example.animecalendar.ui.main.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SearchFragmentViewModel extends ViewModel {

    private final MainActivityViewModel viewModel;
    private MutableLiveData<List<MyAnime>> animeList = new MutableLiveData<>();
    private Disposable disposable;
    private int itemPosition;

    public SearchFragmentViewModel(MainActivityViewModel mainActivityViewModel) {
        this.viewModel = mainActivityViewModel;
    }

    void searchAnimes(String title) {
        disposable = viewModel.getAnimeRepository().getAnimesByFilterText(title, AnimeService.LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> startLoading())
                .subscribe(this::onRetroNext, this::onRetroError, this::stopLoading);
    }

    private void onRetroError(Throwable throwable) {
        Toast.makeText(viewModel.getApplication(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        stopLoading();
    }

    private void onRetroNext(Response<AnimationList> resp) {
        if (resp.isSuccessful() && Objects.requireNonNull(resp.body()).getData().size() != 0) {
            submitAnimeList(resp.body().getData());
        } else if (Objects.requireNonNull(resp.body()).getData().size() == 0) {
            Toast.makeText(viewModel.getApplication(), viewModel.getApplication().getResources().getString(R.string.no_animes_found), Toast.LENGTH_LONG).show();
        }
    }

    LiveData<List<MyAnime>> getAnimeList() {
        return animeList;
    }

    LiveData<Boolean> getProgressBar() {
        return viewModel.getProgressBarController();
    }

    void disposeObservable() {
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    void addAnimeToDatabase(MyAnime anime) {
        viewModel.getLocalRepository().addAnime(anime);
        viewModel.retrieveRetroEpisodes((int) anime.getId(), false);
    }

    int getItemPosition() {
        return itemPosition;
    }

    void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    //INNER METHODS ------------------------------------------------------------------------------

    private void submitAnimeList(List<Datum> listResponse) {
        List<MyAnime> am = new ArrayList<>();
        int id, epcount;
        float avg;
        String synopsis, title, status, ptiny, pmedium, ctiny;
        for (int i = 0; i < Objects.requireNonNull(listResponse).size(); i++) {
            id = Integer.parseInt(listResponse.get(i).getId());

            if (listResponse.get(i).getAttributes().getEpisodeCount() == null) {
                epcount = 0;
            } else {
                epcount = listResponse.get(i).getAttributes().getEpisodeCount();
            }

            if (listResponse.get(i).getAttributes().getAverageRating() == null) {
                avg = 0f;
            } else {
                avg = Float.parseFloat(listResponse.get(i).getAttributes().getAverageRating());
            }

            if (listResponse.get(i).getAttributes().getSynopsis() == null) {
                synopsis = "-";
            } else {
                synopsis = listResponse.get(i).getAttributes().getSynopsis();
            }

            if (listResponse.get(i).getAttributes().getCanonicalTitle() == null) {
                title = "-";
            } else {
                title = listResponse.get(i).getAttributes().getCanonicalTitle();
            }

            if (listResponse.get(i).getAttributes().getStatus() == null) {
                status = "-";
            } else {
                status = listResponse.get(i).getAttributes().getStatus();
            }

            if (listResponse.get(i).getAttributes().getPosterImage() == null) {
                ptiny = "-";
                pmedium = "-";
            } else {
                ptiny = listResponse.get(i).getAttributes().getPosterImage().getTiny();
                pmedium = listResponse.get(i).getAttributes().getPosterImage().getLarge();
            }

            if (listResponse.get(i).getAttributes().getCoverImage() == null) {
                ctiny = "-";
            } else {
                ctiny = listResponse.get(i).getAttributes().getCoverImage().getTiny();
            }
            am.add(new MyAnime(
                    id,
                    synopsis,
                    title,
                    status,
                    avg,
                    ptiny,
                    pmedium,
                    ctiny,
                    epcount
            ));
        }
        animeList.postValue(am);
    }

    private void startLoading() {
        viewModel.progressBarLoading();
    }

    private void stopLoading() {
        viewModel.progressBarStop();
    }
}
