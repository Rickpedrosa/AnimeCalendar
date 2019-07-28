package com.example.animecalendar.ui.search;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.example.animecalendar.base.recycler.BaseListAdapter;
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
    private BaseListAdapter.OnItemClickListener usefulListener = (view, position) -> showConfirmation(position);
    private BaseListAdapter.OnItemClickListener whileLoadingListener = (view, position) ->
            Toast.makeText(requireContext(),
                    "Wait until the current anime is saved into the database",
                    Toast.LENGTH_SHORT).show();


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
        observaSearchStatus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.disposeObservable();
    }

    private void setupFab() {
        b.fab.setAlpha(0.25f);
        setupFabKeyboard();
        b.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(b.editSearch.getText().toString())) {
                    setupFabKeyboard();
                } else {
                    b.fab.setOnClickListener(v -> {
                        setupFabSearch();
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setupFabSearch() {
        if (!TextUtils.isEmpty(b.editSearch.getText().toString())) {
            viewModel.searchAnimes(b.editSearch.getText().toString());
            b.editSearch.getText().clear();
        } else {
            Toast.makeText(requireContext(), "EMPTY QUERY, IDIOT", Toast.LENGTH_SHORT).show();
            b.editSearch.getText().clear();
        }
        KeyboardUtils.hideSoftKeyboard(requireActivity());
        b.fab.setImageResource(R.drawable.ic_search_w_24dp);
    }

    private void setupFabKeyboard() {
        b.fab.setOnClickListener(v -> showKeyboardToSearch());
        b.fab.setImageResource(R.drawable.ic_keyboard_w_24dp);
    }

    private void setupToolbar() {
        NavigationUI.setupWithNavController(b.toolbarSearchFragment,
                navController,
                AppbarConfigProvider.getAppBarConfiguration());
    }

    private void setupTextView() {
        b.lblNoAnimesSearch.setOnClickListener(v -> showKeyboardToSearch());
    }

    private void showKeyboardToSearch() {
        b.editSearch.requestFocus();
        b.editSearch.getText().clear();
        KeyboardUtils.showSoftKeyboard(requireActivity());
    }

    private void setupRecyclerView() {
        listAdapter = new SearchFragmentViewAdapter();
        listAdapter.setOnItemClickListener(usefulListener);
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
                    b.editSearch.getText().clear();
                } else {
                    Toast.makeText(requireContext(), "EMPTY QUERY, IDIOT", Toast.LENGTH_SHORT).show();
                    b.editSearch.getText().clear();
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

    private void observaSearchStatus() {
        viewModel.getProgressBar().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                listAdapter.setOnItemClickListener(whileLoadingListener);
            } else {
                listAdapter.setOnItemClickListener(usefulListener);
            }
            b.listSearch.setAdapter(listAdapter);
        });
    }

    private void showTheSuccess() {
        Snackbar.make(b.lblNoAnimesSearch,
                listAdapter.getItem(viewModel.getItemPosition()).getCanonicalTitle() + " added! (fetching episodes)",
                Snackbar.LENGTH_LONG).show();
        b.editSearch.getText().clear();
    }

    private void showConfirmation(int position) {
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
                SearchFragment.this,
                1
        );
        yn.show(SearchFragment.this.requireFragmentManager(), "YesNoDialogFragment");
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
