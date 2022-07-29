package internship.month;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyHolder> {

    Context context;
    ArrayList<ProductList> arrayList;

    public ProductListAdapter(Context homeActivity, ArrayList<ProductList> productArrayList) {
        this.context = homeActivity;
        this.arrayList = productArrayList;
    }

    @NonNull
    @Override
    public ProductListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product_list,parent,false);
        return new ProductListAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView name,price;
        ImageView imageView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.custom_product_list_name);
            price = itemView.findViewById(R.id.custom_product_list_price);
            imageView = itemView.findViewById(R.id.custom_product_list_image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.MyHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.price.setText(context.getResources().getString(R.string.price_symbol)+arrayList.get(position).getPrice()+"/"+arrayList.get(position).getUnit());
        //holder.imageView.setImageResource(arrayList.get(position).getImage());
        Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.drawable.loading_new).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("NAME",arrayList.get(position).getName());
                bundle.putString("PRICE",arrayList.get(position).getPrice());
                bundle.putString("UNIT",arrayList.get(position).getUnit());
                bundle.putString("DESC",arrayList.get(position).getDescription());
                bundle.putString("IMAGE",arrayList.get(position).getImage());
                Intent intent = new Intent(context,ProductDetailActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
