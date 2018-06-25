package com.app.fiche.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SpecifyProductActivity extends AppCompatActivity {
    private double counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specify_product);

        Bundle bundle = getIntent().getExtras();
        final String productName = bundle.getString("name");
        final int productPicture = bundle.getInt("picture");
        final String productUnitMeasure = bundle.getString("unitMeasure");
        final int shopListId = bundle.getInt("shopListId");

        final EditText editText = findViewById(R.id.specify_item_edit_text);
        TextView textView = findViewById(R.id.specify_item_text_view);
        final TextView counterView = findViewById(R.id.to_buy_edit_text);
        counter = 0;


        double countBy = 0;
        String unitMeasureString = "";
        switch (productUnitMeasure){
            case "ML": unitMeasureString = getString(R.string.ml);
                countBy = 100;
                break;
            case "KG": unitMeasureString = getString(R.string.kg);
                countBy = 0.2;
                break;
            case "PC": unitMeasureString = getString(R.string.pc);
                countBy = 1;
                break;
        }

        final double countByFinal = countBy;

        counterView.setText(""+counter);
        editText.setText(productName);
        textView.setText(unitMeasureString);


        Button buttonMinus = findViewById(R.id.minus_button);
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double currentCount = Double.valueOf(counterView.getText().toString());
                counter = currentCount - countByFinal;
                if(counter < 0){
                    counter = 0;
                }
                counterView.setText(String.format("%.1f", counter));
            }
        });

        Button buttonPlus = findViewById(R.id.plus_button);
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double currentCount = Double.valueOf(counterView.getText().toString());
                counter = currentCount + countByFinal;
                if(counter > 2000000){
                    counter = 2000000;
                }
                counterView.setText(String.format("%.1f", counter));
            }
        });

        Button buttonAdd = findViewById(R.id.add_product_to_shop_list_button);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataRepository dataRepository = new DataRepository(getBaseContext());
                dataRepository.addProductToShopList(shopListId,
                        new ShopListProduct(productName,
                                Product.UnitMeasure.valueOf(productUnitMeasure),
                                productPicture,
                                counter));
                Intent intent = new Intent(getBaseContext(), ExactShopListActivity.class);
                intent.putExtra("id", shopListId);
                startActivity(intent);
            }
        });

    }
}
