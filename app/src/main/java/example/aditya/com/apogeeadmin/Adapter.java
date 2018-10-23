package example.aditya.com.apogeeadmin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private Context context;
    private ArrayList<Shows> mItems;


    public Adapter(Context context, ArrayList<Shows> data) {
        this.context = context;
        mItems = new ArrayList();
        this.mItems = data;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView id;
        TextView name;
        TextView price;
        TextView date;
        TextView time;
        TextView venue;


        private MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            this.id = itemView.findViewById(R.id.id_tv);
            this.name = itemView.findViewById(R.id.name_tv);
            this.price = itemView .findViewById(R.id.price_tv);
            this.date = itemView.findViewById(R.id.date_tv);
            this.time = itemView.findViewById(R.id.time_tv);
            this.venue = itemView .findViewById(R.id.venue_tv);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

       Log.e("size",String.valueOf(mItems.size()));
        holder.id.setText(String.valueOf(mItems.get(position).getShowId()));
        holder.name.setText(mItems.get(position).getName());
        holder.price.setText(String.valueOf(mItems.get(position).getPrice()));
        holder.date.setText(mItems.get(position).getDate());
        holder.time.setText(mItems.get(position).getTime());
        holder.venue.setText(mItems.get(position).getVenue());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ContinuousCaptureActivity.class );
                intent.putExtra("pos",position);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if(mItems== null) return 0;
       else  return mItems.size();
    }
}
