package com.layar.islam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.layar.helpers.EmptyRecyclerView;
import com.layar.helpers.EndlessRecyclerViewScrollListener;

import java.util.List;

/**
 * Created by melvin on 06/06/2017.
 * Shows a list of News Articles that belong to a category
 */
public class ArticlesInCategoryFragment extends Fragment {
    Context context;
    private EmptyRecyclerView mRecyclerView;
    private EmptyRecyclerView.Adapter mAdapter;
    private EmptyRecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout swipeLayout;
    int categoryId = 0;

    public final static int LIST_INITIAL_LOAD = 10;
    public final static int LIST_INITIAL_LOAD_MORE_ONSCROLL = 5;
    EndlessRecyclerViewScrollListener scrollListener;
    List<NewsArticle> articles;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        mRecyclerView = (EmptyRecyclerView) rootView.findViewById(R.id.list);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);
        RelativeLayout empty = (RelativeLayout) rootView.findViewById(R.id.empty);
        mRecyclerView.setEmptyView(empty);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();

        //set RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        int spanCount = 1;
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //handle when user scrolls more than the items on screen
        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager, spanCount) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                System.out.println("load more" + totalItemsCount);
                loadMore(totalItemsCount);
            }

            @Override
            public void onScroll(RecyclerView view, int dx, int dy) {

            }
        };
        mRecyclerView.addOnScrollListener(scrollListener);


        //get category Id from
        categoryId = getArguments().getInt("Category_id", 0);

        //load category from server
        Category.getCategoryName(context, categoryId, new Category.onNameFoundListener() {
            @Override
            public void onNameFound(String name) {
                getActivity().setTitle(name);
            }
        });

        // Swipe to Refresh
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        refresh();
    }

    /**
     * Refresh article list from server
     */
    public void refresh() {
        NewsArticle.loadMultiple(getActivity(), 0, LIST_INITIAL_LOAD, "", "" + categoryId, new NewsArticle.onMultipleDownloadedListener() {
            @Override
            public void onDownloaded(List<NewsArticle> articles) {
                swipeLayout.setRefreshing(false);
                setArticles(articles);
            }
        });
    }

    /**
     * Load more articles from server
     *
     * @param first - start loading from this article
     */
    public void loadMore(int first) {
        NewsArticle.loadMultiple(getActivity(), first, LIST_INITIAL_LOAD_MORE_ONSCROLL, "", "" + categoryId, new NewsArticle.onMultipleDownloadedListener() {
            @Override
            public void onDownloaded(List<NewsArticle> articles) {
                swipeLayout.setRefreshing(false);
                ((NewsArticlesAdapter) mAdapter).addItems(articles);
                mRecyclerView.swapAdapter(mAdapter, false);
            }
        });
    }


    /**
     * Show articles to screen
     *
     * @param articles_loaded - list of articles to show
     */
    public void setArticles(final List<NewsArticle> articles_loaded) {
        this.articles = articles_loaded;
        mAdapter = new NewsArticlesAdapter(articles, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //open article in new activity on click
                Intent intent = new Intent(context, SingleNewsArticleActivity.class);
                intent.putExtra(SingleNewsArticleActivity.ITEM_LIST_KEY, getIds());
                intent.putExtra(SingleNewsArticleActivity.POSITION, i);
                intent.putExtra(SingleNewsArticleActivity.TITLE, getActivity().getTitle());
                startActivity(intent);
            }
        }, context);
        mRecyclerView.setAdapter(mAdapter);
        scrollListener.resetState();
    }

    /**
     * Get the ids of all articles
     * @return
     */
    public int[] getIds() {
        int[] ids = new int[articles.size()];
        for (int i = 0; i < articles.size(); i++) {
            ids[i] = articles.get(i).id;
        }
        return ids;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //clear options menu
        menu.clear();

        //re-initialise menu
        inflater.inflate(R.menu.options_menu, menu);

        //set search icon using FontAwesome
        menu.findItem(R.id.search).setIcon(
                new IconicsDrawable(getContext())
                        .icon(FontAwesome.Icon.faw_search)
                        .color(ContextCompat.getColor(context, R.color.md_white_1000))
                        .sizeDp(18));

        //set search feature
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = new SearchView(((MainActivity) context).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Search for articles on server when text changed
                NewsArticle.loadMultiple(getActivity(), 0, 1000, newText, "" + categoryId, new NewsArticle.onMultipleDownloadedListener() {
                    @Override
                    public void onDownloaded(List<NewsArticle> articles) {
                        setArticles(articles);
                    }
                });
                return false;
            }
        });
    }
}
