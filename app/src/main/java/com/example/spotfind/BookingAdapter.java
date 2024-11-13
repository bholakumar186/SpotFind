package com.example.spotfind;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.userNameTextView.setText("User: " + booking.getUserName());
        holder.ownerNameTextView.setText("Owner: " + booking.getOwnerName());
        holder.parkingLocationTextView.setText("Location: " + booking.getLocation());
        holder.parkingSizeTextView.setText("Size: " + booking.getParkingSize());
        holder.bookingTimeTextView.setText("Time: " + booking.getBookingTime());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }
    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView, ownerNameTextView, parkingLocationTextView, parkingSizeTextView, bookingTimeTextView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            ownerNameTextView = itemView.findViewById(R.id.ownerNameTextView);
            parkingLocationTextView = itemView.findViewById(R.id.parkingLocationTextView);
            parkingSizeTextView = itemView.findViewById(R.id.parkingSizeTextView);
            bookingTimeTextView = itemView.findViewById(R.id.bookingTimeTextView);
        }
    }
}
