package fan.ringtone.util;

import android.content.Context;

public class ResourceLoader {
	public static String getString(Context c, int rid) {
		return c.getResources().getString(rid);
	}
}
