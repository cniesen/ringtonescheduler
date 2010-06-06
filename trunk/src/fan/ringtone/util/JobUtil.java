package fan.ringtone.util;

import android.content.Context;
import fan.ringtone.datastructure.Job;

public class JobUtil {
	public static Job[] makeDummyJobSchedule(Context c) {
		Job dummyJob = new Job(c, true);
		Job[] jobs = new Job[1];
		jobs[0] = dummyJob;
		return jobs;
	}
}
