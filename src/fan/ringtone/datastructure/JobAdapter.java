package fan.ringtone.datastructure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fan.ringtone.R;
import fan.ringtone.util.ResourceLoader;
import fan.ringtone.util.ProgramConstants.FREQUENCY;

public class JobAdapter extends BaseAdapter {

	private Context context;
	ScheduleBase schedule;
	ImageView icon;

	public JobAdapter(Context context, Job[] jobs) {
		this.context = context;
		schedule = ScheduleBase.getInstance();
		for (Job j : jobs) {
			schedule.addJob(j);
		}
	}

	public JobAdapter(Context context, ScheduleBase schedule) {
		this.context = context;
		this.schedule = schedule;
	}

	public int getCount() {
		return schedule.getJobCount();
	}

	public Object getItem(int position) {
		return schedule.getJobs().get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View row = inflater.inflate(R.layout.jobrow, null);
		TextView freq = (TextView) row.findViewById(R.id.frequencyText);
		TextView notifyType = (TextView) row.findViewById(R.id.notifyTypeText);
		TextView from = (TextView) row.findViewById(R.id.fromText);
		TextView to = (TextView) row.findViewById(R.id.toText);
		icon = (ImageView) row.findViewById(R.id.jobIcon);
		Job j = schedule.getJobs().get(position);
		if (!j.isValid()) {
			icon.setImageResource(R.drawable.invalid);
			freq.setText(j.toString());
			notifyType.setText("");
			from.setText("");
			to.setText("");
		} else {
			switch (j.getRingType()) {
			case RING:
				icon.setImageResource(R.drawable.ring);
				break;
			case SILENT:
				icon.setImageResource(R.drawable.mute);
				break;
			case RINGVIBRATE:
				icon.setImageResource(R.drawable.ring2);
				break;
			case VIBRATE:
				icon.setImageResource(R.drawable.vibrate);
				break;
			}
			if (j.getFrequency().equals(FREQUENCY.INDIVIDUALDAY))
				freq.setText(j.getDaysToString());
			else
				freq.setText(j.getFrequencyToString());
			notifyType.setText(j.getNofityTypeToString());
			from.setText(ResourceLoader
					.getString(j.getContext(), R.string.from)
					+ j.getStartTime());
			to.setText(ResourceLoader.getString(j.getContext(), R.string.to)
					+ j.getEndTime());
		}

		return row;
	}
}
