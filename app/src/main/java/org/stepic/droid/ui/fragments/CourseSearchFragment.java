package org.stepic.droid.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import org.stepic.droid.base.App;
import org.stepic.droid.core.presenters.SearchCoursesPresenter;
import org.stepic.droid.storage.operations.Table;

import javax.inject.Inject;

public class CourseSearchFragment extends CourseListFragmentBase {
    public static Fragment newInstance(String query) {
        Fragment fragment = new CourseSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CourseSearchFragment.QUERY_KEY, query);
        fragment.setArguments(bundle);
        return fragment;
    }

    private final static String QUERY_KEY = "query_key";

    private String searchQuery;

    @Inject
    SearchCoursesPresenter searchCoursesPresenter;

    @Override
    protected void injectComponent() {
        App.Companion
                .componentManager()
                .courseGeneralComponent()
                .courseListComponentBuilder()
                .build()
                .inject(this);
    }

    @Override
    protected void onReleaseComponent() {
        App.Companion
                .componentManager()
                .releaseCourseGeneralComponent();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        searchQuery = getArguments().getString(QUERY_KEY);
        super.onViewCreated(view, savedInstanceState);
        emptySearch.setClickable(false);
        emptySearch.setFocusable(false);
        searchCoursesPresenter.attachView(this);
        searchCoursesPresenter.restoreState();
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                searchCoursesPresenter.downloadData(searchQuery);
            }
        });

    }

    @Override
    public void onDestroyView() {
        searchCoursesPresenter.detachView(this);
        super.onDestroyView();
    }

    @Override
    protected Table getCourseType() {
        return null;
    }

    @Override
    public void showEmptyScreen(boolean isShowed) {
        if (isShowed) {
            emptySearch.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        } else {
            emptySearch.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onNeedDownloadNextPage() {
        searchCoursesPresenter.downloadData(searchQuery);
    }

    @Override
    public void onRefresh() {
        searchCoursesPresenter.refreshData(searchQuery);
    }
}
