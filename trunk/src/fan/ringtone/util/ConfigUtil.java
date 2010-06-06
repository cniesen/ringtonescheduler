package fan.ringtone.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.content.Context;
import android.widget.Toast;
import fan.ringtone.R;
import fan.ringtone.datastructure.GlobalSetting;
import fan.ringtone.util.ProgramConstants.NOTIFYTYPE;

public class ConfigUtil {

	public static GlobalSetting getGlobalSettings(Context v) {
		try {
			InputStream ins = v.openFileInput(ProgramConstants.settingData);
			ins.close();
		} catch (FileNotFoundException e1) {
			initializeGlobalSettings(v);
		} catch (IOException e) {
			Toast.makeText(v, R.string.errorReadGlobalSetting, 0).show();
		}
		try {
			InputStream ins = v.openFileInput(ProgramConstants.settingData);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					ins));
			String line = reader.readLine();
			boolean autoRun = true;
			Integer defaultAction = 0;
			boolean useNotification = true;

			if (line != null)
				autoRun = Boolean.parseBoolean(line);

			line = reader.readLine();
			if (line != null)
				defaultAction = Integer.parseInt(line);
			line = reader.readLine();
			if (line != null)
				useNotification = Boolean.parseBoolean(line);
			reader.close();
			return new GlobalSetting(autoRun, defaultAction, useNotification);
		} catch (Exception e) {
			Toast.makeText(v, R.string.errorReadGlobalSetting,
					Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	private static void initializeGlobalSettings(Context v) {

		try {
			OutputStream ous = v.openFileOutput(ProgramConstants.settingData,
					Context.MODE_WORLD_READABLE);
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(ous));
			pw.println(true);
			pw.println(ProgramConstants.notify2IntMap
					.get(ProgramConstants.NOTIFYTYPE.VIBRATE));
			pw.println(true);
			pw.close();
		} catch (Exception e) {
			Toast.makeText(v, R.string.errorInit, Toast.LENGTH_SHORT).show();
		}
	}

	public static void updateGlobalSettings(Context v, boolean autoRun,
			NOTIFYTYPE def, boolean useNotification) {
		try {
			OutputStream ous = v.openFileOutput(ProgramConstants.settingData,
					Context.MODE_WORLD_READABLE);
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(ous));
			pw.println(autoRun);
			pw.println(ProgramConstants.notify2IntMap.get(def));
			pw.println(useNotification);
			pw.close();
		} catch (Exception e) {
			Toast.makeText(v, R.string.errorInit, Toast.LENGTH_SHORT).show();
		}
	}

}
