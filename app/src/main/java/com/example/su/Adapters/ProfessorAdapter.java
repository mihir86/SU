package com.example.su.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.su.Items.LaundryOrder;
import com.example.su.Items.Professor;
import com.example.su.R;

import java.util.ArrayList;

public class ProfessorAdapter extends RecyclerView.Adapter<ProfessorAdapter.MyViewHolder> {

	private ArrayList<Professor> mDataset;

	public ProfessorAdapter(ArrayList<Professor> professors)
	{
		mDataset = professors;
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView professorNameTextView;
		public TextView professorRoomTextView;
		public TextView professorCourseTextView;
		public ImageView professorAvailable;

		public MyViewHolder(View itemView) {
			super(itemView);

			professorNameTextView = itemView.findViewById(R.id.prof_list_professor_name);
			professorRoomTextView = itemView.findViewById(R.id.prof_list_professor_room);
			professorCourseTextView = itemView.findViewById(R.id.prof_list_professor_course);
			professorAvailable = itemView.findViewById(R.id.prof_status_image_view);
		}
	}

	@NonNull
	@Override
	public ProfessorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View professorView = inflater.inflate(R.layout.professor_list_item, parent, false);
		return new ProfessorAdapter.MyViewHolder(professorView);
	}

	@Override
	public void onBindViewHolder(@NonNull ProfessorAdapter.MyViewHolder holder, int position) {
		Professor professor = mDataset.get(position);

		TextView professorNameTextView = holder.professorNameTextView;
		TextView professorRoomTextView = holder.professorRoomTextView;
		TextView professorCourseTextView = holder.professorCourseTextView;
		ImageView professorAvailable = holder.professorAvailable;

		professorNameTextView.setText(professor.getProfessorName());
		professorRoomTextView.setText(professor.getRoomNumber());
		professorCourseTextView.setText(professor.getCourseCode());
		if(professor.isAvailable())
			professorAvailable.setImageResource(R.drawable.round_event_available_24);
	}

	@Override
	public int getItemCount() {
		return mDataset.size();
	}

}
