package com.example.su.Adapters;

//public class CabShareAdapter extends RecyclerView.Adapter<CabShareAdapter.MyViewHolder>{
//
//    private ArrayList<CabShareRequest> mDataset;
//
//    public void CabShareAdapter(ArrayList<CabShareRequest> cabRequest) {
//        mDataset = cabRequest;
//    }
//
//    private class MyViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView cabShareTitleTextView;
//        public TextView cabDateTimeTextView;
//        public TextView cabFlexibilityTextView;
//        public TextView noMatchesTextView;
//        public TextView matchesTextView;
//
//        public MyViewHolder(View itemView) {
//
//            super(itemView);
//
//            cabShareTitleTextView = itemView.findViewById(R.id.cab_share_title_text_view);
//            cabDateTimeTextView = itemView.findViewById(R.id.cab_date_time_text_view);
//            cabFlexibilityTextView = itemView.findViewById(R.id.cab_flexibility_text_view);
//            noMatchesTextView = itemView.findViewById(R.id.no_matches_found_text_view);
//            matchesTextView = itemView.findViewById(R.id.matches_text_View);
//
//        }
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Context context = parent.getContext();
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View cabShareView = inflater.inflate(R.layout.cab_share_list_item, parent, false);
//        return new MyViewHolder(cabShareView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//
//        CabShareRequest cabRequest = mDataset.get(position);
//
//        TextView cabShareTitleTextView = holder.cabShareTitleTextView;
//        TextView cabDateTimeTextView = holder.cabDateTimeTextView;
//        TextView cabFlexibilityTextView = holder.cabFlexibilityTextView;
//        TextView noMatchesTextView = holder.noMatchesTextView;
//        TextView matchesTextView = holder.matchesTextView;
//
//        //TODO: Get data from firebase and bind it to the TextViews
//
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mDataset.size();
//    }
//}
