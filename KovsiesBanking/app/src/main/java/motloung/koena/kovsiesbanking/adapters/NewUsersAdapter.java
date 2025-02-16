package motloung.koena.kovsiesbanking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.data.ListOfUsers;

public class NewUsersAdapter extends RecyclerView.Adapter<NewUsersAdapter.ContactViewHolder> {

    private List<ListOfUsers> userList;

    public NewUsersAdapter(List<ListOfUsers> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_item, parent, false);
        return new ContactViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ListOfUsers contact = userList.get(position);
        holder.contactName.setText(contact.getName());
        holder.contactLocation.setText(contact.getLocation());
        holder.profileImage.setImageResource(contact.getImageResource());
        holder.deleteContact.setOnClickListener(v -> {
            // Handle delete contact
            userList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, userList.size());
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactLocation;
        ImageView profileImage, deleteContact;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactLocation = itemView.findViewById(R.id.contactLocation);
            profileImage = itemView.findViewById(R.id.profileImage);
            deleteContact = itemView.findViewById(R.id.deleteContact);
        }
    }
}