package fan.ringtone.datastructure;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import fan.ringtone.R;
import fan.ringtone.util.ProgramConstants;
import fan.ringtone.util.ResourceLoader;
import fan.ringtone.util.ProgramConstants.FREQUENCY;
import fan.ringtone.util.ProgramConstants.NOTIFYTYPE;

public class Job {

	private DateNode startTime;
	private DateNode endTime;
	private NOTIFYTYPE ringType;
	private FREQUENCY frequency;
	private List<Integer> days = new ArrayList<Integer>();
	private boolean valid;
	private Context c;

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Job(Context c, boolean isDummy) {
		this.c = c;
		valid = !isDummy;
	}

	public Job(Context c, String encodedString) {
		this.c = c;
		String[] token = encodedString.split(" ");
		if (token.length != 5) {
			valid = false;
			return;
		} else {
			ringType = ProgramConstants.Int2notifyMap.get(Integer
					.parseInt(token[0]));
			frequency = ProgramConstants.Int2FreqMap.get(Integer
					.parseInt(token[1]));
			String dayString = token[2];
			String[] dayToken = dayString.split(",");
			for (String day : dayToken) {
				try {
					int d = Integer.parseInt(day);
					days.add(d);
				} catch (NumberFormatException e) {
				}
			}
			startTime = new DateNode(token[3]);
			endTime = new DateNode(token[4]);
			if (ringType != null && frequency != null)
				valid = true;
			else
				valid = false;
		}
	}

	public Job(Context c, DateNode st, DateNode et, NOTIFYTYPE r, FREQUENCY f) {
		this.c = c;
		valid = true;
		setStartTime(st);
		setEndTime(et);
		setRingType(r);
		setFrequency(f);
	}

	public void setStartTime(DateNode startTime) {
		this.startTime = startTime;
	}

	public DateNode getStartTime() {
		return startTime;
	}

	public void setEndTime(DateNode endTime) {
		this.endTime = endTime;
	}

	public DateNode getEndTime() {
		return endTime;
	}

	public void setRingType(NOTIFYTYPE ringType) {
		this.ringType = ringType;
	}

	public NOTIFYTYPE getRingType() {
		return ringType;
	}

	public void setFrequency(FREQUENCY frequency) {
		this.frequency = frequency;
	}

	public FREQUENCY getFrequency() {
		return frequency;
	}

	public String getDaysToString() {
		StringBuilder sb = new StringBuilder();
		sb.append(ResourceLoader.getString(c, R.string.every) + " ");
		if (containsDay(Calendar.MONDAY))
			sb.append(ResourceLoader.getString(c, R.string.monday) + " ");
		if (containsDay(Calendar.TUESDAY))
			sb.append(ResourceLoader.getString(c, R.string.tuesday) + " ");
		if (containsDay(Calendar.WEDNESDAY))
			sb.append(ResourceLoader.getString(c, R.string.wednesday) + " ");
		if (containsDay(Calendar.THURSDAY))
			sb.append(ResourceLoader.getString(c, R.string.thursday) + " ");
		if (containsDay(Calendar.FRIDAY))
			sb.append(ResourceLoader.getString(c, R.string.friday) + " ");
		if (containsDay(Calendar.SATURDAY))
			sb.append(ResourceLoader.getString(c, R.string.saturday) + " ");
		if (containsDay(Calendar.SUNDAY))
			sb.append(ResourceLoader.getString(c, R.string.sunday));
		return sb.toString();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (valid) {
			sb.append(this.getNofityTypeToString() + ", ");
			if (!frequency.equals(FREQUENCY.INDIVIDUALDAY)) {
				sb.append(this.getFrequencyToString());
				sb.append(", " + startTime.toString() + " ~ "
						+ endTime.toString());
			} else {
				sb.append(getDaysToString());
				sb.append(", " + startTime.toString() + " ~ "
						+ endTime.toString());
			}
		} else {
			sb.append(ResourceLoader.getString(c, R.string.norulesyet));
		}
		return sb.toString();
	}

	public String toEncodedString() {
		StringBuilder sb = new StringBuilder();
		if (valid) {
			sb.append(ProgramConstants.notify2IntMap.get(ringType) + " "
					+ ProgramConstants.freq2IntMap.get(frequency) + " "
					+ makeEncodedStringForDays() + " " + startTime.toString()
					+ " " + endTime.toString());
			return sb.toString();
		} else {
			return null;
		}
	}

	public String makeEncodedStringForDays() {
		StringBuilder sb = new StringBuilder();
		sb.append(",");
		for (Integer day : days) {
			sb.append(day + ",");
		}
		return sb.toString();

	}

	public void setDays(List<Integer> days) {
		this.days = days;
	}

	public List<Integer> getDays() {
		return days;
	}

	public boolean containsWeekday() {
		return containsDay(Calendar.MONDAY) || containsDay(Calendar.TUESDAY)
				|| containsDay(Calendar.WEDNESDAY)
				|| containsDay(Calendar.THURSDAY)
				|| containsDay(Calendar.FRIDAY);
	}

	public boolean containsWeekend() {
		return containsDay(Calendar.SUNDAY) || containsDay(Calendar.SATURDAY);
	}

	public boolean containsDay(int day) {
		return days.contains(day);
	}

	public String getFrequencyToString() {
		StringBuilder sb = new StringBuilder();
		switch (frequency) {
		case EVERYDAY:
			sb.append(ResourceLoader.getString(c, R.string.everyday));
			break;
		case WEEKDAY:
			sb.append(ResourceLoader.getString(c, R.string.weekday));
			break;
		case WEEKEND:
			sb.append(ResourceLoader.getString(c, R.string.weekend));
			break;
		}
		return sb.toString();
	}

	public String getNofityTypeToString() {
		StringBuilder sb = new StringBuilder();
		switch (ringType) {
		case RING:
			sb.append(ResourceLoader.getString(c, R.string.radioRing));
			break;
		case RINGVIBRATE:
			sb
					.append(ResourceLoader.getString(c,
							R.string.radioRingAndVibrate));
			break;
		case SILENT:
			sb.append(ResourceLoader.getString(c, R.string.radioSilent));
			break;
		case VIBRATE:
			sb.append(ResourceLoader.getString(c, R.string.radioVibrate));
			break;
		}
		return sb.toString();

	}

	public Context getContext() {
		return c;
	}

}
