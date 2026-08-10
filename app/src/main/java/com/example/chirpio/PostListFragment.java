package com.example.chirpio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chirpio.databinding.FragmentPostListBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class    PostListFragment extends Fragment {
    private FirebaseFirestore firestore = null;
    private CollectionReference post_collection = null;
    private FragmentPostListBinding binding = null;
    private ArrayList<Posts> list = new ArrayList<>();
    private ListenerRegistration listenerRegistration;
    private PostAdapter adapter = null;
    private RecyclerView.LayoutManager manager = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostListFragment newInstance(String param1, String param2) {
        PostListFragment fragment = new PostListFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_second, container,false);
        binding = FragmentPostListBinding.inflate(inflater, container, false);
        Bundle data=getArguments();
        String uid=data.getString("uid");
        String id=data.getString("id");
        adapter = new PostAdapter(list);
        manager = new LinearLayoutManager(getActivity());
        binding.recvuPostList.setAdapter(adapter);
        binding.recvuPostList.setLayoutManager(manager);
        firestore = FirebaseFirestore.getInstance();
        post_collection = firestore.collection("users").document(uid).collection("thoughts");

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FIRE", "onResume: ########## ");
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
                    Bundle data=getArguments();
                    String user_id=data.getString("uid");
                    Log.i("jus before for loop", "onResume: "+user_id); //this should give user id
                    for (DocumentSnapshot doc : docs) {
                        String id=doc.getId(); //thought id
//                        String user_id=doc.getId(); //but is this fetching user id?
                        Log.i("FOR LOOP", "ID "+id);
                        String n = doc.getString("username");
                        String d = doc.getString("dt");
                        String p = doc.getString("post");
                        long l = doc.getLong("likecount");
                        long c = doc.getLong("commentcount");
                        int like_count = (int) l;
                        int comment_count = (int) c;
                        Posts post = new Posts(n, d, p, like_count, comment_count);
                        post.setId(id);
                        post.setUser_id(user_id);
                        list.add(post);
                        Log.i("FIRETASK", "####### ONE RECORD ADDED ######");
                    }//for ends
                    Log.i("FIRETASK", "######### onResume: ends #########");
                    adapter.notifyDataSetChanged();
                });//addSnapshotlistener
    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        listenerRegistration.remove();
//    }
}
