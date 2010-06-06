package fan.ringtone.datastructure;

import fan.ringtone.util.ProgramConstants;
import fan.ringtone.util.ProgramConstants.NOTIFYTYPE;

public class GlobalSetting {

	public boolean autoRun = false;
	public NOTIFYTYPE defaultRing;
	public boolean useNotification = false;

	public GlobalSetting(boolean auto, int def, boolean useNotification) {
		this.autoRun = auto;
		this.defaultRing = ProgramConstants.Int2notifyMap.get(def);
		this.useNotification = useNotification;
	}

}
