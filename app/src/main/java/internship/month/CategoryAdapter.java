package internship.month;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {

    Context context;
    ArrayList<CategoryList> arrayList;
    SharedPreferences sp;

    public CategoryAdapter(Context context, ArrayList<CategoryList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(ConstantSp.PREFERENCE,Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_category_image);
            name = itemView.findViewById(R.id.custom_category_name);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());

        //Picasso
        //Glide
        Picasso.get().load(arrayList.get(position).getImage()).placeholder(R.drawable.loading_new).into(holder.imageView);

        //holder.imageView.setImageResource(arrayList.get(position).getImage());

        /*holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new CommonMethod(context,nameArray[position]);
                clickMessage(position);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new CommonMethod(context,nameArray[position]);
                clickMessage(position);
            }
        });*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickMessage(position);
            }
        });

    }

    private void clickMessage(int position) {
        //new CommonMethod(context,arrayList.get(position).getName());
        sp.edit().putString(ConstantSp.CATEGORY_ID,arrayList.get(position).getId()).commit();
        new CommonMethod(context,ProductActivity.class);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    /*Context context;
    String[] nameArray;
    int[] imageArray;

    public CategoryAdapter(Context context, String[] categoryNameArray, int[] categoryImageArray) {
        this.context = context;
        nameArray = categoryNameArray;
        imageArray = categoryImageArray;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_category_image);
            name = itemView.findViewById(R.id.custom_category_name);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.name.setText(nameArray[position]);
        holder.imageView.setImageResource(imageArray[position]);

        *//*holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new CommonMethod(context,nameArray[position]);
                clickMessage(position);
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new CommonMethod(context,nameArray[position]);
                clickMessage(position);
            }
        });*//*

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickMessage(position);
            }
        });

    }

    private void clickMessage(int position) {
        new CommonMethod(context,nameArray[position]);
    }

    @Override
    public int getItemCount() {
        return nameArray.length;
    }*/
}
