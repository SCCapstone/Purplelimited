package edu.sc.purplelimited.ui.on_hand_ingredients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.sc.purplelimited.R;
import edu.sc.purplelimited.databinding.FragmentOnHandBinding;
import edu.sc.purplelimited.databinding.FragmentSuggestionsBinding;

import java.util.ArrayList;
import java.util.List;

public class OnHandIngredientsFragment extends Fragment {
    private final ArrayList<String> onHandIngredientsList = new ArrayList<>();
    private FragmentOnHandBinding binding;
    private View root;
    private FirebaseDatabase database;
    private DatabaseReference users;
    private ListView onHandListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOnHandBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        onHandListView = root.findViewById(R.id.on_hand_list_view);
        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //TODO: replace lookingFor string with int userId
                String lookingFor = "bob";
                onHandIngredientsList.clear();
                for(DataSnapshot user : dataSnapshot.getChildren()) {
                    String name = (String) user.child("name").getValue();
                    if (name.equalsIgnoreCase(lookingFor)) {
                        DataSnapshot onHandIngredients = user.child("onHandIngredients");
                        for (DataSnapshot ingredient : onHandIngredients.getChildren()) {
                            String ingredientString = (String) ingredient.getValue();
                            onHandIngredientsList.add(ingredientString);
                        }
                    }
                }
                populateOnHandIngredients();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {      }
        });

        View root = binding.getRoot();
        return root;
    }

    // TODO: Implement add/remove functionality
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