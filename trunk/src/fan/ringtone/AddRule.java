package fan.ringtone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import fan.ringtone.datastructure.DateNode;
import fan.ringtone.datastructure.Job;
import fan.ringtone.datastructure.ScheduleBase;
import fan.ringtone.util.EditConstants;
import fan.ringtone.util.ResourceLoader;
import fan.ringtone.util.RuleUtil;
import fan.ringtone.util.ProgramConstants.FREQUENCY;
import fan.ringtone.util.ProgramConstants.NOTIFYTYPE;

public class AddRule extends Activity {

	private TimePicker fromTime;
	private TimePicker toTime;
	private Button btnOK;
	private Button btnCancel;
	private RadioButton silent;
	private RadioButton vibrate;
	private RadioButton ring;
	private RadioButton ringvibrate;

	private ScheduleBase jobs;

	private RadioButton everyday;
	private RadioButton weekday;
	private RadioButton weekend;
	private CheckBox mon, tue, wed, thur, fri, sat, sun;

	private TextView title;
	private long editID = -1;

	private enum Type {
		add, edit
	};

	private Type intentType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.addrule);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		String myintent = this.getIntent().getStringExtra(EditConstants.INTENT);
		if (myintent != null && myintent.equals(IntentGroup.EDITRULE)) {
			intentType = Type.edit;
			editID = this.getIntent().getLongExtra(EditConstants.ITEMKEY, -1);
		} else {
			intentType = Type.add;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		title = (TextView) this.findViewById(R.id.addoreditTitle);

		btnOK = (Button) this.findViewById(R.id.btnAddRuleOK);
		btnCancel = (Button) this.findViewById(R.id.btnAddRuleCancel);
		fromTime = (TimePicker) this.findViewById(R.id.fromTime);
		toTime = (TimePicker) this.findViewById(R.id.toTime);

		silent = (RadioButton) this.findViewById(R.id.radioSlient);
		vibrate = (RadioButton) this.findViewById(R.id.radioVibrate);
		ring = (RadioButton) this.findViewById(R.id.radioRing);
		ringvibrate = (RadioButton) this.findViewById(R.id.radioRingAndVibrate);

		everyday = (RadioButton) this.findViewById(R.id.everyday);
		weekday = (RadioButton) this.findViewById(R.id.weekday);
		weekend = (RadioButton) this.findViewById(R.id.weekend);

		mon = (CheckBox) this.findViewById(R.id.mon);
		tue = (CheckBox) this.findViewById(R.id.tue);
		wed = (CheckBox) this.findViewById(R.id.wed);
		thur = (CheckBox) this.findViewById(R.id.thur);
		fri = (CheckBox) this.findViewById(R.id.fri);
		sat = (CheckBox) this.findViewById(R.id.sat);
		sun = (CheckBox) this.findViewById(R.id.sun);

		switch (intentType) {
		case add:
			title.setText(R.string.addRuleWelcome);
			break;
		case edit:
			title.setText(R.string.editRuleWelcome);
			jobs = ScheduleBase.getInstance();
			Job editee = jobs.getJobs().get((int) editID);
			if (editee != null) {
				initEditFrequency(editee);
				initEditTimePicker(fromTime, editee.getStartTime());
				initEditTimePicker(toTime, editee.getEndTime());
				initEditRingType(editee.getRingType());
			}
			break;
		}
		this.setDaysCheckbox(mon);
		this.setDaysCheckbox(tue);
		this.setDaysCheckbox(wed);
		this.setDaysCheckbox(thur);
		this.setDaysCheckbox(fri);
		this.setDaysCheckbox(sat);
		this.setDaysCheckbox(sun);
		this.setFreqCheckbox(everyday);
		this.setFreqCheckbox(weekday);
		this.setFreqCheckbox(weekend);
		this.setupCancelButton();
		this.setupOKButton();
	}

	private void initEditFrequency(Job editee) {
		FREQUENCY freq = editee.getFrequency();
		switch (freq) {
		case EVERYDAY:
			everyday.setChecked(true);
			break;
		case WEEKDAY:
			weekday.setChecked(true);
			break;
		case WEEKEND:
			weekend.setChecked(true);
			break;
		case INDIVIDUALDAY:
			everyday.setChecked(false);
			weekday.setChecked(false);
			weekend.setChecked(false);
			if (editee.containsDay(Calendar.MONDAY))
				mon.setChecked(true);
			if (editee.containsDay(Calendar.TUESDAY))
				tue.setChecked(true);
			if (editee.containsDay(Calendar.WEDNESDAY))
				wed.setChecked(true);
			if (editee.containsDay(Calendar.THURSDAY))
				thur.setChecked(true);
			if (editee.containsDay(Calendar.FRIDAY))
				fri.setChecked(true);
			if (editee.containsDay(Calendar.SATURDAY))
				sat.setChecked(true);
			if (editee.containsDay(Calendar.SUNDAY))
				sun.setChecked(true);
			break;
		}
	}

	private void initEditTimePicker(TimePicker tp, DateNode time) {
		tp.setCurrentHour((int) time.getHour());
		tp.setCurrentMinute((int) time.getMinutes());
	}

	private void initEditRingType(NOTIFYTYPE type) {
		switch (type) {
		case RING:
			ring.setChecked(true);
			break;
		case RINGVIBRATE:
			ringvibrate.setChecked(true);
			break;
		case SILENT:
			silent.setChecked(true);
			break;
		case VIBRATE:
			vibrate.setChecked(true);
			break;
		}
	}

	private void setFreqCheckbox(RadioButton btn) {
		btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					if (mon.isChecked())
						mon.setChecked(false);
					if (tue.isChecked())
						tue.setChecked(false);
					if (wed.isChecked())
						wed.setChecked(false);
					if (thur.isChecked())
						thur.setChecked(false);
					if (fri.isChecked())
						fri.setChecked(false);
					if (sat.isChecked())
						sat.setChecked(false);
					if (sun.isChecked())
						sun.setChecked(false);
				}
				buttonView.setChecked(isChecked);
			}
		});
	}

	private void setDaysCheckbox(CheckBox box) {
		box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					if (everyday.isChecked())
						everyday.setChecked(false);
					if (weekday.isChecked())
						weekday.setChecked(false);
					if (weekend.isChecked())
						weekend.setChecked(false);
				}
			}
		});
	}

	private void setupCancelButton() {
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void setupOKButton() {
		btnOK.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				switch (intentType) {
				case add:
					if (addNewRule())
						finish();
					break;
				case edit:
					jobs = ScheduleBase.getInstance();
					Job pendingEditee = jobs.getJobs().get((int) editID);
					jobs.removeJob(editID);
					if (addNewRule()) {
						finish();
					} else {
						jobs.addJob(pendingEditee);
					}
				}

			}
		});
	}

	private boolean addNewRule() {
		boolean success = getRuleSetting();
		if (success) {
			updateRules();
		}
		return success;
	}

	private void updateRules() {
		RuleUtil.saveNewSchedules(this, jobs);
		BroadcastRequesterUtil.requestUpdate(this, false, 0);
	}

	private boolean getRuleSetting() {
		int fromHour = fromTime.getCurrentHour();
		int fromMin = fromTime.getCurrentMinute();
		int toHour = toTime.getCurrentHour();
		int toMin = toTime.getCurrentMinute();
		FREQUENCY freq;
		NOTIFYTYPE type;
		List<Integer> days = new ArrayList<Integer>();
		if (everyday.isChecked())
			freq = FREQUENCY.EVERYDAY;
		else if (weekday.isChecked())
			freq = FREQUENCY.WEEKDAY;
		else if (weekend.isChecked())
			freq = FREQUENCY.WEEKEND;
		else {
			freq = FREQUENCY.INDIVIDUALDAY;
			if (mon.isChecked())
				days.add(Calendar.MONDAY);
			if (tue.isChecked())
				days.add(Calendar.TUESDAY);
			if (wed.isChecked())
				days.add(Calendar.WEDNESDAY);
			if (thur.isChecked())
				days.add(Calendar.THURSDAY);
			if (fri.isChecked())
				days.add(Calendar.FRIDAY);
			if (sat.isChecked())
				days.add(Calendar.SATURDAY);
			if (sun.isChecked())
				days.add(Calendar.SUNDAY);
			if (days.size() == 0) {
				Toast.makeText(this, R.string.errorAddRule2, 0).show();
				return false;
			}
		}
		if (silent.isChecked())
			type = NOTIFYTYPE.SILENT;
		else if (vibrate.isChecked())
			type = NOTIFYTYPE.VIBRATE;
		else if (ring.isChecked())
			type = NOTIFYTYPE.RING;
		else if (ringvibrate.isChecked())
			type = NOTIFYTYPE.RINGVIBRATE;
		else
			type = NOTIFYTYPE.RINGVIBRATE;
		if (fromHour * 60 + fromMin < toHour * 60 + toMin) {
			jobs = ScheduleBase.getInstance();
			DateNode start = new DateNode(fromHour, fromMin);
			DateNode end = new DateNode(toHour, toMin);
			Job newJob = new Job(this, start, end, type, freq);
			newJob.setDays(days);
			Job result = jobs.addJob(newJob);
			if (result == null)
				return true;
			else {
				Toast.makeText(
						this,
						ResourceLoader.getString(this, R.string.errorAddRule3)
								+ " " + result.toString(), 1).show();
				return false;
			}
		} else {
			Toast.makeText(this, R.string.errorAddRule1, 0).show();
			return false;
		}
	}
}
