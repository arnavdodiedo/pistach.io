package com.hersheys.recommender.pistachio;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewRatings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewRatings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewRatings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private StorageReference mStorageRef;

    List<item> mList = new ArrayList<>();

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    public NewRatings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewRatings.
     */
    // TODO: Rename and change types and number of parameters
    public static NewRatings newInstance(String param1, String param2) {
        NewRatings fragment = new NewRatings();
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
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        mList.clear();
        final View view = inflater.inflate(R.layout.fragment_new_ratings, container, false);
        recyclerView = view.findViewById(R.id.card_list);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);

        Random random = new Random();
        Set set = new HashSet<Integer>(5);

        final Set globalSet = new HashSet<Integer>(50);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            DatabaseReference userRatingRef = ref.child(user.getUid()).child("Ratings");
            userRatingRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ratings: dataSnapshot.getChildren()){
                        int mid = Integer.parseInt(ratings.getKey());
                        globalSet.add(mid);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            // No user is signed in
        }

        while(set.size() < 5){
            int newID = random.nextInt(3883);
            if(!globalSet.contains(newID))
                set.add(newID);
        }

        Iterator iter = set.iterator();
        while(iter.hasNext()){
            Integer mid = (Integer)iter.next();
            mList.add(new item("https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/images%2F"+mid.toString()+".jpg?alt=media&token=baff526a-ac90-4390-84ac-da4b9ee0f29a",mid.intValue(),0,"newRatings"));
        }
        Adapter adapter = new Adapter(getActivity(), mList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        view.findViewById(R.id.new_ratings_such_empty).setVisibility(View.INVISIBLE);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mList.clear();
                globalSet.clear();
                view.findViewById(R.id.new_ratings_such_empty).setVisibility(View.VISIBLE);
                Random random = new Random();
                Set set = new HashSet<Integer>(5);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    DatabaseReference userRatingRef = ref.child(user.getUid()).child("Ratings");
                    userRatingRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot ratings: dataSnapshot.getChildren()){
                                int mid = Integer.parseInt(ratings.getKey());
                                globalSet.add(mid);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    // No user is signed in
                }

                while(set.size() < 5){
                    int newID = random.nextInt(3883);
                    if(!globalSet.contains(newID))
                        set.add(newID);
                }
                Iterator iter = set.iterator();
                while(iter.hasNext()){
                    Integer mid = (Integer)iter.next();
                    mList.add(new item("https://firebasestorage.googleapis.com/v0/b/pistachio-8f641.appspot.com/o/images%2F"+mid.toString()+".jpg?alt=media&token=baff526a-ac90-4390-84ac-da4b9ee0f29a",mid.intValue(),0,"newRatings"));
                }
                Adapter adapter = new Adapter(getActivity(), mList);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mSwipeRefreshLayout.setRefreshing(false);
                view.findViewById(R.id.new_ratings_such_empty).setVisibility(View.INVISIBLE);
            }
        });

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
