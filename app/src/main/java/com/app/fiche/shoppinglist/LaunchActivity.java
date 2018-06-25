package com.app.fiche.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        
        DataRepository dataRepository = new DataRepository(getBaseContext());
        List<Product> products = dataRepository.getProducts();
        if(products == null || products.size() == 0){
            List<Product> predefiniedProducts = new ArrayList<>();
            predefiniedProducts.add(new Product(getString(R.string.bread) , Product.UnitMeasure.PC, R.mipmap.bread));
            predefiniedProducts.add(new Product(getString(R.string.cheese) , Product.UnitMeasure.KG, R.mipmap.cheese));
            predefiniedProducts.add(new Product(getString(R.string.milk) , Product.UnitMeasure.ML, R.mipmap.milk));
            predefiniedProducts.add(new Product(getString(R.string.pork) , Product.UnitMeasure.KG, R.mipmap.pork));
            predefiniedProducts.add(new Product(getString(R.string.onion) , Product.UnitMeasure.KG, R.mipmap.onion));
            predefiniedProducts.add(new Product(getString(R.string.water) , Product.UnitMeasure.ML, R.mipmap.water));
            predefiniedProducts.add(new Product(getString(R.string.potato) , Product.UnitMeasure.KG, R.mipmap.potato));
            predefiniedProducts.add(new Product(getString(R.string.egg) , Product.UnitMeasure.PC, R.mipmap.eggs));
            predefiniedProducts.add(new Product(getString(R.string.flour) , Product.UnitMeasure.KG, R.mipmap.flour));
            predefiniedProducts.add(new Product(getString(R.string.tomato) , Product.UnitMeasure.KG, R.mipmap.tomato));

            dataRepository.setPredefinedProducts(predefiniedProducts);
        }

        Button buttonLauncher = findViewById(R.id.button_launcher);
        buttonLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ShopListActivity.class);
                startActivity(intent);
            }
        });
    }


}
