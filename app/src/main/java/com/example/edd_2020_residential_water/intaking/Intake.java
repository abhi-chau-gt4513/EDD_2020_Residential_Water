package com.example.edd_2020_residential_water.intaking;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edd_2020_residential_water.BR;
import com.example.edd_2020_residential_water.MainActivity;
import com.example.edd_2020_residential_water.R;
import com.example.edd_2020_residential_water.SharedViewModel;
import com.example.edd_2020_residential_water.databinding.FragmentFixturesBinding;
import com.example.edd_2020_residential_water.databinding.FragmentIntakeBinding;
import com.example.edd_2020_residential_water.databinding.IntakeTableBinding;
import com.example.edd_2020_residential_water.fixtures.Fixtures;
import com.example.edd_2020_residential_water.fixtures.FixturesRecyclerViewAdapter;
import com.example.edd_2020_residential_water.models.Splash;
import com.example.edd_2020_residential_water.models.Water;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseError;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Intake#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Intake extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2;

    List<Water> waterList;
    List<Integer> day, month, year, hour, min, sec;
    List<String> fixtures;
    List<Double> volume;

    private FragmentIntakeBinding waterBinding;
    private IntakeRecyclerViewAdapter mAdapterI;
    private RecyclerView fluid;
    private MainActivity conserve;

    private SharedViewModel svm;

    private OnFragmentInteractionListener mListener;

    private LinearLayoutManager wllm;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mGetReference = mDatabase.getReference().child("Master Shower");

    public static final String CHANNEL_ID = "simple_water_message";

    public Intake() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Intake.
     */
    // TODO: Rename and change types and number of parameters
    public static Intake newInstance(String param1, String param2) {
        Intake fragment = new Intake();
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
        waterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_intake, container, false);
        final View view = waterBinding.getRoot();

        wllm = new LinearLayoutManager(view.getContext());
        conserve = (MainActivity) getActivity();

        fluid = waterBinding.waterDataIntake;
        fluid.setLayoutManager(wllm);

//        fluid.removeAllViews();

        final Calendar cal = Calendar.getInstance();
        waterList = new ArrayList<Water>();
        waterList.clear();

//        onStart();

//        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Water>().setQuery(query, Water.class).build();
//
//        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Water, SplashViewHolder>(options) {
//            @Override
//            public SplashViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                // Create a new instance of the ViewHolder, in this case we are using a custom
//                // layout called R.layout.message for each item
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.fragment_intake, parent, false);
//
//                return new SplashViewHolder(waterBinding);
//            }
//
//            @Override
//            protected void onBindViewHolder(SplashViewHolder holder, int position, Water model) {
//
//                holder.bind(model);
////                holder.setFixt(model.getFixture());
////                holder.setFlow(model.getFlowRate());
////                holder.setLeaking(model.getLeaking());
////                holder.setVolume(model.getVolume());
//            }
//        };
//        adapter.startListening();

        // Initialize the adapter
//        fluid.setAdapter(adapter);

        // Set the layout manager
        waterBinding.setWaterManager(wllm);

        // Set the adapter
        waterBinding.setWaterAdapter(mAdapterI);

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.replace(fluid.getId(), Fixtures.newInstance("", ""));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

//        Query query = FirebaseDatabase.getInstance().getReference().child("Master Shower").limitToLast(1);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Water>().setQuery(mGetReference, Water.class).build();

        FirebaseRecyclerAdapter<Water, SplashViewHolder> adapter = new FirebaseRecyclerAdapter<Water, SplashViewHolder>(options) {
            @Override
            public SplashViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                waterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_intake, parent, false);
                return new SplashViewHolder(waterBinding);
            }

            @Override
            protected void onBindViewHolder(SplashViewHolder holder, int position, Water model) {

                holder.bind(model);
//                holder.setFixt(model.getFixture());
//                holder.setFlow(model.getFlowRateL(), model.getFlowRateML());
//                holder.setLeaking(model.isLeak());
//                holder.setVolume(model.getVolumeFlow());
            }
        };
        fluid.setAdapter(adapter);
        fluid.setLayoutManager(wllm);
        adapter.startListening();
    }

    public static class SplashViewHolder extends RecyclerView.ViewHolder {
        private FragmentIntakeBinding binding;
        private View view;

        /*public SplashViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }*/

        public SplashViewHolder(@NonNull FragmentIntakeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Water water) {
            binding.setVariable(BR.water, water);
            binding.executePendingBindings();
        }
//        public void setFixt(String fixture) {
//            binding.waterFixture.setText(fixture);
//        }
//
//        public void setLeaking(Boolean leaking) {
//            binding.waterLeaking.setText(Boolean.toString(leaking));
//        }
//
//        public void setFlow(double flowL, double flowML) {
//            binding.waterFlowRate.setText(Double.toString(flowL + flowML / 1000));
//        }
//
//        public void setVolume(double volume) {
//            binding.waterVolumeFlow.setText(Double.toString(volume));
//        }
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
