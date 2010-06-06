package fan.ringtone.util;

import android.content.Context;
import android.media.AudioManager;
import fan.ringtone.util.ProgramConstants.NOTIFYTYPE;

public class ReceiverUtil {
	public static NOTIFYTYPE currentMode(Context c) {
		AudioManager audio = (AudioManager) c
				.getSystemService(Context.AUDIO_SERVICE);

		switch (audio.getRingerMode()) {
		case AudioManager.RINGER_MODE_SILENT:
			return NOTIFYTYPE.SILENT;
		case AudioManager.RINGER_MODE_VIBRATE:
			return NOTIFYTYPE.VIBRATE;
		}
		if (audio.shouldVibrate(AudioManager.VIBRATE_TYPE_RINGER))
			return NOTIFYTYPE.RINGVIBRATE;
		return NOTIFYTYPE.RING;
	}

	public static void ringAndVibrate(AudioManager audio) {
		audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
				AudioManager.VIBRATE_SETTING_ON);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
				AudioManager.VIBRATE_SETTING_ON);
	}

	public static void ring(AudioManager audio) {
		audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
				AudioManager.VIBRATE_SETTING_OFF);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
				AudioManager.VIBRATE_SETTING_OFF);

	}

	public static void vibrate(AudioManager audio) {
		audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
				AudioManager.VIBRATE_SETTING_ON);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
				AudioManager.VIBRATE_SETTING_ON);

	}

	public static void silent(AudioManager audio) {
		audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
				AudioManager.VIBRATE_SETTING_OFF);
		audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
				AudioManager.VIBRATE_SETTING_OFF);

	}
}
