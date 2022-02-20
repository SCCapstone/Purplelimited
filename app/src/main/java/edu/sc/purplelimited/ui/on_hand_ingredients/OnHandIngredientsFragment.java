package edu.sc.purplelimited.ui.on_hand_ingredients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.sc.purplelimited.R;
import edu.sc.purplelimited.classes.Ingredient;
import edu.sc.purplelimited.classes.Recipe;
import edu.sc.purplelimited.databinding.FragmentOnHandBinding;

import java.util.ArrayList;

public class OnHandIngredientsFragment extends Fragment {
    private final ArrayList<Ingredient> onHandIngredientsList = new ArrayList<>();
    private FragmentOnHandBinding binding;
    private ListView onHandListView;

    //TODO implement add/remove functionality
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOnHandBinding.inflate(inflater, container, false);
        View root1 = binding.getRoot();
        onHandListView = root1.findViewById(R.id.on_hand_list_view);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // TODO replace hardcoded reference with userId
        DatabaseReference onHand = database.getReference("users").child("1").child("onHandIngredients");

        onHand.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String ingName = snapshot.child("ingredientName").getValue(String.class);
                String ingUnit = snapshot.child("units").getValue(String.class);
                String ingQuantity = snapshot.child("quantity").getValue(String.class);
                onHandIngredientsList.add(new Ingredient(ingName, ingUnit, ingQuantity));
                populateOnHandIngredients();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String removed = snapshot.getValue(String.class);
                onHandIngredientsList.remove(removed);
               populateOnHandIngredients();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return binding.getRoot();
    }

    //TODO create custom list view for on hand ingredients w/ add, remove and + - quantity buttons
    private void populateOnHandIngredients() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, onHandIngredientsList);
        onHandListView.setAdapter(arrayAdapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}