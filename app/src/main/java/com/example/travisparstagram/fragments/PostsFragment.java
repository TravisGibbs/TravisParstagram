package com.example.travisparstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travisparstagram.Adapters.PostsAdapter;
import com.example.travisparstagram.Adapters.EndlessRecyclerViewScrollListener;
import com.example.travisparstagram.DataTypes.Post;
import com.example.travisparstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class PostsFragment extends Fragment {
    protected EndlessRecyclerViewScrollListener scrollListener;
    protected RecyclerView rvPosts;
    final static String Tag = "postsFrag";
    protected PostsAdapter postsAdapter;
    protected List<Post> allPosts;
    private SwipeRefreshLayout swipeContainer;


    public PostsFragment() {
        // Required empty public constructor
    }



    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        queryPosts(false);



        //Despite working from home, my experience here had been really nice. I really enjoy my work and get excited when I think of a somewhate clever way of implementing features when I'm off the clock. Additonally I have enjoyed helping others and comparing soloutions for different hang ups with my pod. Often I have been able to work ahead and I get to help others with features and concepts that I have looked at previously.
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvposts);
        allPosts = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        postsAdapter = new PostsAdapter(getContext(),allPosts);
        rvPosts.setAdapter(postsAdapter);
        rvPosts.setLayoutManager(linearLayoutManager);
        queryPosts(false);// Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(Tag,"scrolled");
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        Log.i(Tag,"scroll set");
        rvPosts.addOnScrollListener(scrollListener);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_purple,
                android.R.color.white,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    protected void queryPosts(boolean clear){
        if(clear) {
            postsAdapter.clear();
            allPosts.clear();
        }
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.setLimit(2);
        if(allPosts.size()>0) {

            query.whereLessThan(Post.KEY_CREATED_AT, allPosts.get(allPosts.size() - 1).getCreatedAt());
        }
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(Tag, "can't get posts!", e);
                    return;
                }
                allPosts.addAll(posts);
                postsAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
                for(Post p : allPosts){
                    Log.i(Tag,p.getKeyDescription());
                }
            }
        });

    }

    public void fetchTimelineAsync(int page) {
        queryPosts(true);
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.

    }
}