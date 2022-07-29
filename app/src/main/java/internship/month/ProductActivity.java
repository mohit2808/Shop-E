package internship.month;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductActivity extends AppCompatActivity {

    RecyclerView productRecyclerview;

    String[] productNameArray = {"Beet","Green Chilli","Red Chilli","Apple","Banana"};
    int[] productImageArray = {R.drawable.beet,R.drawable.green_chilli,R.drawable.red_chilli,R.drawable.apple1,R.drawable.banana};
    String[] productPriceArray = {"50","20","35","200","60"};
    String[] productUnitArray = {"250 GM","100 GM","100 GM","1 KG","12 Items"};
    String[] productDescArray = {
            "beet, (Beta vulgaris), also called beetroot, common beet, or garden beet, one of the four cultivated forms of the plant Beta vulgaris of the amaranth family (Amaranthaceae), grown for its edible leaves and taproot. Beetroots are frequently roasted or boiled and served as a side dish.",
            "Green chillies are hollow, and the pith and seeds are the hottest part. Green chillies have a different flavour to other coloured chillies. Chilli heat is related to capsaicin content and measured in Scoville units.",
            "Chili peppers are widely used in many cuisines as a spice to add heat to dishes.",
            "the fleshy, usually rounded red, yellow, or green edible pome fruit of a usually cultivated tree (genus Malus) of the rose family also : an apple tree — compare crab apple. 2 : a fruit (such as a star apple) or other vegetative growth (such as an oak apple) suggestive of an apple. apple of one's eye.",
            "The fruit is variable in size, color, and firmness, but is usually elongated and curved, with soft flesh rich in starch covered with a rind, which may be green, yellow, red, purple, or brown when ripe. The fruits grow upward in clusters near the top of the plant."
    };

    ArrayList<ProductList> productArrayList;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        sp = getSharedPreferences(ConstantSp.PREFERENCE,MODE_PRIVATE);
        getSupportActionBar().setTitle("Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productRecyclerview = findViewById(R.id.product_recyclerview);
        productRecyclerview.setLayoutManager(new LinearLayoutManager(ProductActivity.this));
        productRecyclerview.setItemAnimator(new DefaultItemAnimator());

        /*productArrayList = new ArrayList<>();
        for(int i=0;i<productNameArray.length;i++){
            ProductList list = new ProductList();
            list.setName(productNameArray[i]);
            list.setImage(productImageArray[i]);
            list.setPrice(productPriceArray[i]);
            list.setUnit(productUnitArray[i]);
            list.setDescription(productDescArray[i]);
            productArrayList.add(list);
        }
        ProductListAdapter productAdapter = new ProductListAdapter(ProductActivity.this,productArrayList);
        productRecyclerview.setAdapter(productAdapter);*/

        if(new ConnectionDetector(ProductActivity.this).isConnectingToInternet()){
            new getData().execute();
        }
        else{
            new ConnectionDetector(ProductActivity.this).connectiondetect();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class getData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProductActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if(sp.getString(ConstantSp.CATEGORY_ID,"").equalsIgnoreCase("")){
                return new MakeServiceCall().MakeServiceCall(ConstantSp.URL+"getAllProduct.php",MakeServiceCall.POST,new HashMap<>());
            }
            else{
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("categoryId",sp.getString(ConstantSp.CATEGORY_ID,""));
                return new MakeServiceCall().MakeServiceCall(ConstantSp.URL+"getProduct.php",MakeServiceCall.POST,hashMap);
            }
            //return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("Status")==true){
                    JSONArray array = object.getJSONArray("CategoryData");
                    productArrayList = new ArrayList<>();
                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        ProductList list = new ProductList();
                        list.setName(jsonObject.getString("name"));
                        list.setImage(jsonObject.getString("image"));
                        list.setPrice(jsonObject.getString("price"));
                        list.setUnit(jsonObject.getString("unit"));
                        list.setDescription(jsonObject.getString("description"));
                        productArrayList.add(list);
                    }
                    ProductListAdapter productAdapter = new ProductListAdapter(ProductActivity.this,productArrayList);
                    productRecyclerview.setAdapter(productAdapter);
                }
                else{
                    new CommonMethod(ProductActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new CommonMethod(ProductActivity.this,e.getMessage());
            }

        }
    }
}