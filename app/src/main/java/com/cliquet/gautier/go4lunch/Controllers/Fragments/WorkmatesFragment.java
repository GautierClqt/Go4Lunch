package com.cliquet.gautier.go4lunch.Controllers.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cliquet.gautier.go4lunch.Controllers.Activities.MainActivity;
import com.cliquet.gautier.go4lunch.Models.Workmates;
import com.cliquet.gautier.go4lunch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkmatesFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Workmates> mWorkmatesList = new ArrayList<>();

    public WorkmatesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Available Workmates");

        View view =  inflater.inflate(R.layout.fragment_workmates, viewGroup, false);
        Gson gson = new Gson();

        if (getArguments() != null) {
            String gsonWorkmatesList = getArguments().getString("workmates_list");
            mWorkmatesList = gson.fromJson(gsonWorkmatesList, new TypeToken<List<Workmates>>(){}.getType());
        }

        recyclerView = view.findViewById(R.id.activity_workmates_list_recycler);

        setWorkmatesAdapter();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        usersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mWorkmatesList.clear();

                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Workmates workmates = documentSnapshot.toObject(Workmates.class);

                    if (!Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(workmates.getId())) {
                        mWorkmatesList.add(new Workmates(
                                workmates.getId(),
                                workmates.getFirstName(),
                                workmates.getLastName(),
                                workmates.getEmail(),
                                workmates.getUrlPicture(),
                                workmates.getSelectedRestaurant()
                        ));
                    }
                }
                setWorkmatesAdapter();
            }
        });
    }

    private void setWorkmatesAdapter() {
        WorkmatesRecyclerAdapter adapter = new WorkmatesRecyclerAdapter(this.getContext());
        adapter.setWorkmatesList(mWorkmatesList);
        adapter.setActiveActivity(1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
