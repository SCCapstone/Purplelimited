package edu.sc.purplelimited.ui.on_hand_ingredients;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import edu.sc.purplelimited.LoginActivity;
import edu.sc.purplelimited.R;
import edu.sc.purplelimited.classes.Ingredient;
import edu.sc.purplelimited.databinding.FragmentOnHandBinding;

import java.util.ArrayList;

public class OnHandIngredientsFragment extends Fragment {
    private ArrayList<Ingredient> onHandArrayList;
    private ListView onHandListView;
    private FragmentOnHandBinding binding;
    private static FirebaseDatabase database;
    private static DatabaseReference onHandDBRef;
    private AlertDialog addIngredientPopup;
    private EditText newIngredientName;
    private EditText newIngredientUnits;
    private EditText newIngredientQuantity;
    private int currentQuantity = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        onHandArrayList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        String userName = LoginActivity.getCurrentUserName();
        onHandDBRef = database.getReference("users").child(userName).child("onHandIngredients");
        binding = FragmentOnHandBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        onHandListView = root.findViewById(R.id.on_hand_list_view);
        Button createNewIngredientButton = root.findViewById(R.id.new_ingredient_button);

        createNewIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        onHandDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot ds, @Nullable String pcn) {
                String name = ds.child("ingredientName").getValue(String.class);
                String units = ds.child("units").getValue(String.class);
                int quantity = ds.child("quantity").getValue(int.class);
                String id = ds.getKey();
                onHandArrayList.add(new Ingredient(name, units, quantity, id));
                populateOnHandIngredients();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot ds, @Nullable String pcn) {
              String id = ds.getKey();
              int quantity = ds.child("quantity").getValue(int.class);
              for (Ingredient ingredient : onHandArrayList) {
                  if(ingredient.getId().equals(id)) {
                      ingredient.setQuantity(quantity);
                  }
              }
              populateOnHandIngredients();
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot ds) {
                int index = 0;
                String id = ds.getKey();
                for(int i = 0; i < onHandArrayList.size();i++) {
                    Ingredient ingredient = onHandArrayList.get(i);
                    if (ingredient.getId().equals(id)) {
                      index = i;
                    }
                }
                onHandArrayList.remove(index);
                populateOnHandIngredients();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot ds, @Nullable String pcn) {/* empty */}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {/* empty */}
        });
        return binding.getRoot();
    }

    public static void addIngredient(Ingredient toAdd) {
        String id = onHandDBRef.push().getKey();
        toAdd.setId(id);
        onHandDBRef.child(id).setValue(toAdd);
    }

    public static void changeQuantity(int quantity, String id) {
        onHandDBRef.child(id).child("quantity").setValue(quantity);
    }

    public static void removeIngredient(String id) {
        onHandDBRef.child(id).removeValue();
    }

    private void populateOnHandIngredients() {
        if(getContext() != null) {
            OnHandListAdapter adapter = new OnHandListAdapter(this.getContext(), onHandArrayList);
            onHandListView.setAdapter(adapter);
        }
    }

    public void createDialog(){
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(getContext());
        final View view = getLayoutInflater().inflate(R.layout.new_ingredient_popup, null);

        // Input Fields
        newIngredientName = (EditText) view.findViewById(R.id.new_ingredient_name);
        newIngredientQuantity = (EditText) view.findViewById(R.id.current_quantity);
        newIngredientUnits = (EditText) view.findViewById(R.id.new_ingredient_units);

        // Clickable
        ImageView increaseQuantity = (ImageView) view.findViewById(R.id.increase_quantity_popup);
        ImageView decreaseQuantity = (ImageView) view.findViewById(R.id.decrease_quantity_popup);
        Button addIngredientButton = (Button) view.findViewById(R.id.add_new_ingredient);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_new_ingredient);

        // PopupDialog
        popupBuilder.setView(view);
        addIngredientPopup = popupBuilder.create();
        addIngredientPopup.show();

        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuantity++;
                newIngredientQuantity.setText(""+currentQuantity);
            }
        });
        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(currentQuantity > 0) {
                   currentQuantity--;
                   newIngredientQuantity.setText(""+currentQuantity);
               }
            }
        });
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ingName = newIngredientName.getText().toString();
                String ingUnits = newIngredientUnits.getText().toString();
                int ingQuantity = currentQuantity;

                Boolean emptyName = (ingName.equals(""));
                Boolean emptyUnits = (ingUnits.equals(""));
                Boolean emptyQuantity = (ingQuantity == 0);

                //TODO fix toast notification
                if(emptyName || emptyUnits || emptyQuantity) {
                    String toastText = "Please enter all required fields.";
                    Toast.makeText(view.getContext(), toastText, Toast.LENGTH_SHORT).show();
                } else {
                    addIngredient(new Ingredient(ingName, ingUnits, ingQuantity, "none"));
                    currentQuantity = 0;
                    addIngredientPopup.dismiss();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuantity = 0;
                addIngredientPopup.dismiss();
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}