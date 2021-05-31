package com.example.tahrimohamedguide.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tahrimohamedguide.Models.Course;
import com.example.tahrimohamedguide.Models.Users;
import com.example.tahrimohamedguide.Models.specialty;
import com.example.tahrimohamedguide.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {
    private ArrayList<Users> UAL;
    private ArrayList<Course> CAL;
    private ArrayList<specialty> SAL;
    private Context context;
    private adminListener listener;
    private int x ;
    private double y ;
    private AdminAdapter() {

    }

    public AdminAdapter(Context context, ArrayList<Users> UAL, adminListener listener) {
        this.context = context;
        this.UAL = UAL;
        this.listener=listener;

    }
    public AdminAdapter(Context context, ArrayList<Course> UAL,int x, adminListener listener) {
        this.context = context;
        this.CAL = UAL;
        this.listener=listener;
        this.x=x;

    }
    public AdminAdapter(Context context, ArrayList<specialty> UAL, double y, adminListener listener) {
        this.context = context;
        this.SAL = UAL;
        this.listener=listener;
        this.y=y;

    }


    @NonNull
    @Override
    public AdminAdapter.AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chef_department_items_list, parent, false);
         AdminViewHolder AVH = new AdminViewHolder(view);
        return AVH;
    }




    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.AdminViewHolder holder, int position, @NonNull @org.jetbrains.annotations.NotNull List payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdminAdapter.AdminViewHolder holder, int position) {
        if (x==1){
            holder.FullName.setText(CAL.get(position).getName());
            holder.Department.setText(CAL.get(position).getTeacher());
            holder.Email.setVisibility(View.INVISIBLE);
            holder.photo.setImageResource(R.drawable.modul);

        } else if(y==2){
            holder.FullName.setText(SAL.get(position).getName());
            holder.Department.setText(SAL.get(position).getDepartment());
            holder.Email.setVisibility(View.INVISIBLE);
            holder.photo.setImageResource(R.drawable.modul);


        }else {
         holder.FullName.setText(UAL.get(position).getUser_name()+" "+UAL.get(position).getUser_prename());
         holder.Department.setText(UAL.get(position).getUser_department());
         holder.Email.setText(UAL.get(position).getUser_email());
         if(UAL.get(position).getImageid()!=null){
        Picasso.get().load(UAL.get(position).getImageid()).into(holder.photo);
         }
        }
    }



    @Override
    public int getItemCount() {
        if(x==1){
            return CAL.size();

        }else if(y==2){

            return SAL.size();

        }else  return UAL.size();
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView photo;
        private TextView FullName,Department,Email;
        private LinearLayout LL ;

        public AdminViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.Chef_D_photoUrl);
            FullName = itemView.findViewById(R.id.Chef_D_FullName);
            Department = itemView.findViewById(R.id.Chef_D_Department);
            Email = itemView.findViewById(R.id.Chef_D_Email);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChefClick(getAdapterPosition());
                }
            });
        }
    }

    public interface adminListener {
        void onChefClick(int position);

    }
}
