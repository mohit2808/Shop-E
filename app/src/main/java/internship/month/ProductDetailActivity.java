package internship.month;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    TextView name,price,description;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.product_detail_name);
        price = findViewById(R.id.product_detail_price);
        description = findViewById(R.id.product_detail_description);
        imageView = findViewById(R.id.product_detail_image);

        Bundle bundle = getIntent().getExtras();
        String sName = bundle.getString("NAME");
        String sPrice = bundle.getString("PRICE");
        String sUnit = bundle.getString("UNIT");
        String sDesc = bundle.getString("DESC");
        String iImage = bundle.getString("IMAGE");

        getSupportActionBar().setTitle(sName);

        name.setText(sName);
        price.setText(getResources().getString(R.string.price_symbol)+sPrice+"/"+sUnit);
        description.setText(sDesc);
        //imageView.setImageResource(iImage);
        Picasso.get().load(iImage).placeholder(R.drawable.loading_new).into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}