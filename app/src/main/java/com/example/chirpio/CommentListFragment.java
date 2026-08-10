package com.example.chirpio;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chirpio.databinding.FragmentCommentListBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentListFragment extends Fragment {
    private String uid,id;

    private FirebaseFirestore firestore = null;
    private CollectionReference post_collection = null;
    private FragmentCommentListBinding binding = null;
    private ArrayList<Comments> list = new ArrayList<>();
    private ListenerRegistration listenerRegistration;
    private CommentAdapter adapter = null;
    private RecyclerView.LayoutManager manager = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommentListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommentListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentListFragment newInstance(String param1, String param2) {
        CommentListFragment fragment = new CommentListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCommentListBinding.inflate(inflater, container, false);
        adapter = new CommentAdapter(list);
        manager = new LinearLayoutManager(getActivity());
        Bundle data=getArguments();
        uid=data.getString("uid"); //thought id
        id=data.getString("id"); //user
        Log.i("FIRETAG", "onCreateView: UID "+uid);
        Log.i("FIRETAG", "onCreateView: TID "+id);

        binding.recvuCommentList.setAdapter(adapter);
        binding.recvuCommentList.setLayoutManager(manager);
        firestore = FirebaseFirestore.getInstance();
        post_collection = firestore.collection("users").document(uid).collection("thoughts");
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FIRE", "onResume: commentlist ########## ");

        listenerRegistration=firestore.collection("users").document(uid).collection("thoughts").document(id).collection("comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error !=null){
                            Log.i("FIRE", "onEvent: ERROR "+error.getMessage());
                            return;
                        }
                        List<DocumentSnapshot> commentlist=value.getDocuments();
                        for(DocumentSnapshot ds:commentlist){
                            Log.i("FIRE", "onEvent: comment : "+ds.getString("ctext"));
                        }
                    }
                });

        /*
        listenerRegistration = post_collection.orderBy("username")
                .addSnapshotListener((value, error) -> {
                    //value:QuerySnapshot
                    //Error:Exception
                    if (error != null) {
                        Log.i("FIRE_BASE_TAG", "###### ERROR:" + error.getMessage());
                        return;
                    }
                    List<DocumentSnapshot> docs = value.getDocuments();
                    Log.i("FIRETASK", "#########  onResume: ######## " + docs.size());
                    if (docs.size() > 0) {
                        list.clear();
                    }
                    Log.i("FIRETASK", "###### onResume: ######");
                    for (DocumentSnapshot doc : docs) {
//                        String id=doc.getId();
                        String n = doc.getString("username");
                        String d = doc.getString("dt");
                        String c = doc.getString("comment");
//                        long l = doc.getLong("likecount");
//                        long c = doc.getLong("commentcount");
//                        int like_count = (int) l;
//                        int comment_count = (int) c;
                        Comments post = new Comments(n, d, c);
//                        post.setId(id);
//                        list.add(post);
                        Log.i("FIRETASK", "####### ONE RECORD ADDED ######");
                    }//for ends
                    Log.i("FIRETASK", "######### onResume: ends #########");
                    adapter.notifyDataSetChanged();
                });//addSnapshotlistener

         */
    }
}