package com.example.agilesprinters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class forumPostList extends ArrayAdapter<Forum> {
    private ArrayList<Forum> forumPost;
    private Context context;

    public forumPostList(Context context, ArrayList<Forum> forumPost) {
        super(context, 0, forumPost);
        this.forumPost = forumPost;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.user_list_content, parent, false);
        }

        Forum forumItem = forumPost.get(position);

        TextView userFullNameTextView = view.findViewById(R.id.user_name_text_view);
        String temp = forumItem.getFirstName()+" "+ forumItem.getLastName();
        userFullNameTextView.setText(temp);

        return view;
    }
}
