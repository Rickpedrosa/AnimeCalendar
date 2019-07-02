package com.example.animecalendar.ui.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.animecalendar.R;
import com.example.animecalendar.databinding.FragmentSearchBinding;
import com.example.animecalendar.providers.VMProvider;
import com.example.animecalendar.utils.KeyboardUtils;

public class SearchFragment extends Fragment {

    private NavController navController;
    private FragmentSearchBinding b;
    private SearchFragmentViewModel viewModel;
    private SearchFragmentViewAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = androidx.lifecycle.ViewModelProviders.of(this,
                VMProvider.viewModelFragmentFactory(this,
                        VMProvider.FRAGMENTS.SEARCH))
                .get(SearchFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        setupProgressBar();
        setupToolbar();
        setupRecyclerView();
        setupSearchText();
        observeAnimeData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.disposeObservable();
    }

    private void setupProgressBar() {
//        b.progressCircular.setVisibility(View.INVISIBLE);
//        b.lblEmpty.setText(getResources().getString(R.string.lbl_search));
    }

    private void setupToolbar() {
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.myAnimeSeriesFragment,
                        R.id.calendarFragment,
                        R.id.searchFragment)
                        .build();
        NavigationUI.setupWithNavController(b.toolbarSearchFragment,
                navController,
                appBarConfiguration);
    }

    private void setupRecyclerView() {
        listAdapter = new SearchFragmentViewAdapter();
        b.listSearch.setHasFixedSize(true);
        b.listSearch.setItemAnimator(new DefaultItemAnimator());
        b.listSearch.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.listSearch.setAdapter(listAdapter);
    }

    private void setupSearchText() {
        b.editSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardUtils.hideSoftKeyboard(requireActivity());
                if (!TextUtils.isEmpty(b.editSearch.getText().toString())) {
                    viewModel.searchAnimes(b.editSearch.getText().toString());
                } else {
                    Toast.makeText(requireContext(), "EMPTY QUERY, IDIOT", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });
    }

    private void observeAnimeData() {
        viewModel.getAnimeList().observe(getViewLifecycleOwner(), myAnimes ->
        {
//            b.lblEmpty.setVisibility(myAnimes.size() == 0 ? View.VISIBLE : View.INVISIBLE);
            listAdapter.submitList(myAnimes);
        });
//        viewModel.progressBarController().observe(getViewLifecycleOwner(), aBoolean ->
//                b.progressCircular.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE));
    }
}
