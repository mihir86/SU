package com.example.su;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.su.Items.CabShareRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddOrEditCabSharingBottomDialogFragment extends BottomSheetDialogFragment {

    View mRootView;
    public static final int ADD_CAB_SHARING_REQUEST_MODE = 1001;
    private ChipGroup cabTypeChipGroup;
    private TextInputLayout dateTextInputLayout;
    private TextInputEditText flightDateHoursEditText;
    private TextInputEditText flightDateMinsEditText;
    public static final int EDIT_CAB_SHARING_REQUEST_MODE = 1002;
    private ChipGroup aopChipGroup;
    private TextInputEditText waitTimeHoursEditText;
    private TextInputEditText waitTimeMinsEditText;
    private Button addOrEditRequestButton;
    private TextInputEditText dateInputEditText;
    private FirebaseFirestore db;
    private CabShareRequest mCabShareRequest;
    private int mMode;
    private Context mContext;

    public AddOrEditCabSharingBottomDialogFragment(int mode, Context context) {
        mMode = mode;
        mContext = context;
    }

    public AddOrEditCabSharingBottomDialogFragment(int mode, CabShareRequest cabShareRequest, Context context) {
        mCabShareRequest = cabShareRequest;
        mMode = mode;
        mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_add_cab_sharing, container, false);
        mRootView = rootView;

        db = FirebaseFirestore.getInstance();

        addOrEditRequestButton = rootView.findViewById(R.id.add_request_button);

        cabTypeChipGroup = rootView.findViewById(R.id.cab_type_chip_group);

        dateInputEditText = rootView.findViewById(R.id.date_edit_text);
        flightDateHoursEditText = rootView.findViewById(R.id.time_hours_edit_text);
        flightDateMinsEditText = rootView.findViewById(R.id.mins_hours_edit_text);

        aopChipGroup = rootView.findViewById(R.id.aop_chip_group);

        waitTimeHoursEditText = rootView.findViewById(R.id.edit_wait_hours_edit_text);
        waitTimeMinsEditText = rootView.findViewById(R.id.edit_wait_time_mins_hours_edit_text);


        dateTextInputLayout = rootView.findViewById(R.id.date_edit_text_layout);

        if (mMode == EDIT_CAB_SHARING_REQUEST_MODE) {
            TextView addRequestTextView = rootView.findViewById(R.id.add_new_request_text_view);
            addRequestTextView.setText(mContext.getString(R.string.edit_cab_sharing_request));
            addOrEditRequestButton.setText(R.string.edit_request);

            Chip ctaChip = rootView.findViewById(R.id.campus_to_airport_chip);
            Chip atcChip = rootView.findViewById(R.id.airport_to_campus_chip);

            if (mCabShareRequest.getCabType() == CabShareRequest.CAMPUS_TO_AIRPORT)
                ctaChip.setChecked(true);
            else
                atcChip.setChecked(true);

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            dateInputEditText.setText(dateFormat.format(mCabShareRequest.getFlightDateWithTime()));

            DateFormat flightHourFormat = new SimpleDateFormat("hh");
            flightDateHoursEditText.setText(flightHourFormat.format(mCabShareRequest.getFlightDateWithTime()));
            DateFormat flightMinsFormat = new SimpleDateFormat("mm");
            flightDateMinsEditText.setText(flightMinsFormat.format(mCabShareRequest.getFlightDateWithTime()));

            DateFormat flightAoPFormat = new SimpleDateFormat("a");
            Chip aChip = rootView.findViewById(R.id.time_hours_am_chip);
            Chip pChip = rootView.findViewById(R.id.time_hours_pm_chip);

            if (flightAoPFormat.format(mCabShareRequest.getFlightDateWithTime()).equals("AM"))
                aChip.setChecked(true);
            else
                pChip.setChecked(true);

            double waitTime = mCabShareRequest.getWaitTime();
            double mins = waitTime - (long) waitTime;
            double hrs = waitTime - mins;
            mins = mins * 60;

            waitTimeHoursEditText.setText(Long.toString((long) hrs));
            waitTimeMinsEditText.setText(Long.toString((long) mins));

        }

        dateTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText(R.string.enter_date);

                MaterialDatePicker<Long> picker = builder.build();
                picker.show(getFragmentManager(), picker.toString());
                picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        Date date = new Date(selection);
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
                        String s = dateFormat.format(date);
                        dateInputEditText.setText(s);
                    }
                });
            }
        });

        if (mMode == ADD_CAB_SHARING_REQUEST_MODE) {
            addOrEditRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkDataNotBlankAndDisplayError()) {
                        if (checkDateCorrectAndDisplayError() && checkWaitTimeCorrectAndDisplayError()) {
                            final Map<String, Object> data = new HashMap<>();
                            data.put("requesterPhone", 8007187941L); //TODO: Get user phone number and pass that instead
                            data.put("cabType", getCabType());
                            data.put("flightDateAndTime", getFlightDateAndTime());
                            data.put("waitTime", getWaitTime());
                            addDocumentToFirebaseDatabase(data);
                            dismiss();
                        }
                    }
                }
            });
        } else {
            addOrEditRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkDataNotBlankAndDisplayError()) {
                        if (checkDateCorrectAndDisplayError() && checkWaitTimeCorrectAndDisplayError()) {

                            final Map<String, Object> data = new HashMap<>();
                            data.put("requesterPhone", 8007187941L); //TODO: Get user phone number and pass that instead
                            data.put("cabType", getCabType());
                            data.put("flightDateAndTime", getFlightDateAndTime());
                            data.put("waitTime", getWaitTime());

                            db.collection(mContext.getString(R.string.firebase_database_cab_sharing_collection)).document(mCabShareRequest.getDocumentID())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            addDocumentToFirebaseDatabase(data);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("EditCabShareRequest", "Error deleting document", e);
                                        }
                                    });

                            dismiss();
                        }
                    }
                }
            });


        }

        return rootView;
    }

    private void addDocumentToFirebaseDatabase(Map<String, Object> data) {
        db.collection(mContext.getString(R.string.firebase_database_cab_sharing_collection))
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AddEditCabSharingFragme", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AddEditCabSharingFragme", "Error adding document", e);
                    }
                });
    }

    private int getCabType() {
        if (cabTypeChipGroup.getCheckedChipId() == R.id.campus_to_airport_chip) {
            return CabShareRequest.CAMPUS_TO_AIRPORT;
        } else
            return CabShareRequest.AIRPORT_TO_CAMPUS;
    }

    private Date getFlightDateAndTime() {
        try {
            DateFormat flightDateFormat = new SimpleDateFormat("dd/MM/yy hh:mm a");

            String enteredFlightDateAndTime = dateInputEditText.getText().toString() + " " + flightDateHoursEditText.getText().toString() + ":" + flightDateMinsEditText.getText().toString();
            if (aopChipGroup.getCheckedChipId() == R.id.time_hours_am_chip) {
                enteredFlightDateAndTime += " AM";
            } else {
                enteredFlightDateAndTime += " PM";
            }
            return flightDateFormat.parse(enteredFlightDateAndTime);
        } catch (ParseException e) {
            Log.e("AddCabSharingFragment", "Date was validated earlier, but there's a parse exception. WARNING: Today\'s date has been returned by the getFlightDateAndTime() method");
            Log.e("AddCabSharingFragment", e.getMessage());
            return new Date();
        }
    }

    private double getWaitTime() {
        long hrs = Long.parseLong(waitTimeHoursEditText.getText().toString());
        long mins = Long.parseLong(waitTimeMinsEditText.getText().toString());
        return hrs + (mins / 60d);
    }

    private boolean checkDataNotBlankAndDisplayError() {
        if (cabTypeChipGroup.getCheckedChipId() == ChipGroup.NO_ID) {
            Snackbar.make(mRootView.findViewById(R.id.cab_add_coordinator), "The cab type hasn\'t been selected.", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (dateInputEditText.getText().toString().matches("")) {
            Snackbar.make(mRootView.findViewById(R.id.cab_add_coordinator), "The date of the flight hasn\'t been selected.", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (flightDateHoursEditText.getText().toString().matches("") || flightDateMinsEditText.getText().toString().matches("") || aopChipGroup.getCheckedChipId() == ChipGroup.NO_ID) {
            Snackbar.make(mRootView.findViewById(R.id.cab_add_coordinator), "The time of the flight hasn\'t been selected.", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (waitTimeHoursEditText.getText().toString().matches("") || waitTimeMinsEditText.getText().toString().matches("")) {
            Snackbar.make(mRootView.findViewById(R.id.cab_add_coordinator), "The time you\'re willing to wait hasn\'t been selected.", Snackbar.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }

    private boolean checkDateCorrectAndDisplayError() {
        try {
            DateFormat flightDateFormat = new SimpleDateFormat("dd/MM/yy hh:mm a");

            String enteredFlightDateAndTime = dateInputEditText.getText().toString() + " " + flightDateHoursEditText.getText().toString() + ":" + flightDateMinsEditText.getText().toString();
            if (aopChipGroup.getCheckedChipId() == R.id.time_hours_am_chip)
                enteredFlightDateAndTime += " AM";
            else
                enteredFlightDateAndTime += " PM";

            Date curDate = new Date();
            Date flightDate = flightDateFormat.parse(enteredFlightDateAndTime);
            if (flightDate.before(curDate)) {
                Snackbar.make(mRootView.findViewById(R.id.cab_add_coordinator), "The flight date entered is before today.", Snackbar.LENGTH_LONG).show();
                return false;
            } else
                return true;
        } catch (ParseException e) {
            Snackbar.make(mRootView.findViewById(R.id.cab_add_coordinator), "The date entered is in the wrong format.", Snackbar.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean checkWaitTimeCorrectAndDisplayError() {
        try {
            int hrs = Integer.parseInt(waitTimeHoursEditText.getText().toString());
            int mins = Integer.parseInt(waitTimeMinsEditText.getText().toString());
            if (hrs >= 6) {
                Snackbar.make(mRootView.findViewById(R.id.cab_add_coordinator), "The wait time cannot be more than 6 hours.", Snackbar.LENGTH_LONG).show();
                return false;
            } else if (mins > 60) {
                Snackbar.make(mRootView.findViewById(R.id.cab_add_coordinator), "The wait time minutes cannot be more than 60 minutes.", Snackbar.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            Snackbar.make(mRootView.findViewById(R.id.cab_add_coordinator), "The wait time hasn\'t been entered in the correct format.", Snackbar.LENGTH_LONG).show();
            return false;
        }
    }

}
