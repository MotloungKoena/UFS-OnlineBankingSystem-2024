package motloung.koena.kovsiesbanking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import motloung.koena.kovsiesbanking.R;
import motloung.koena.kovsiesbanking.data.AppUser;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<AppUser> userList;
    private OnUserActionListener userActionListener;

    public UserAdapter(List<AppUser> userList, OnUserActionListener listener) {
        this.userList = userList;
        this.userActionListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        AppUser user = userList.get(position);
        holder.userName.setText(user.getFirstName());
        holder.userEmail.setText(user.getNormalizedEmail());

        holder.editButton.setOnClickListener(v -> userActionListener.onEditUser(user));

        holder.deleteButton.setOnClickListener(v -> userActionListener.onDeleteUser(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userEmail;
        Button editButton, deleteButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            userEmail = itemView.findViewById(R.id.user_email);
            editButton = itemView.findViewById(R.id.btn_edit);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface OnUserActionListener {
        void onEditUser(AppUser user);
        void onDeleteUser(AppUser user);
    }
}