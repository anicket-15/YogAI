package com.anicket.yogai;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.lang.reflect.Field;

public class YogaAdapter extends FirebaseRecyclerAdapter<YogaPose,YogaAdapter.YogaViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public YogaAdapter(@NonNull FirebaseRecyclerOptions<YogaPose> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull YogaViewHolder holder, int position, @NonNull final YogaPose model) {
        holder.yogaName.setText(model.getPoseName());
        holder.description.setText(model.getDescription());
        Glide.with(holder.itemView.getContext()).load(model.getResourceUrl()).into(holder.imageView);
        YogaActivity.progressBar.setVisibility(View.INVISIBLE);
        holder.buttonLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = model.getUrl();
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(youtubeIntent);
            }
        });
        holder.buttonLetsGo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cameraIntent  = new Intent(view.getContext(),CameraActivity.class);
                        view.getContext().startActivity(cameraIntent);
                    }
                }
        );
    }

    @NonNull
    @Override
    public YogaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.yoga_card_view,parent,false);
        return new YogaViewHolder(view);
    }

    public static class YogaViewHolder extends RecyclerView.ViewHolder{

        TextView yogaName,description;
        ImageView imageView;
        Button buttonLearn,buttonLetsGo;

        public YogaViewHolder(@NonNull View itemView) {
            super(itemView);
            yogaName = itemView.findViewById(R.id.yogaName);
            description = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.imageView);
            buttonLearn = itemView.findViewById(R.id.buttonLearn);
            buttonLetsGo = itemView.findViewById(R.id.buttonLetsGo);
        }
    }

}
