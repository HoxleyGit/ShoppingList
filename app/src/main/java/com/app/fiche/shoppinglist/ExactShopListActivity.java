package com.app.fiche.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ExactShopListActivity extends AppCompatActivity {
    private ListView list;
    private ArrayAdapter<ShopListProduct> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exact_shop_list);

        Bundle bundle = getIntent().getExtras();
        final int currentShopListId = (int) bundle.get("id");

        Button buttonBack = findViewById(R.id.exact_shop_list_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ShopListActivity.class);
                startActivity(intent);
            }
        });

        Button button = findViewById(R.id.exact_shop_list_add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddProductToShopListActivity.class);
                intent.putExtra("shopListId", currentShopListId);
                startActivity(intent);
            }
        });



        final DataRepository dataRepository = new DataRepository(getBaseContext());
        final ShopList currentShopList = dataRepository.findShopListById(currentShopListId);
        list = findViewById(R.id.product_list);

        final List<ShopListProduct> shopListProducts = currentShopList.getShopListProducts();
        if(shopListProducts != null) {
            List<ShopListProduct> shopListProductsArray = shopListProducts;
            adapter = new CustomAdapter(getBaseContext(), R.layout.shop_list_product_list_adapter, shopListProductsArray);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    dataRepository.buyProduct(shopListProducts.get(i));
                    adapter = new CustomAdapter(getBaseContext(),
                            R.layout.shop_list_product_list_adapter,
                            dataRepository.findShopListById(currentShopListId).getShopListProducts());
                    list.setAdapter(adapter);
                }
            });
            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final int id = i;
                    AlertDialog.Builder b = new AlertDialog.Builder(ExactShopListActivity.this);
                    b.setMessage(getString(R.string.question_delete));
                    b.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dataRepository.deleteProductFromShopList(currentShopListId, shopListProducts.get(id).getId());
                            dialogInterface.dismiss();
                            Intent intent = new Intent(getBaseContext(), ExactShopListActivity.class);
                            intent.putExtra("id", currentShopListId);
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


    class CustomAdapter extends ArrayAdapter<ShopListProduct> {
        private LayoutInflater layoutInflater;
        private List<ShopListProduct> shopListsProducts;
        private int mViewResourseId;

        public CustomAdapter(@NonNull Context context, int resource, @NonNull List<ShopListProduct> shopListProducts) {
            super(context, resource, shopListProducts);
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mViewResourseId = resource;
            this.shopListsProducts = shopListProducts;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = layoutInflater.inflate(mViewResourseId, null);
            ShopListProduct shopListProduct = null;
            if(shopListsProducts != null) {
                shopListProduct = shopListsProducts.get(position);
            }

            if (shopListProduct != null) {
                TextView productNameTextView = convertView.findViewById(R.id.product_list_adapter_text_view_name);
                TextView productListToBuyTextView = convertView.findViewById(R.id.product_list_adapter_text_view_to_buy);
                ImageView imageView = convertView.findViewById(R.id.product_list_adapter_image_view);

                if (productNameTextView != null && productListToBuyTextView != null
                        && imageView != null) {
                    productNameTextView.setText(shopListProduct.getName());
                    Product.UnitMeasure unitMeasure = shopListProduct.getUnitMeasure();
                    String toBuyText = getString(R.string.bought);
                    double toBuy = shopListProduct.getToBuy();
                    if (toBuy > 0) {
                        toBuyText = "" + String.format("%.1f", toBuy);
                        switch (unitMeasure) {
                            case KG:
                                toBuyText += " " + getString(R.string.kg);
                                break;
                            case ML:
                                toBuyText += " " + getString(R.string.ml);
                                break;
                            case PC:
                                toBuyText += " " + getString(R.string.pc);
                                break;
                        }

                    } else {
                        toBuyText = getString(R.string.bought).toUpperCase();
                    }
                    productListToBuyTextView.setText(toBuyText);

                    imageView.setImageResource(shopListProduct.getPicture());
                }
            }
            return convertView;

        }
    }
}
