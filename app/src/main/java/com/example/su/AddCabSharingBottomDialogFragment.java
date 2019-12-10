package com.example.su;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddCabSharingBottomDialogFragment extends BottomSheetDialogFragment {

    public static final int CAMPUS_TO_AIRPORT = 101;
    public static final int AIRPORT_TO_CAMPUS = 102;
    View mRootView;
    private Button addRequestButton;
    private ChipGroup cabTypeChipGroup;
    private TextInputLayout dateTextInputLayout;
    private TextInputEditText flightDateHoursEditText;
    private TextInputEditText flightDateMinsEditText;
    private TextInputEditText dateeInputEditText;
    private ChipGroup aopChipGroup;
    private TextInputEditText waitTimeHoursEditText;
    private TextInputEditText waitTimeMinsEditText;

    public AddCabSharingBottomDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_add_cab_sharing, container, false);
        mRootView = rootView;

        addRequestButton = rootView.findViewById(R.id.add_request_button);

        cabTypeChipGroup = rootView.findViewById(R.id.cab_type_chip_group);

        dateeInputEditText = rootView.findViewById(R.id.date_edit_text);
        flightDateHoursEditText = rootView.findViewById(R.id.time_hours_edit_text);
        flightDateMinsEditText = rootView.findViewById(R.id.mins_hours_edit_text);

        aopChipGroup = rootView.findViewById(R.id.aop_chip_group);

        waitTimeHoursEditText = rootView.findViewById(R.id.wait_hours_edit_text);
        waitTimeMinsEditText = rootView.findViewById(R.id.wait_time_mins_hours_edit_text);


        dateTextInputLayout = rootView.findViewById(R.id.date_edit_text_layout);
        final TextInputEditText dateInputEditText = rootView.findViewById(R.id.date_edit_text);
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

        addRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Validate data and display errors (if any).
                if (checkDataNotBlankAndDisplayError()) {
                    if (checkDateCorrectAndDisplayError()) {
                        //TODO: Data entered has no issues. Process it.
                        dismiss();
                    }
                }
            }
        });

        return rootView;
    }

    private boolean checkDataNotBlankAndDisplayError() {
        if (cabTypeChipGroup.getCheckedChipId() == ChipGroup.NO_ID) {
            Snackbar.make(mRootView.findViewById(R.id.cab_add_coordinator), "The cab type hasn\'t been selected.", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (dateeInputEditText.getText().toString().matches("")) {
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

            String enteredFlightDateAndTime = dateeInputEditText.getText().toString() + " " + flightDateHoursEditText.getText().toString() + ":" + flightDateHoursEditText.getText().toString();
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

}
