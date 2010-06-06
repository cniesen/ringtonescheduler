package fan.ringtone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import fan.ringtone.datastructure.GlobalSetting;
import fan.ringtone.util.ConfigUtil;
import fan.ringtone.util.ProgramConstants;
import fan.ringtone.util.ResourceLoader;
import fan.ringtone.util.ProgramConstants.NOTIFYTYPE;

public class Configer extends Activity {

	private Button btnOK;
	private Button btnCancel;
	private GlobalSetting setting;
	private CheckBox chkAutoRun;
	private CheckBox chkUseNotify;
	private RadioButton silent;
	private RadioButton vibrate;
	private RadioButton ring;
	private RadioButton ringvibrate;
	private ImageButton about;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.config);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		btnCancel = (Button) this.findViewById(R.id.btnConfigCancel);
		btnOK = (Button) this.findViewById(R.id.btnConfigOK);
		chkAutoRun = (CheckBox) this.findViewById(R.id.chkboxAutoRun);
		chkUseNotify = (CheckBox) this.findViewById(R.id.useNotification);
		silent = (RadioButton) this.findViewById(R.id.radioDefaultSlient);
		vibrate = (RadioButton) this.findViewById(R.id.radioDefaultVibrate);
		ring = (RadioButton) this.findViewById(R.id.radioDefaultRing);
		ringvibrate = (RadioButton) this
				.findViewById(R.id.radioDefaultRingAndVibrate);
		about = (ImageButton) this.findViewById(R.id.disclaimer);
		setupCancelButton();
		setupOKButton();
		setupAboutButton();
	}

	@Override
	protected void onStart() {
		super.onStart();
		setting = ConfigUtil.getGlobalSettings(this);
		chkAutoRun.setChecked(setting.autoRun);
		chkUseNotify.setChecked(setting.useNotification);
		switch (setting.defaultRing) {
		case SILENT:
			this.silent.setChecked(true);
			break;
		case VIBRATE:
			this.vibrate.setChecked(true);
			break;
		case RING:
			this.ring.setChecked(true);
			break;
		default:
			this.ringvibrate.setChecked(true);
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		if (id == ProgramConstants.ABOUT_DIALOG) {
			return new AlertDialog.Builder(this).setTitle(
					ResourceLoader.getString(this, R.string.disclaimer))
					.setMessage(
							ResourceLoader.getString(this,
									R.string.aboutMessage)).create();
		}
		return null;
	}

	private void setupOKButton() {
		btnOK.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (silent.isChecked())
					updateGlobalSetting(ProgramConstants.NOTIFYTYPE.SILENT);
				else if (vibrate.isChecked())
					updateGlobalSetting(ProgramConstants.NOTIFYTYPE.VIBRATE);
				else if (ring.isChecked())
					updateGlobalSetting(ProgramConstants.NOTIFYTYPE.RING);
				else
					updateGlobalSetting(ProgramConstants.NOTIFYTYPE.RINGVIBRATE);
				finish();
			}
		});
	}

	private void setupAboutButton() {
		about.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showDialog(ProgramConstants.ABOUT_DIALOG);
			}
		});
	}

	private void updateGlobalSetting(NOTIFYTYPE def) {
		ConfigUtil.updateGlobalSettings(this, chkAutoRun.isChecked(), def,
				chkUseNotify.isChecked());
		BroadcastRequesterUtil.requestUpdate(this, false, 0);
	}

	private void setupCancelButton() {
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}
