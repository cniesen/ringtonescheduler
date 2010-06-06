package fan.ringtone.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import android.content.Context;
import android.widget.Toast;
import fan.ringtone.R;
import fan.ringtone.datastructure.Job;
import fan.ringtone.datastructure.JobAdapter;
import fan.ringtone.datastructure.ScheduleBase;

public class RuleUtil {

	public static void deleteAllRules(Context v, ScheduleBase schedule) {
		schedule.clearAllJobs();
		saveNewSchedules(v, schedule);
	}

	public static void saveNewSchedules(Context v, ScheduleBase schedule) {
		try {
			OutputStream ous = v.openFileOutput(ProgramConstants.scheduleData,
					Context.MODE_WORLD_READABLE);
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(ous));
			List<Job> jobs = schedule.getJobs();
			for (Job j : jobs) {
				String encodedStr = j.toEncodedString();
				if (encodedStr != null)
					pw.println(encodedStr);
			}
			pw.close();
		} catch (Exception e) {
			Toast.makeText(v, R.string.errorInit, Toast.LENGTH_SHORT).show();
		}
	}

	public static JobAdapter readScheduleList(Context c, ScheduleBase schedule) {
		JobAdapter jobAdapter = null;
		List<Job> jobs = null;
		schedule.clearAllJobs();
		InputStream ins = null;
		try {
			ins = c.openFileInput(ProgramConstants.scheduleData);
		} catch (FileNotFoundException e) {
			return makeDummyJobAdapter(c);
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					ins));
			while (true) {
				String line = reader.readLine();
				if (line != null)
					schedule.addJob(c, line);
				else
					break;
			}
		} catch (Exception e) {
		}
		jobs = schedule.getJobs();
		if (jobs.size() == 0)
			return makeDummyJobAdapter(c);
		else {
			jobAdapter = new JobAdapter(c, schedule);
			return jobAdapter;
		}
	}

	private static JobAdapter makeDummyJobAdapter(Context c) {
		Job[] jobs = JobUtil.makeDummyJobSchedule(c);
		JobAdapter jobAdapter = new JobAdapter(c, jobs);
		return jobAdapter;
	}

	public static void deleteSingleRule(Context v, ScheduleBase schedule,
			long id) {
		if (schedule.getJobCount() == 0)
			return;
		else {
			schedule.removeJob(id);
			saveNewSchedules(v, schedule);
		}
	}
}
