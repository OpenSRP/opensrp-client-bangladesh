package org.smartregister.cbhc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.smartregister.cbhc.R;
import org.smartregister.cbhc.domain.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private Context context;
    private List<Contact> contacts;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View cardLayout;
        public TextView name;
        public TextView requiredFields;
        public View completeLayout;


        public ViewHolder(View view) {
            super(view);
            cardLayout = view.findViewById(R.id.card_layout);
            name = view.findViewById(R.id.container_name);
            requiredFields = view.findViewById(R.id.required_fields);
            completeLayout = view.findViewById(R.id.complete_layout);
        }
    }

    public ContactAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_card_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.cardLayout.setBackgroundResource(contact.getBackground());
        holder.name.setText(contact.getName());
        if (contact.getRequiredFields() > 0) {
            holder.requiredFields.setText(String.format(context.getString(R.string.required_fields), contact.getRequiredFields()));

            holder.requiredFields.setVisibility(View.VISIBLE);
            holder.completeLayout.setVisibility(View.GONE);
        } else {
            holder.completeLayout.setVisibility(View.VISIBLE);
            holder.requiredFields.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
