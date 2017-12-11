package hu.ait.android.christmasapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;

import hu.ait.android.christmasapp.MainActivity;
import java.util.List;
import hu.ait.android.christmasapp.data.Item;
import hu.ait.android.christmasapp.R;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button btnDelete;
        public TextView tvCity;
        public Button btnDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            tvCity = (TextView) itemView.findViewById(R.id.tvItem);
            btnDetails = (Button) itemView.findViewById(R.id.btnDetails);
        }
    }

    private List<Item> itemsList;
    private Context context;
    private int lastPosition = -1;

    public ItemsAdapter(List<Item> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.tvCity.setText(itemsList.get(position).getItemName());


        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).showItemDetailsActivity(
                        itemsList.get(viewHolder.getAdapterPosition()).getItemID(),
                        viewHolder.getAdapterPosition());
            }
        });

    }
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void addItem(Item item ) {
        itemsList.add(item);
        notifyDataSetChanged();
    }

    public void updateItem(int index, Item item) {
        itemsList.set(index, item);
        notifyItemChanged(index);

    }

    public void resetItemsList(List<Item> itemsList){
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }

    public void removeItem(int index) {
        ((MainActivity)context).deleteItem(itemsList.get(index));
        itemsList.remove(index);
        notifyItemRemoved(index);
    }

    public void swapPlaces(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(itemsList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(itemsList, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }

    public Item getItem(int i) {
        return itemsList.get(i);
    }
}
