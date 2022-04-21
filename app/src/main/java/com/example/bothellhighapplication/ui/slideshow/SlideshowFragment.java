package com.example.bothellhighapplication.ui.slideshow;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bothellhighapplication.R;
import com.example.bothellhighapplication.databinding.FragmentSlideshowBinding;
import com.example.bothellhighapplication.ui.home.MyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    //Fragment for Showin Extracurriculars
    private FragmentSlideshowBinding binding;
    View root;
    private FirebaseFirestore mstore;
    RecyclerView recyclerView;
    ArrayList<Extracurricular> extracurricularsArrayList;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        //binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();
        root= inflater.inflate(R.layout.fragment_slideshow,container,false);
        extracurricularsArrayList = new ArrayList<Extracurricular>();
        mstore = FirebaseFirestore.getInstance();
        recyclerView = root.findViewById(R.id.extracurr_recycler);
        mstore.collection("extracurriculars")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        System.out.println("Actual: " + task.getResult().size());
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                extracurricularsArrayList.add(document.toObject(Extracurricular.class));
                            }
                            RecyclerAdapter adapter = new RecyclerAdapter(getContext(), extracurricularsArrayList);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        System.out.println("what" + extracurricularsArrayList.size());
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}