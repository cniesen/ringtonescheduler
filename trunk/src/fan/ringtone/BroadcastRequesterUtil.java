package fan.ringtone;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import fan.ringtone.datastructure.JobEvent;

public class BroadcastRequesterUtil {
	/**
	 * request an update after a certain delay
	 * 
	 * @param c
	 *            current context
	 * @param nextMin
	 *            update at the beginning of nex minute?
	 * @param mins
	 *            how long the delay is (if not updating next min)
	 */
	public static void requestUpdate(Context c, boolean nextMin, double mins) {
		AlarmManager alarms = (AlarmManager) c
				.getSystemService(Context.ALARM_SERVICE);
		Intent checkScheduleIntent = new Intent(IntentGroup.UPDATE);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(c, 0,
				checkScheduleIntent, 0);
		if (!nextMin)
			alarms.set(AlarmManager.RTC_WAKEUP, makeDelay(mins), alarmIntent);
		else
			alarms.set(AlarmManager.RTC_WAKEUP, delayToNextMinute(),
					alarmIntent);
	}

	private static long makeDelay(double mins) {
		Date dateNow = new Date();
		return (long) (dateNow.getTime() + (mins) * 60 * 1000);
	}

	private static long delayToNextMinute() {
		Date now = new Date();
		long sec = Calendar.getInstance().get(Calendar.SECOND);
		return now.getTime() - sec * 1000 + 61 * 1000;
	}

	public static void sendChangeRingtoneRequest(Context c, JobEvent nextJob) {
		AlarmManager alarms = (AlarmManager) c
				.getSystemService(Context.ALARM_SERVICE);
		Intent changeRingToneIntent = new Intent(IntentGroup.SETEVENT);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(c, 0,
				changeRingToneIntent, 0);
		alarms.set(AlarmManager.RTC_WAKEUP, calculateNextJobWaitTime(nextJob),
				alarmIntent);
	}

	public static long calculateNextJobWaitTime(JobEvent nextJob) {
		Calendar cal = Calendar.getInstance();
		Date dateNow = new Date();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		long time1 = dateNow.getTime() - ((hour * 60 + min) * 60 + sec) * 1000;
		long time2 = nextJob.getEventTime().getTime() * 60 * 1000;
		return time1 + time2;

	}
}