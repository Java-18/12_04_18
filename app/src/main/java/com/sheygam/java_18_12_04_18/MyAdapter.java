package com.sheygam.java_18_12_04_18;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private List<User> users;
    private MyAdapterClickListener listener;


    public MyAdapter(MyAdapterClickListener listener) {
        users = new ArrayList<>();
        this.listener = listener;
        fillList();
    }

    private void fillList(){
        for (int i = 0; i < 100; i++) {
            users.add(new User("User " + i,
                    "user" + i + "@mail.com",
                    Images.avatars[i%Images.avatars.length]));
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = users.get(position);
        holder.emailTxt.setText(user.getEmail());
        holder.nameTxt.setText(user.getName());
        Picasso.with(holder.avatarImg.getContext())
                .load(user.getImgUrl())
                .fit()
                .centerCrop()
                .into(holder.avatarImg);
    }

    public void remove(int position){
        users.remove(position);
        notifyItemRemoved(position);
    }

    public void move(int from, int to){
        User user = users.remove(from);
        users.add(to,user);
        notifyItemMoved(from, to);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameTxt, emailTxt;
        private ImageView avatarImg;
        public MyViewHolder(View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.name_txt);
            emailTxt = itemView.findViewById(R.id.email_txt);
            avatarImg = itemView.findViewById(R.id.avatar_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener != null){
                listener.onClick(users.get(getAdapterPosition()),getAdapterPosition());
            }
        }
    }

    public interface MyAdapterClickListener{
        void onClick(User user, int position);
    }
}
