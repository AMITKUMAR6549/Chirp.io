package com.example.chirpio;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chirpio.databinding.FragmentFirstBinding;
import com.example.chirpio.databinding.FragmentNewPostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPostFragment extends Fragment {
    private FirebaseFirestore firestore=null;
    private FirebaseAuth auth;
    FragmentNewPostBinding binding = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewPostFragment newInstance(String param1, String param2) {
        NewPostFragment fragment = new NewPostFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewPostBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        String uid=auth.getCurrentUser().getUid();
        Log.i("NewPostFragment", "UID: " + uid);
        binding.btnPost.setOnClickListener(view -> {
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                Log.i("FIRETAG", "onCreateView: ############# : "+userId);
                String username = currentUser.getDisplayName();
                String postText = binding.edtpost.getText().toString();
                String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

                Map<String, Object> document = new HashMap<>();
                document.put("commentcount", 0);
                document.put("likecount", 0);
                document.put("dt", currentDate);
                document.put("username", username);
                document.put("post", postText);


                firestore.collection("users").document(userId).collection("thoughts").document().set(document)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getActivity(), "Thought Posted: " , Toast.LENGTH_SHORT).show();
                                           // Navigation.findNavController(view).navigate(R.id.action_postListFragment_to_newPostFragment);
                                            //Navigation.findNavController(view).navigate(R.id.action_newPostFragment_to_postListFragment);
                                        }
                                    }
                                });
                /*
                firestore.collection("thoughts")
                        .add(document)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getActivity(), "Thought Posted: " + documentReference.getId(), Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.action_postListFragment_to_newPostFragment);
                            Navigation.findNavController(view).navigate(R.id.action_newPostFragment_to_postListFragment);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Failed to post thought: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });*/
            } else {
                Toast.makeText(getActivity(), "User is not logged in", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
