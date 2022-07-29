package internship.month;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    TextView welcomeMessage;
    SharedPreferences sp;

    RecyclerView categoryRecyclerview,productRecyclerview;

    /*String[] categoryNameArray = {"Vegetable","Chillies","Citrus Fruits","Berries"};
    int[] categoryImageArray = {R.drawable.vegetable,R.drawable.green_red,R.drawable.citrus,R.drawable.berries};*/

    ArrayList<CategoryList> arrayList;

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

    TextView viewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Dashboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences(ConstantSp.PREFERENCE,MODE_PRIVATE);

        welcomeMessage = findViewById(R.id.home_welcome_message);

        welcomeMessage.setText("Welcome "+sp.getString(ConstantSp.EMAIL,""));

        categoryRecyclerview = findViewById(R.id.home_category_recyclerview);
        //Data Set In List
        //categoryRecyclerview.setLayoutManager(new LinearLayoutManager(HomeActivity.this));

        //Data Set In Grid
        categoryRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        categoryRecyclerview.setItemAnimator(new DefaultItemAnimator());

        productRecyclerview = findViewById(R.id.home_product_recyclerview);
        productRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        productRecyclerview.setItemAnimator(new DefaultItemAnimator());

        viewAll = findViewById(R.id.home_view_all);
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.CATEGORY_ID,"").commit();
                new CommonMethod(HomeActivity.this,ProductActivity.class);
            }
        });

        if(new ConnectionDetector(HomeActivity.this).isConnectingToInternet()){
            new getCategoryData().execute();
            //new getProductData().execute();
        }
        else{
            new ConnectionDetector(HomeActivity.this).connectiondetect();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        if(id==R.id.main_menu_logout){
            sp.edit().clear().commit();
            new CommonMethod(HomeActivity.this,MainActivity.class);
        }
        if(id==R.id.main_menu_message){
            new CommonMethod(HomeActivity.this,MessageActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Exit Alert");
        builder.setMessage("Are You Sure Want To Exit!");

        builder.setCancelable(false);

        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //finish();
                finishAffinity();
                //System.exit(0);
            }
        });
        builder.setNeutralButton("Rate Us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                new CommonMethod(HomeActivity.this, "Rate Us");
            }
        });
        builder.show();
    }

    private class getCategoryData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(HomeActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return new MakeServiceCall().MakeServiceCall(ConstantSp.URL+"getCategory.php",MakeServiceCall.POST,new HashMap<>());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("Status")==true){
                    JSONArray array = object.getJSONArray("CategoryData");
                    arrayList= new ArrayList<>();
                    for(int i=0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        CategoryList list = new CategoryList();
                        list.setId(jsonObject.getString("id"));
                        list.setName(jsonObject.getString("name"));
                        list.setImage(jsonObject.getString("image"));
                        arrayList.add(list);
                    }
                    //CategoryAdapter adapter = new CategoryAdapter(HomeActivity.this,categoryNameArray,categoryImageArray);
                    CategoryAdapter adapter = new CategoryAdapter(HomeActivity.this,arrayList);
                    categoryRecyclerview.setAdapter(adapter);
                }
                else{
                    new CommonMethod(HomeActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new CommonMethod(HomeActivity.this,e.getMessage());
            }
        }
    }

    private class getProductData extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(HomeActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return new MakeServiceCall().MakeServiceCall(ConstantSp.URL+"getAllProduct.php",MakeServiceCall.POST,new HashMap<>());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
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
                    ProductAdapter productAdapter = new ProductAdapter(HomeActivity.this,productArrayList);
                    productRecyclerview.setAdapter(productAdapter);
                }
                else{
                    new CommonMethod(HomeActivity.this,object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                new CommonMethod(HomeActivity.this,e.getMessage());
            }
        }
    }
}