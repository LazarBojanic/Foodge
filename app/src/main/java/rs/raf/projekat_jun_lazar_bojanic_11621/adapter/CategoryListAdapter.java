package rs.raf.projekat_jun_lazar_bojanic_11621.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.database.remote.model.Category;
import rs.raf.projekat_jun_lazar_bojanic_11621.util.PlaceHolders;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {
    private List<Category> categoryList;
    private OnCategoryClickListener onCategoryClickListener;
    private Context context;
    public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener){
        this.onCategoryClickListener = onCategoryClickListener;
    }

    public CategoryListAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCategoryName;
        private ImageView imageViewCategoryImage;
        private Button buttonShowCategoryDescription;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
            imageViewCategoryImage = itemView.findViewById(R.id.imageViewCategoryImage);
            buttonShowCategoryDescription = itemView.findViewById(R.id.buttonShowCategoryDescription);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onCategoryClickListener != null) {
                    Category category = categoryList.get(position);
                    onCategoryClickListener.onCategoryClick(category);
                }
            });
            buttonShowCategoryDescription.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Category category = categoryList.get(position);
                    showCategoryDescriptionDialog(category.getStrCategoryDescription());
                }
            });
        }

        public void bind(Category category) {
            textViewCategoryName.setText(category.getStrCategory());
            Glide.with(itemView)
                    .load(category.getStrCategoryThumb())
                    .error(PlaceHolders.getInstance().getPlaceHolderImage())
                    .placeholder(PlaceHolders.getInstance().getPlaceHolderImage()).into(imageViewCategoryImage);
        }
        private void showCategoryDescriptionDialog(String description) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(description)
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        }
    }
}
