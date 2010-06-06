package fan.ringtone;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import fan.ringtone.datastructure.DateNode;
import fan.ringtone.datastructure.GlobalSetting;
import fan.ringtone.datastructure.Job;
import fan.ringtone.datastructure.JobEvent;
import fan.ringtone.datastructure.ScheduleBase;
import fan.ringtone.util.ConfigUtil;
import fan.ringtone.util.ReceiverUtil;
import fan.ringtone.util.RuleUtil;
import fan.ringtone.util.ProgramConstants.NOTIFYTYPE;

public class RingBroadcastListener extends BroadcastReceiver {

	AlarmManager alarms = null;
	Calendar cal = Calendar.getInstance();
	ScheduleBase schedule;
	GlobalSetting setting;
	static long lastNotificationTime = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (IntentGroup.SYSTEM_BOOT.equals(intent.getAction())) {
			initializeAndUpdate(context);
		} else if (IntentGroup.UPDATE.equals(intent.getAction())) {
			initializeAndUpdate(context);
		} else if (IntentGroup.SETEVENT.equals(intent.getAction())) {
			updateSchedule(context);
			JobEvent nextJob = schedule.getNextEvent(context);
			if (nextJob != null)
				changeRingTheme(context, nextJob);
			GlobalSetting setting = ConfigUtil.getGlobalSettings(context);
			if (setting.autoRun == false)
				return;
			else
				scheduleNextCheck(context);
		}
	}

	private void initializeAndUpdate(Context context) {
		setting = ConfigUtil.getGlobalSettings(context);
		if (setting.autoRun == false)
			return;
		else {
			updateSchedule(context);
			scheduleNextCheck(context);
		}
	}

	private void updateSchedule(Context context) {
		schedule = ScheduleBase.getInstance();
		RuleUtil.readScheduleList(context, schedule);
	}

	private void changeRingTheme(Context c, JobEvent nextJob) {
		if (nextJob == null)
			return;
		AudioManager audio = (AudioManager) c
				.getSystemService(Context.AUDIO_SERVICE);
		if (ReceiverUtil.currentMode(c).equals(nextJob.getRingType()))
			return;
		if (setting == null)
			setting = ConfigUtil.getGlobalSettings(c);
		if (setting.useNotification) {
			notifyUser(c, nextJob.getRingType());
		}
		switch (nextJob.getRingType()) {
		case RING:
			ReceiverUtil.ring(audio);
			break;
		case SILENT:
			ReceiverUtil.silent(audio);
			break;
		case RINGVIBRATE:
			ReceiverUtil.ringAndVibrate(audio);
			break;
		case VIBRATE:
			ReceiverUtil.vibrate(audio);
			break;
		}
	}

	private void notifyUser(Context c, NOTIFYTYPE ringType) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) c
				.getSystemService(ns);
		int icon = R.drawable.icon;
		CharSequence tickerText = "Ringtone Scheduler";
		long when = System.currentTimeMillis();
		if (lastNotificationTime == 0)
			lastNotificationTime = when;
		if (when - lastNotificationTime < 1000) {
			lastNotificationTime = when;
			return;
		} else {
			lastNotificationTime = when;
		}
		CharSequence contentText = "";
		CharSequence contentTitle = "Ringtone Scheduler";
		switch (ringType) {
		case RING:
			contentText = "Ringtone Changed to Ring";
			break;
		case RINGVIBRATE:
			contentText = "Ringtone Changed to Ring/Vibrate";
			break;
		case SILENT:
			contentText = "Ringtone Changed to Silent";
			break;
		case VIBRATE:
			contentText = "Ringtone Changed to Vibrate";
			break;
		}
		Notification notification = new Notification(icon, tickerText, when);
		Intent notificationIntent = new Intent(c, RingBroadcastListener.class);
		PendingIntent contentIntent = PendingIntent.getActivity(c, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(c, contentTitle, contentText,
				contentIntent);
		int RINGTONE_ID = 1;
		mNotificationManager.notify(RINGTONE_ID, notification);
	}

	private void setRingToneToDefault(Context c) {
		if (setting == null)
			setting = ConfigUtil.getGlobalSettings(c);
		DateNode date = new DateNode(Calendar.getInstance().get(
				Calendar.HOUR_OF_DAY), Calendar.getInstance().get(
				Calendar.MINUTE));
		JobEvent job = new JobEvent(date, setting.defaultRing, null);
		changeRingTheme(c, job);
	}

	private void scheduleNextCheck(Context c) {
		JobEvent nextJob = schedule.getNextEvent(c);
		alarms = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
		if (nextJob == null) {
			setRingToneToDefault(c);
			BroadcastRequesterUtil.requestUpdate(c, true, 1);
		} else {
			Date now = new Date();
			Job j = nextJob.getAssociatedJob();
			// going to start a event
			if (j.getStartTime().getTime() == nextJob.getEventTime().getTime()) {
				// current profile != default ring tone
				if (setting != null
						&& !setting.defaultRing.equals(ReceiverUtil
								.currentMode(c))
						&& now.getTime() < BroadcastRequesterUtil
								.calculateNextJobWaitTime(nextJob)) {
					setRingToneToDefault(c);
					BroadcastRequesterUtil.requestUpdate(c, false, 0);
				} else {
					if (!ReceiverUtil.currentMode(c).equals(
							nextJob.getRingType())) {
						BroadcastRequesterUtil.sendChangeRingtoneRequest(c,
								nextJob);
					} else {
						BroadcastRequesterUtil.requestUpdate(c, true, 0.5);
					}

				}
			} else {// going to end a ringtone event
				// if current ring != interval ring
				if (!j.getRingType().equals(ReceiverUtil.currentMode(c))
						&& now.getTime() < BroadcastRequesterUtil
								.calculateNextJobWaitTime(nextJob)) {
					changeRingTheme(c, new JobEvent(nextJob.getEventTime(), j
							.getRingType(), j));
					BroadcastRequesterUtil.requestUpdate(c, false, 0);
				} else {
					{
						if (!ReceiverUtil.currentMode(c).equals(
								nextJob.getRingType())) {
							BroadcastRequesterUtil.sendChangeRingtoneRequest(c,
									nextJob);
						} else {
							BroadcastRequesterUtil.requestUpdate(c, true, 0.5);
						}
					}
				}
			}
		}
	}
}
