package my.edu.utem.uemsattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<Event> events;

    public Adapter(Context ctx, List<Event>events) {
        this.inflater = LayoutInflater.from(ctx);
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.eventName.setText(events.get(position).getName());
        holder.eventDesc.setText(events.get(position).getDescription());
        holder.eventDateTime.setText(events.get(position).getStart_event());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventDesc, eventDateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName = itemView.findViewById(R.id.eventTitle);
            eventDesc = itemView.findViewById(R.id.eventDesc);
            eventDateTime = itemView.findViewById(R.id.eventDate);
        }
    }
}
