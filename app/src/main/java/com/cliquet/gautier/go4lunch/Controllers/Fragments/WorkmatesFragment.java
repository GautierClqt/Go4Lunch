package com.cliquet.gautier.go4lunch.Controllers.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cliquet.gautier.go4lunch.Models.Restaurant;
import com.cliquet.gautier.go4lunch.Models.User;
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

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class WorkmatesFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Workmates> mWorkmatesList = new ArrayList<>();
    private List<Restaurant> mRestaurantsList = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

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

        View view =  inflater.inflate(R.layout.fragment_workmates, viewGroup, false);
        Gson gson = new Gson();

        if (getArguments() != null) {
            String gsonWorkmatesList = getArguments().getString("workmates_list");
            String gsonRestaurantsList = getArguments().getString("restaurant_list");
            mWorkmatesList = gson.fromJson(gsonWorkmatesList, new TypeToken<List<Workmates>>(){}.getType());
            mRestaurantsList = gson.fromJson(gsonRestaurantsList, new TypeToken<List<Restaurant>>(){}.getType());
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

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    User user = documentSnapshot.toObject(User.class);

                    String selectedRestaurant;
                    if (documentSnapshot.get("userSelected") == null) {
                        selectedRestaurant = null;
                    } else {
                        selectedRestaurant = documentSnapshot.get("userSelected").toString();
                    }

                    if (!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUserId())) {
                        mWorkmatesList.add(new Workmates(
                                user.getUserId(),
                                user.getUserFirstName(),
                                user.getUserLastName(),
                                user.getUserEmail(),
                                user.getUserUrlPicture(),
                                selectedRestaurant
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
        adapter.setRestaurantsList(mRestaurantsList);
        adapter.setActiveActivity(1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
