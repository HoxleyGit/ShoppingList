package com.app.fiche.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddShopListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop_list);

        final Button addNewShopListButton = findViewById(R.id.add_new_shop_list_button);
        addNewShopListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText addNewShopListEditText = findViewById(R.id.add_new_shop_list_edit_text);
                String shopListName = addNewShopListEditText.getText().toString();
                if(!shopListName.equals("")) {
                    DataRepository dataRepository = new DataRepository(getBaseContext());
                    dataRepository.addShopList(new ShopList(shopListName, new Date()));
                    Intent intent = new Intent(getBaseContext(), ShopListActivity.class);
                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getBaseContext(), getString(R.string.insert_shop_list_name), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
