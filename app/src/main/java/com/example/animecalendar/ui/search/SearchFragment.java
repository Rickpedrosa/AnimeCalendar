package com.example.animecalendar.ui.search;

import android.content.DialogInterface;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animecalendar.R;
import com.example.animecalendar.base.dialogs.YesNoDialogFragment;
import com.example.animecalendar.databinding.FragmentSearchBinding;
import com.example.animecalendar.providers.AppbarConfigProvider;
import com.example.animecalendar.providers.VMProvider;
import com.example.animecalendar.utils.KeyboardUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class SearchFragment extends Fragment implements YesNoDialogFragment.Listener {

    private NavController navController;
    private FragmentSearchBinding b;
    private SearchFragmentViewModel viewModel;
    private SearchFragmentViewAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(requireActivity(),
                VMProvider.viewModelFragmentFactory(requireActivity(),
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
        setupToolbar();
        setupRecyclerView();
        setupSearchText();
        setupFab();
        setupTextView();
        observeAnimeData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.disposeObservable();
    }

    private void setupFab() {
        b.fab.setAlpha(0.25f);
        b.fab.setOnClickListener(v -> showKeyboardToSearch());
    }

    private void setupToolbar() {
        NavigationUI.setupWithNavController(b.toolbarSearchFragment,
                navController,
                AppbarConfigProvider.getAppBarConfiguration());
    }

    private void setupTextView() {
        b.lblNoAnimesSearch.setOnClickListener(v -> showKeyboardToSearch());
    }

    private void showKeyboardToSearch(){
        b.editSearch.requestFocus();
        b.editSearch.getText().clear();
        KeyboardUtils.showSoftKeyboard(requireActivity());
    }

    private void setupRecyclerView() {
        listAdapter = new SearchFragmentViewAdapter();
        listAdapter.setOnItemClickListener((view, position) -> {
            String format = "Add %s (%d eps) to your list?";
            viewModel.setItemPosition(position);
            YesNoDialogFragment yn = YesNoDialogFragment.newInstance(
                    listAdapter.getItem(position).getCanonicalTitle(),
                    String.format(Locale.US,
                            format,
                            listAdapter.getItem(position).getCanonicalTitle(),
                            listAdapter.getItem(position).getEpisodeCount()),
                    "YEA BOI",
                    "NAY BOI",
                    this,
                    1
            );
            yn.show(requireFragmentManager(), "YesNoDialogFragment");
        });
        //b.listSearch.setHasFixedSize(true);
        b.listSearch.setItemAnimator(new DefaultItemAnimator());
        b.listSearch.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.VERTICAL));
        b.listSearch.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.listSearch.setAdapter(listAdapter);
    }

    private void setupSearchText() {
        b.editSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
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
            b.lblNoAnimesSearch.setVisibility(myAnimes.size() == 0 ? View.VISIBLE : View.GONE);
            listAdapter.submitList(myAnimes);
        });
    }

    private void showTheSuccess() {
        Snackbar.make(b.lblNoAnimesSearch,
                listAdapter.getItem(viewModel.getItemPosition()).getCanonicalTitle() + " added! (fetching episodes)",
                Snackbar.LENGTH_LONG).show();
        b.editSearch.getText().clear();
    }

    @Override
    public void onPositiveButtonClick(DialogInterface dialog) {
        viewModel.addAnimeToDatabase(listAdapter.getItem(viewModel.getItemPosition()));
        showTheSuccess();
    }

    @Override
    public void onNegativeButtonClick(DialogInterface dialog) {
        dialog.dismiss();
    }
}
