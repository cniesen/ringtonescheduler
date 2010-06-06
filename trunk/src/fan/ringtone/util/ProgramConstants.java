package fan.ringtone.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ProgramConstants {
	public static final int DELRULEMENUITEM = 0;
	public static final int DELALLMENUITEM = 1;
	public static final int EDITEVENTMENUITEM = 2;
	public static final int ABOUT_DIALOG = 1;

	public static enum FREQUENCY {
		EVERYDAY, WEEKDAY, WEEKEND, INDIVIDUALDAY;
	}

	public static enum NOTIFYTYPE {
		SILENT, VIBRATE, RING, RINGVIBRATE;
	}

	public static Map<FREQUENCY, Integer> freq2IntMap;
	public static Map<Integer, FREQUENCY> Int2FreqMap;
	static {
		freq2IntMap = new HashMap<FREQUENCY, Integer>();
		freq2IntMap.put(FREQUENCY.EVERYDAY, 0);
		freq2IntMap.put(FREQUENCY.WEEKDAY, 1);
		freq2IntMap.put(FREQUENCY.WEEKEND, 2);
		freq2IntMap.put(FREQUENCY.INDIVIDUALDAY, 3);
		Int2FreqMap = new HashMap<Integer, FREQUENCY>();
		for (Entry<FREQUENCY, Integer> e : freq2IntMap.entrySet()) {
			Int2FreqMap.put(e.getValue(), e.getKey());
		}

	}

	public static Map<NOTIFYTYPE, Integer> notify2IntMap;
	public static Map<Integer, NOTIFYTYPE> Int2notifyMap;
	static {
		notify2IntMap = new HashMap<NOTIFYTYPE, Integer>();
		Int2notifyMap = new HashMap<Integer, NOTIFYTYPE>();
		notify2IntMap.put(NOTIFYTYPE.SILENT, 0);
		notify2IntMap.put(NOTIFYTYPE.VIBRATE, 1);
		notify2IntMap.put(NOTIFYTYPE.RING, 2);
		notify2IntMap.put(NOTIFYTYPE.RINGVIBRATE, 3);
		for (Entry<NOTIFYTYPE, Integer> e : notify2IntMap.entrySet()) {
			Int2notifyMap.put(e.getValue(), e.getKey());
		}
	}

	public static final String scheduleData = "scheduleData.conf";
	public static final String settingData = "rts.conf";
}
