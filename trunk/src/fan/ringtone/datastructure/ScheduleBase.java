package fan.ringtone.datastructure;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import fan.ringtone.util.ConfigUtil;
import fan.ringtone.util.ProgramConstants.FREQUENCY;

public class ScheduleBase {
	private List<Job> jobs = null;
	private static ScheduleBase base = null;

	public int getJobCount() {
		if (jobs == null)
			return 0;
		else
			return jobs.size();
	}

	private ScheduleBase() {
		setJobs(new ArrayList<Job>());
	}

	public static ScheduleBase getInstance() {
		if (base == null)
			base = new ScheduleBase();
		return base;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public void addJob(Context c, String line) {
		Job newJob = new Job(c, line);
		if (newJob.isValid())
			jobs.add(newJob);
	}

	public Job addJob(Job newJob) {
		Job result = hasTimeOverlap(newJob);
		if (result == null)
			jobs.add(newJob);
		return result;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void clearAllJobs() {
		jobs.clear();
	}

	public void removeJob(long id) {
		if (jobs == null || jobs.size() <= id)
			return;
		else
			jobs.remove((int) id);
	}

	public JobEvent getNextEvent(Context c) {
		Calendar cal = Calendar.getInstance();
		Date d = cal.getTime();
		int today = cal.get(Calendar.DAY_OF_WEEK);
		FREQUENCY freq = today == Calendar.SUNDAY || today == Calendar.SATURDAY ? FREQUENCY.WEEKEND
				: FREQUENCY.WEEKDAY;
		long currentTime = d.getHours() * 60 + d.getMinutes();

		Job nextJob = null;
		long nextFrom = 0;
		long nextTo = 0;
		boolean start = false;

		for (Job j : jobs) {
			if (j.isValid()) {
				FREQUENCY jfreq = j.getFrequency();
				if (!jfreq.equals(freq)
						&& !jfreq.equals(FREQUENCY.EVERYDAY)
						&& !(jfreq.equals(FREQUENCY.INDIVIDUALDAY) && j
								.containsDay(today)))
					continue;
				long from = j.getStartTime().getTime();
				long to = j.getEndTime().getTime();
				if (from >= currentTime || to >= currentTime) {
					if (nextJob == null) {
						nextJob = j;
						nextFrom = from;
						nextTo = to;
						if (from >= currentTime)
							start = true;
						else
							start = false;
					} else {
						if (from > nextTo)
							continue;
						else if (from == nextTo && start == false) {
							start = true;
							nextJob = j;
							nextFrom = from;
							nextTo = to;
						} else if (to == nextFrom) {
							continue;
						} else if (to < nextFrom) {
							nextJob = j;
							start = (currentTime <= from);
							nextFrom = from;
							nextTo = to;
						}
					}
				}
			}
		}
		if (nextJob != null) {
			GlobalSetting setting = ConfigUtil.getGlobalSettings(c);
			if (start == false)
				return new JobEvent(new DateNode(nextTo), setting.defaultRing,
						nextJob);
			else
				return new JobEvent(new DateNode(nextFrom), nextJob
						.getRingType(), nextJob);
		} else
			return null;
	}

	/**
	 * check if j has time overlap with any jobs, if so, return the overlapping
	 * job
	 * 
	 * @param j
	 * @return
	 */
	private Job hasTimeOverlap(Job j) {
		if (j.isValid()) {
			FREQUENCY jFreq = j.getFrequency();
			for (Job current : jobs) {
				if (current.isValid()) {
					FREQUENCY cFreq = current.getFrequency();
					boolean wontOverlap = true;
					if (cFreq.equals(FREQUENCY.EVERYDAY)
							|| jFreq.equals(FREQUENCY.EVERYDAY)) {
						wontOverlap = false;
					} else if (jFreq.equals(FREQUENCY.WEEKDAY)
							&& cFreq.equals(FREQUENCY.WEEKEND)) {
						continue;
					} else if (cFreq.equals(FREQUENCY.WEEKDAY)
							&& jFreq.equals(FREQUENCY.WEEKEND)) {
						continue;
					} else if (cFreq.equals(FREQUENCY.INDIVIDUALDAY)) {
						switch (jFreq) {
						case INDIVIDUALDAY:
							for (Integer day : j.getDays()) {
								if (current.containsDay(day)) {
									wontOverlap = false;
									break;
								}
							}
							break;
						case WEEKDAY:
							if (current.containsWeekday())
								wontOverlap = false;
							break;
						case WEEKEND:
							if (current.containsWeekend())
								wontOverlap = false;
							break;
						case EVERYDAY:
							wontOverlap = false;
							break;
						}
					} else if (jFreq.equals(FREQUENCY.INDIVIDUALDAY)) {
						switch (cFreq) {
						case INDIVIDUALDAY:
							for (Integer day : j.getDays()) {
								if (current.containsDay(day)) {
									wontOverlap = false;
									break;
								}
							}
							break;
						case WEEKDAY:
							if (j.containsWeekday())
								wontOverlap = false;
							break;
						case WEEKEND:
							if (j.containsWeekend())
								wontOverlap = false;
							break;
						case EVERYDAY:
							wontOverlap = false;
							break;
						}
					} else if (cFreq.equals(jFreq)) {
						wontOverlap = false;
					}
					if (!wontOverlap) {
						long cFrom = current.getStartTime().getTime();
						long cTo = current.getEndTime().getTime();
						long jFrom = j.getStartTime().getTime();
						long jTo = j.getEndTime().getTime();
						if (jTo <= cFrom || jFrom >= cTo)
							continue;
						else
							return current;
					}
				}
			}
		}
		return null;
	}
}
