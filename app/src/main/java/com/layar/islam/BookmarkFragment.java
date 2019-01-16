package com.layar.islam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.layar.helpers.EmptyRecyclerView;

import java.util.List;

import static com.layar.islam.SingleNewsArticleActivity.ITEM_KEY;

/**
 * Created by melvin on 08/09/2016.
 * <p>
 * Shows a list of the bookmarked Articles. The bookmarked articles are stored by id locally in preferences.
 * The content is however obtained from server.
 */
public class BookmarkFragment extends Fragment {
    Context context;
    private EmptyRecyclerView mRecyclerView;
    private EmptyRecyclerView.Adapter mAdapter;
    private EmptyRecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout swipeLayout;
    RelativeLayout empty;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeToRefresh);
        mRecyclerView = (EmptyRecyclerView) rootView.findViewById(R.id.list);
        empty = (RelativeLayout) rootView.findViewById(R.id.empty);
        mRecyclerView.setEmptyView(empty);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();

        //set title
        getActivity().setTitle(getString(R.string.Bookmark_page_title));

        //set RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manage
        mLayoutManager = new LinearLayoutManager(context);

        mRecyclerView.setLayoutManager(mLayoutManager);

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
     * Refresh Bookmarked Articles.
     */
    public void refresh() {
        NewsArticle.getFavorites(getActivity(), new NewsArticle.onMultipleDownloadedListener() {
            @Override
            public void onDownloaded(List<NewsArticle> articles) {
                swipeLayout.setRefreshing(false);
                setArticles(articles);
            }
        });
    }


    /**
     * Show articles on screen after refresh
     *
     * @param articles
     */
    public void setArticles(final List<NewsArticle> articles) {
        mAdapter = new NewsArticlesAdapter(articles, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, SingleNewsArticleActivity.class);
                intent.putExtra(ITEM_KEY, articles.get(i).id);
                startActivity(intent);
            }
        }, context);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

    }

}
