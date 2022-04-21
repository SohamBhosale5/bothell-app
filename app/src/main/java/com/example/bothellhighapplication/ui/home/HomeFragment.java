package com.example.bothellhighapplication.ui.home;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bothellhighapplication.OnboardingScreens;
import com.example.bothellhighapplication.R;
import com.example.bothellhighapplication.SignUp;
import com.example.bothellhighapplication.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//Fragment for building the home fragment of the nav la
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ImageView plus;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText newclass_name, newclass_teacher, newclass_period;
    private Button newClass_cancel, newClass_save;
    private String className, classTeacher;
    private int period;
    private String userID;
    private FirebaseFirestore mstore;
    RecyclerView recyclerView;
    ArrayList<Course> courseArrayList;
    MyAdapter myAdapter;
    ProgressDialog progressDialog;

    View root;
    ImageView ivInsta, ivTwitter;

    //Creates the view that will be shown when the model is started
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        root= inflater.inflate(R.layout.fragment_home,container,false);
        progressDialog = new ProgressDialog(getContext());
        ivInsta = root.findViewById(R.id.Instagram);
        ivTwitter = root.findViewById(R.id.Twitter);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        //Initializing Firebase
        mstore = FirebaseFirestore.getInstance();
        recyclerView = root.findViewById(R.id.recycle_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Intent intent = getActivity().getIntent();
        userID = intent.getStringExtra("id");
        courseArrayList = new ArrayList<Course>();
        myAdapter = new MyAdapter(getContext(), courseArrayList);
        plus = root.findViewById(R.id.plus_icon);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewContentDialog();
            }
        });
        ivInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sAppLink = "https://www.instagram.com/bothell.hs";
                String sPackage = "com.instagram.android";
                openLink(sAppLink, sPackage, sAppLink);
            }
        });
        ivTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sAppLink = "twitter://user?screen_name=BothellHS";
                String sPackage = "com.twitter.android";
                String sWebLink = "https://twitter.com/BothellHS";
                openLink(sAppLink, sPackage, sWebLink);
            }
        });
        recyclerView.setAdapter(myAdapter);
        EventChangeListener();
        return root;
    }

    //To Connect to Social Media Links
    public void openLink(String sAppLink, String sPackage, String sWebLink) {
        try {
            Uri uri = Uri.parse(sAppLink);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(sPackage);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException activityNotFoundException) {
            Uri uri = Uri.parse(sWebLink);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    //Checks to see whether entries in database have changed and updates front-end accordingly
    public void EventChangeListener() {
        mstore.collection("users").document(userID).collection("courses").orderBy("period", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                //Converting changed documents into classes that can be used by the recycler view to display data
               for(DocumentChange dc: value.getDocumentChanges()) {
                   if(dc.getType() == DocumentChange.Type.ADDED) {
                       courseArrayList.add(dc.getDocument().toObject(Course.class));
                   }
                   myAdapter.notifyDataSetChanged();
               }

               //Dismissing progress dialog if it is still showing
                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Builds Alert Dialog builder and updates database based on any new additions
    public void createNewContentDialog() {
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View classPopupView = getLayoutInflater().inflate(R.layout.popup,null);
        newclass_period = (EditText) classPopupView.findViewById(R.id.edit_text_period);
        newclass_name = (EditText) classPopupView.findViewById(R.id.edit_text_name);
        newclass_teacher = (EditText) classPopupView.findViewById(R.id.edit_text_teacher);
        newClass_save = (Button) classPopupView.findViewById(R.id.create);

        dialogBuilder.setView(classPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        //Adding onClickListener to save button
        newClass_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Storing string values from popup dialog into database
                String per = newclass_period.getText().toString().trim();
                period = Integer.parseInt(per);
                className = newclass_name.getText().toString().trim();
                classTeacher = newclass_teacher.getText().toString().trim();
                dialog.dismiss();
                String perId = "Period " + period;
                System.out.println(userID);
                int last = 0;
                for(int i = 0; i < classTeacher.length(); i++) {
                    if(classTeacher.charAt(i) == ' ') {
                        last = i;
                    }
                }
                String email = classTeacher.charAt(0) + classTeacher.substring(last+1) + "@nsd.org";
                email = email.toLowerCase();
                DocumentReference documentReference = mstore.collection("users").document(userID);
                //String[] newCourse = {className, classTeacher};
                Map<String, Object> course = new HashMap<>();
                course.put("period",perId);
                course.put("class_name", className);
                course.put("teacher", classTeacher);
                course.put("email", email);
                mstore.collection("users").document(userID).collection("courses").document(perId)
                        .set(course, SetOptions.merge());

            }
        });
    }
}