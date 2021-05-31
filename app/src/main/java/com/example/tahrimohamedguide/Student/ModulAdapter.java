package com.example.tahrimohamedguide.Student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tahrimohamedguide.Admin.AdminAdapter;
import com.example.tahrimohamedguide.Models.Course;
import com.example.tahrimohamedguide.Models.Users;
import com.example.tahrimohamedguide.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ModulAdapter extends RecyclerView.Adapter<ModulAdapter.AdminViewHolder> {
    private ArrayList<Course> UAL;
    private Context context;
    private ModulAdapter.adminListener listener;
    private ModulAdapter() {

    }

    public ModulAdapter(Context context, ArrayList<Course> UAL, ModulAdapter.adminListener listener) {
        this.context = context;
        this.UAL = UAL;
        this.listener=listener;

    }


    @NonNull
    @Override
    public ModulAdapter.AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.moduls_item_list, parent, false);
        ModulAdapter.AdminViewHolder AVH = new ModulAdapter.AdminViewHolder(view);
        return AVH;
    }




    @Override
    public void onBindViewHolder(@NonNull ModulAdapter.AdminViewHolder holder, int position, @NonNull @org.jetbrains.annotations.NotNull List payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ModulAdapter.AdminViewHolder holder, int position) {
        holder.TextStudent.setText(UAL.get(position).getName());

    }



    @Override
    public int getItemCount() {
        return UAL.size();
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;
        private TextView TextStudent;
        private CardView LL ;

        public AdminViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.CourseImage);
            TextStudent = itemView.findViewById(R.id.textStudent);
            LL = itemView.findViewById(R.id.Teacher);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCourseClick(getAdapterPosition());
                }
            });
        }
    }

    public interface adminListener {
        void onCourseClick(int position);

    }
}