package com.example.groupmembersapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    private List<Member> memberList;
    private Context context;

    public MemberAdapter(List<Member> memberList, Context context) {
        this.memberList = memberList;
        this.context = context;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = memberList.get(position);
        holder.nameTextView.setText(member.getName());
        holder.roleTextView.setText(member.getRole());
        holder.descriptionTextView.setText(member.getDetails());


        // Load image
        try {
            Glide.with(context)
                    .load(member.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.imageView);
        } catch (Exception e) {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            try {

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("member_id", member.getId());
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return memberList != null ? memberList.size() : 0;
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView, roleTextView, descriptionTextView;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageMember);
            nameTextView = itemView.findViewById(R.id.textName);
            roleTextView = itemView.findViewById(R.id.textRole);
            descriptionTextView = itemView.findViewById(R.id.textDescription);
        }
    }
}
