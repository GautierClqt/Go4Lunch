package com.cliquet.gautier.go4lunch.Controllers.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cliquet.gautier.go4lunch.Models.Workmates;
import com.cliquet.gautier.go4lunch.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Workmates> mWorkmatesList = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    public WorkmatesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_workmates, viewGroup, false);
        Gson gson = new Gson();

        if (getArguments() != null) {
            String gsonWorkmatesList = getArguments().getString("workmates_list");
            mWorkmatesList = gson.fromJson(gsonWorkmatesList, new TypeToken<List<Workmates>>(){}.getType());
        }

        recyclerView = view.findViewById(R.id.activity_workmates_list_recycler);

        WorkmatesRecyclerAdapter adapter = new WorkmatesRecyclerAdapter(this.getContext(), mWorkmatesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // Inflate the layout for this fragment
        return view;
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
