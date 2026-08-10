package com.example.chirpio;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Posts> postlist;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    public PostAdapter(ArrayList<Posts>list){
        postlist=list;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.recvu_row_postlist,parent,false);
        MyViewHolder mvh=new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder mvh=(MyViewHolder)holder;
        Posts p=postlist.get(position);
        mvh.tv1.setText(p.getName());
        mvh.tv2.setText(p.getDate());
        mvh.tv3.setText(p.getPost());
        mvh.tv4.setText(String.valueOf(p.getLike_count()));
        mvh.tv5.setText(String.valueOf(p.getComment_count()));
        mvh.tv6.setText(p.getId()); //thought id
        mvh.tv7.setText(p.getUser_id()); //user id
    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
        public Button btnlikecount;
        public  MyViewHolder(@NonNull View itemview){
            super(itemview);
            tv1=itemview.findViewById(R.id.tv_recvu_username);
            tv2=itemview.findViewById(R.id.tv_recvu_thought);
            tv3=itemview.findViewById(R.id.tv_recvu_date);
            tv4=itemview.findViewById(R.id.tv_postcomment);
            tv5=itemview.findViewById(R.id.tv_postlistlikecount);
            tv6=itemview.findViewById(R.id.tvid);
            tv7=itemview.findViewById(R.id.tv_post_id);
            btnlikecount=itemview.findViewById(R.id.btnpostlistlikecount);
            btnlikecount.setOnClickListener(view -> {
                Log.i("FIRETAG", "MyViewHolder: ID "+tv6.getText().toString());
                DocumentReference docref = firestore.collection("thoughts").document(tv6.getText().toString());
                docref.get()
                                .addOnCompleteListener(task -> {
                                    DocumentSnapshot ds = task.getResult();
                                    long likes= (long) ds.get("likecount");
                                    docref.update("likecount",likes+1);
                                });
            });
            itemview.setOnClickListener(view -> {
                Bundle data = new Bundle();
                data.putString("id",tv6.getText().toString());
                data.putString("uid",tv7.getText().toString());
                Log.i("Post Adapter", "TID: " + tv6.getText().toString());
                Log.i("Post Adapter", "UID: " + tv7.getText().toString());
                Navigation.findNavController(view).navigate(R.id.action_postListFragment_to_commentListFragment,data);
            });

        }

    }
}
