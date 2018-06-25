package com.app.fiche.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import java.util.List;

public class AddProductToShopListActivity extends AppCompatActivity {
    private ListView list;
    private ArrayAdapter<Product> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_shop_list);

        final DataRepository dataRepository = new DataRepository(getBaseContext());
        final Bundle bundle = getIntent().getExtras();

        Button buttonBack = findViewById(R.id.products_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ExactShopListActivity.class);
                intent.putExtra("id", bundle.getInt("shopListId"));
                startActivity(intent);
            }
        });

        Button button = findViewById(R.id.add_new_product_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), NewProductActivity.class);
                intent.putExtra("shopListId", bundle.getInt("shopListId"));
                startActivity(intent);
            }
        });


        list = findViewById(R.id.exact_product_list);

        final List<Product> products = dataRepository.getProducts();
        if(products != null) {
            List<Product> productsArray = products;
            adapter = new CustomAdapter(getBaseContext(), R.layout.product_adapter, productsArray);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ShopList shopList = dataRepository.getShopListById(bundle.getInt("shopListId"));
                    boolean itWas = false;
                    if(shopList != null){
                        List<ShopListProduct> shopListProducts = shopList.getShopListProducts();
                        if(shopListProducts != null){
                            for(ShopListProduct shopListProduct : shopListProducts){
                                if(shopListProduct.getName().equals(products.get(i).getName())){
                                    itWas = true;
                                }
                            }
                        }
                    }
                    if(!itWas) {
                        Intent intent = new Intent(getBaseContext(), SpecifyProductActivity.class);
                        intent.putExtra("name", products.get(i).getName());
                        intent.putExtra("picture", products.get(i).getPicture());
                        Product.UnitMeasure unitMeasure = products.get(i).getUnitMeasure();
                        intent.putExtra("unitMeasure", unitMeasure.toString());
                        intent.putExtra("shopListId", bundle.getInt("shopListId"));
                        startActivity(intent);
                    } else {
                        Toast toast = Toast.makeText(getBaseContext(), R.string.already_exists, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });

            list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i > 9) {
                        final int id = i;
                        AlertDialog.Builder b = new AlertDialog.Builder(AddProductToShopListActivity.this);
                        b.setMessage(getString(R.string.question_delete));
                        b.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dataRepository.deleteProduct(products.get(id));
                                dialogInterface.dismiss();
                                Intent intent = new Intent(getBaseContext(), AddProductToShopListActivity.class);
                                intent.putExtra("shopListId", bundle.getInt("shopListId"));
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
                    }
                    return true;
                }
            });
        }
    }


    class CustomAdapter extends ArrayAdapter<Product> {
        private LayoutInflater layoutInflater;
        private List<Product> products;
        private int mViewResourseId;

        public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Product> products) {
            super(context, resource, products);
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mViewResourseId = resource;
            this.products = products;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = layoutInflater.inflate(mViewResourseId, null);
            Product product = products.get(position);

            if (product != null) {
                TextView productNameTextView = convertView.findViewById(R.id.exact_product_list_adapter_text_view_name);
                ImageView imageView = convertView.findViewById(R.id.exact_product_list_adapter_image_view);

                if (productNameTextView != null && imageView != null) {
                    productNameTextView.setText(product.getName());
                    imageView.setImageResource(product.getPicture());
                }
            }
            return convertView;
        }

    }
}

