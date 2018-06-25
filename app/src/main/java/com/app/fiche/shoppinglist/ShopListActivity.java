package com.app.fiche.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class ShopListActivity extends AppCompatActivity {
    private ListView list;
    private ArrayAdapter<ShopList> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        Button addNewShopListButton = findViewById(R.id.shop_lists_add_button);
        addNewShopListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddShopListActivity.class);
                startActivity(intent);
            }
        });

        list = findViewById(R.id.shop_list);
        final DataRepository dataRepository = new DataRepository(getBaseContext());

        Button buttonBack = findViewById(R.id.shop_lists_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LaunchActivity.class);
                startActivity(intent);
            }
        });

        final List<ShopList> shopLists = dataRepository.getShopLists();
        if(shopLists != null) {
            List<ShopList> shopListArray = shopLists;
            adapter = new CustomAdapter(getBaseContext(), R.layout.shop_list_adapter, shopListArray);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getBaseContext(), ExactShopListActivity.class);
                    intent.putExtra("id", shopLists.get(i).getId());
                    startActivity(intent);
                }
            });

            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final int id = i;
                    AlertDialog.Builder b = new AlertDialog.Builder(ShopListActivity.this);
                    b.setMessage(getString(R.string.question_delete));
                    b.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dataRepository.deleteShopList(shopLists.get(id));
                            dialogInterface.dismiss();
                            Intent intent = new Intent(getBaseContext(), ShopListActivity.class);
                            startActivity(intent);
                        }
                    });
                    b.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    b.show();
                    return true;
                }


            });
        }

    }


    class CustomAdapter extends ArrayAdapter<ShopList>{
        private LayoutInflater layoutInflater;
        private List<ShopList> shopLists;
        private int mViewResourseId;

        public CustomAdapter(@NonNull Context context, int resource, @NonNull List<ShopList> shopLists) {
            super(context, resource, shopLists);
            this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mViewResourseId = resource;
            this.shopLists = shopLists;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = layoutInflater.inflate(mViewResourseId, null);
            ShopList shopList = shopLists.get(position);

            if(shopList != null) {
                TextView shopListNameTextView = convertView.findViewById(R.id.shop_list_adapter_text_view_name);
                TextView shopListDateTextView = convertView.findViewById(R.id.shop_list_adapter_text_view_date);
                TextView shopListStatusTextView = convertView.findViewById(R.id.shop_list_adapter_text_view_status);

                if(shopListNameTextView != null && shopListDateTextView != null
                        && shopListStatusTextView != null){
                    shopListNameTextView.setText(shopList.getName());
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String dateString = dateFormat.format(shopList.getDate());
                    shopListDateTextView.setText(dateString);

                    ShopList.Status status = shopList.getStatus();
                    String statusString = "";
                    switch (status){
                        case NEW : statusString = getString(R.string.newList);
                        break;
                        case PARTIALLY_COMPLETED : statusString = getString(R.string.partially_completed);
                        break;
                        case COMPLETED : statusString = getString(R.string.completed);
                        break;
                    }
                    int size = 0;
                    if(shopList.getShopListProducts() != null){
                        size = shopList.getShopListProducts().size();
                    }
                    shopListStatusTextView.setText(statusString + " - " + size + " " +getString(R.string.how_many_products));

                }
            }

            return convertView;
        }
    }
}
