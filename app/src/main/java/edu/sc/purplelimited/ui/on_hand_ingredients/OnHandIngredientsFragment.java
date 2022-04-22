package edu.sc.purplelimited.ui.on_hand_ingredients;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.sc.purplelimited.LoginActivity;
import edu.sc.purplelimited.R;
import edu.sc.purplelimited.classes.Ingredient;
import edu.sc.purplelimited.databinding.FragmentOnHandBinding;

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
    private int currentQuantity;
    private final int defaultQuantity = 1;
    private int defaultColor;
    private int errorColor;
    private boolean invalidAttempt = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Set up view and binding
        binding = FragmentOnHandBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Fetch on hand ingredients from Firebase
        String userName = LoginActivity.getCurrentUserName();
        database = FirebaseDatabase.getInstance();
        onHandDBRef = database.getReference("users").child(userName).child("onHandIngredients");

        // Set up local list of on hand ingredients
        onHandArrayList = new ArrayList<>();
        onHandListView = root.findViewById(R.id.on_hand_list_view);

        // Get text colors from current style
        defaultColor = ContextCompat.getColor(getContext(), R.color.grey);
        errorColor = ContextCompat.getColor(getContext(), R.color.errorTextColor);

        // New Ingredient Button
        Button createNewIngredientButton = root.findViewById(R.id.new_ingredient_button);
        createNewIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        // Populate local list with database entries
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
            // save the scroll position before changing the view
            int prevPosition = onHandListView.getFirstVisiblePosition();
            View v = onHandListView.getChildAt(0);
            int top = (v == null) ? 0 : v.getTop();

            // populate the view and reset to the original scroll position
            OnHandListAdapter adapter = new OnHandListAdapter(this.getContext(), onHandArrayList);
            onHandListView.setAdapter(adapter);
            onHandListView.setSelectionFromTop(prevPosition, top);
        }
    }

    private void createDialog(){
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(getContext());
        final View view = getLayoutInflater().inflate(R.layout.new_ingredient_popup, null);

        // Reset local indicators to default values
        currentQuantity = defaultQuantity;
        invalidAttempt = false;

        // Input Fields
        newIngredientName = (EditText) view.findViewById(R.id.new_ingredient_name);
        newIngredientQuantity = (EditText) view.findViewById(R.id.current_quantity);
        newIngredientUnits = (EditText) view.findViewById(R.id.new_ingredient_units);

        // Clickable
        ImageView increaseQuantity = (ImageView) view.findViewById(R.id.increase_quantity_popup);
        ImageView decreaseQuantity = (ImageView) view.findViewById(R.id.decrease_quantity_popup);
        Button addIngredientButton = (Button) view.findViewById(R.id.add_new_ingredient);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_new_ingredient);
        CheckBox noUnits = (CheckBox) view.findViewById(R.id.no_units_checkbox);

        // PopupDialog
        popupBuilder.setView(view);
        addIngredientPopup = popupBuilder.create();
        addIngredientPopup.show();

        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentQuantity++;
                newIngredientQuantity.setText(""+ currentQuantity);
            }
        });
        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(currentQuantity > 1) {
                   currentQuantity--;
                   newIngredientQuantity.setText(""+ currentQuantity);
               }
            }
        });
        noUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(noUnits.isChecked()) {
                    newIngredientUnits.setVisibility(View.GONE);
                    newIngredientUnits.setText("none");
                    newIngredientUnits.setHintTextColor(defaultColor);
                } else {
                    if (invalidAttempt) {
                        newIngredientUnits.setHintTextColor(errorColor);
                    }
                    newIngredientUnits.setText("");
                    newIngredientUnits.setVisibility(View.VISIBLE);
                }
            }
        });
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get data from fields at time of click
                String ingName = newIngredientName.getText().toString();
                String ingUnits = newIngredientUnits.getText().toString();

                // Check that data from fields is valid
                String validRegex = "[A-Za-z0-9]+[A-Za-z0-9 ]*";
                boolean validName = ingName.matches(validRegex);
                boolean validUnits = ingUnits.matches(validRegex);
                boolean bothInvalid = (!validUnits && !validName);

                // Catch invalid input
                if(!validName || !validUnits) {
                    invalidAttempt = true;

                    // Remove invalid input and set hint text to red
                    if (!validName) {
                        newIngredientName.setText("");
                        newIngredientName.setHintTextColor(errorColor);
                    }
                    if (!validUnits) {
                        newIngredientUnits.setText("");
                        newIngredientUnits.setHintTextColor(errorColor);
                    }

                    // Create a toast based on which fields
                    // contain invalid input
                    String fieldString = bothInvalid ? "fields" : "field";
                    String missingFields = bothInvalid ? "Name and Units " : !validName ? "Name " : "Units ";
                    String toastText = "The " + missingFields + fieldString +
                                       " must contain at least one letter or number," +
                                       " and cannot begin with a space.";
                    Toast.makeText(view.getContext(), toastText, Toast.LENGTH_LONG).show();
                } else {
                    addIngredient(new Ingredient(ingName, ingUnits, currentQuantity, "none"));
                    addIngredientPopup.dismiss();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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