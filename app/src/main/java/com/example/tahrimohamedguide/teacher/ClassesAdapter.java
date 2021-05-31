package com.example.tahrimohamedguide.teacher;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tahrimohamedguide.Models.DownModel;
import com.example.tahrimohamedguide.Models.model;
import com.example.tahrimohamedguide.R;
import com.example.tahrimohamedguide.Student.studentviewpdf;
import com.example.tahrimohamedguide.viewpdf;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.myviewholder> {
    com.example.tahrimohamedguide.Student.studentviewpdf studentviewpdf;
    ArrayList<model> downModels;
    TextView filename;
    private String sModul;
    private ClassesAdapterListiner Listener ;
    private int x = 0;
    private Context context;


    public ClassesAdapter(Context context,  ArrayList<model> downModels, String Modul, ClassesAdapterListiner listener) {

        sModul=Modul;
        this.Listener=listener;
        this.downModels = downModels;
        this.context=context;
    }
    public ClassesAdapter(Context context,  ArrayList<model> downModels,String Modul,int x,ClassesAdapterListiner listener) {
        sModul=Modul;
        this.Listener=listener;
        this.downModels = downModels;
        this.x=x;
        this.context=context;
    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singelrowdesign, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ClassesAdapter.myviewholder holder, int position) {
        if(x==0){
            holder.header.setText(downModels.get(position).getFilename());

            com.example.tahrimohamedguide.Models.model helper = new model(downModels.get(position).getFilename(),downModels.get(position).getFileurl()
                    ,downModels.get(position).getModul(),downModels.get(position).getNod(),downModels.get(position).getNol(),downModels.get(position).getNol());
            downModels.add(helper);

            holder.img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(holder.img1.getContext(), viewpdf.class);
                    intent.putExtra("filename", downModels.get(position).getFilename());
                    intent.putExtra("fileurl", downModels.get(position).getFileurl());

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    holder.img1.getContext().startActivity(intent);

                }
            });
            holder.dislikebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /* Intent intent=new Intent(holder.img1.getContext(),downloadfile.class);
                intent.putExtra("filename",model.getFilename());
                intent.putExtra("fileurl",model.getFileurl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.dislikebook.getContext().startActivity(intent);
*/
                    Listener.onDownloadCourse(position);

                }
            });



        }else {


            holder.header.setText(downModels.get(position).getFilename());


            holder.img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(holder.img1.getContext(), viewpdf.class);
                    intent.putExtra("filename", downModels.get(position).getFilename());
                    intent.putExtra("fileurl", downModels.get(position).getFileurl());

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    holder.img1.getContext().startActivity(intent);

                }
            });
            holder.dislikebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /* Intent intent=new Intent(holder.img1.getContext(),downloadfile.class);
                intent.putExtra("filename",model.getFilename());
                intent.putExtra("fileurl",model.getFileurl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.dislikebook.getContext().startActivity(intent);
*/
                    Listener.onDownloadCourse(position);

                }
            });




        }
    }

    @Override
    public int getItemCount() {
        return downModels.size();}

    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView img1;
        TextView header;
        private CardView CardView;
      //  ImageView readbook, likebook;
        ImageView dislikebook;
       // TextView textviewbook, textlike, textdislike;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.img1);
            header = itemView.findViewById(R.id.header);
            CardView= itemView.findViewById(R.id.CardView);


            dislikebook = itemView.findViewById(R.id.dislikebook);



        }
    }

    public interface ClassesAdapterListiner {
        void onDownloadCourse(int position);

    }
    public  ArrayList<model> getElement (){
        return downModels;
    }

    private void searchInModel (){




    }


}

