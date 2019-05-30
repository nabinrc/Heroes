package adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.heroesapi.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import model.User;
import url.Url;

public class HeroesAdapter extends RecyclerView.Adapter<HeroesAdapter.HeroViewHolder> {

    private List<User> userList;
    private Context context;

    public HeroesAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_dashboard, viewGroup, false);


        return new HeroViewHolder(view);
    }

    private void StrictMode() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder viewHolder, int i) {
        User user = userList.get(i);
        String imgPath = Url.BASE_URL + "uploads/" + user.getImage();
        StrictMode();
        try {
            URL url = new URL(imgPath);
            viewHolder.imgPhoto.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        viewHolder.tvName.setText(user.getName());
        viewHolder.tvDesc.setText(user.getDesc());


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public class HeroViewHolder extends  RecyclerView.ViewHolder{
        ImageView imgPhoto;
        TextView tvName, tvDesc;

        public HeroViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            tvName = itemView.findViewById(R.id.tvName);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }
    }

}