package com.example.btl_nhom4.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl_nhom4.AddEmployees;
import com.example.btl_nhom4.R;
import com.example.btl_nhom4.WorkspaceActivityAdmin;
import com.example.btl_nhom4.model.user.Notification;
import com.example.btl_nhom4.model.user.User;
import com.example.btl_nhom4.model.user.Workspace;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {


    List<Notification> notifications;
    private Context context;

    public NotificationAdapter(List<Notification> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        return new NotificationAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
            Notification notification = notifications.get(position);
            if(notification == null){
                return;
            }
            if(notification.getIsAccept() == 1){
                holder.tv_emailSender.setText(notification.getEmailSender());
                holder.tv_message.setText(notification.getMessage());
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnRefure.setVisibility(View.GONE);
                holder.tv_accept.setVisibility(View.VISIBLE);
                holder.item_notification.setBackground(ContextCompat.getDrawable(context,R.color.notificationWatched));
            }
           else if(notification.getIsRefuse() == 1){
                holder.tv_emailSender.setText(notification.getEmailSender());
                holder.tv_message.setText(notification.getMessage());
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnRefure.setVisibility(View.GONE);
                holder.tv_refuse.setVisibility(View.VISIBLE);
                holder.item_notification.setBackground(ContextCompat.getDrawable(context,R.color.notificationWatched));
            }
           else{
                holder.tv_emailSender.setText(notification.getEmailSender());
                holder.tv_message.setText(notification.getMessage());
                holder.item_notification.setBackground(ContextCompat.getDrawable(context,R.color.notificationActive));
            }
            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference();
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    int idWsp = notification.getIdWorkspace();
                    DatabaseReference reference1 = database.getReference();
                    reference.child("Workspaces").child(String.valueOf(notification.getIdWorkspace())).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Workspace workspace = snapshot.getValue(Workspace.class);

                            reference1.child("Users").child(uid).child("Workspaces").child(String.valueOf(idWsp)).setValue(workspace, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    //complete !!
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // show error when adding data
                        }
                    });
                    reference.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            reference1.child("Workspaces").child(String.valueOf(idWsp)).child("Employees").child(user.getUid())
                                    .setValue(user, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            //complete !!
                                        }
                                    });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //show error when adding data
                        }
                    });
                    reference.child("Users").child(uid).child("Messages").child(String.valueOf(notification.getIdMessage())).child("isAccept").setValue(1);

                    Toast.makeText(context,"Bạn đã tham gia workspace",Toast.LENGTH_SHORT).show();
                    holder.btnAccept.setVisibility(View.GONE);
                    holder.btnRefure.setVisibility(View.GONE);
                    holder.tv_accept.setVisibility(View.VISIBLE);

                }

            });
            holder.btnRefure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference();
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Toast.makeText(context,"Bạn đã từ chối tham gia workspace",Toast.LENGTH_SHORT).show();
                    reference.child("Users").child(uid).child("Messages").child(String.valueOf(notification.getIdMessage())).child("isRefuse").setValue(1);
                    holder.btnAccept.setVisibility(View.GONE);
                    holder.btnRefure.setVisibility(View.GONE);
                    holder.tv_refuse.setVisibility(View.VISIBLE);
                }
            });
    }
    @Override
    public int getItemCount() {
        if(notifications != null){
            return notifications.size();
        }
        return 0;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView tv_emailSender,tv_message,tv_accept,tv_refuse;
        LinearLayout btnAccept,btnRefure,item_notification;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_emailSender = itemView.findViewById(R.id.tv_emailSender);
            tv_message = itemView.findViewById(R.id.tv_message);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnRefure = itemView.findViewById(R.id.btnRefuse);
            item_notification = itemView.findViewById(R.id.item_notification);
            tv_accept = itemView.findViewById(R.id.tv_accept);
            tv_refuse = itemView.findViewById(R.id.tv_refuse);
        }
    }
}
