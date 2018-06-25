package com.app.fiche.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

public class NewProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        final Bundle bundle = getIntent().getExtras();

        Button button = findViewById(R.id.add_unique_new_product);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup radioGroup = findViewById(R.id.new_product_radio_group);
                EditText editText = findViewById(R.id.new_item_edit_text);
                int radioId = radioGroup.getCheckedRadioButtonId();
                Product.UnitMeasure unitMeasure = null;
                switch (radioId){
                    case R.id.new_product_radio_kg: unitMeasure = Product.UnitMeasure.KG;
                    break;
                    case R.id.new_product_radio_ml: unitMeasure = Product.UnitMeasure.ML;
                    break;
                    case R.id.new_product_radio_pc: unitMeasure = Product.UnitMeasure.PC;
                    break;
                }

                DataRepository dataRepository = new DataRepository(getBaseContext());
                List<Product> products = dataRepository.getProducts();
                boolean itWas = false;
                for(Product product : products){
                    if(product.getName().equals(editText.getText().toString())){
                        itWas = true;
                    }
                }
                if(!itWas) {
                    dataRepository.addProduct(new Product(editText.getText().toString(), unitMeasure, R.mipmap.defaultpic));
                    Intent intent = new Intent(getBaseContext(), AddProductToShopListActivity.class);
                    intent.putExtra("shopListId", bundle.getInt("shopListId"));
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getBaseContext(), R.string.already_exists, Toast.LENGTH_SHORT);
                    toast.show();

                }
            }
        });
    }
}
