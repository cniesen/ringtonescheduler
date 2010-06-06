package fan.ringtone;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import fan.ringtone.datastructure.JobAdapter;
import fan.ringtone.datastructure.ScheduleBase;
import fan.ringtone.util.EditConstants;
import fan.ringtone.util.ProgramConstants;
import fan.ringtone.util.RuleUtil;

public class Main extends Activity {
	private Button closeButton;
	private Button addButton;
	private Button configButton;
	private ScheduleBase schedule = null;
	private ListView scheduleView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		schedule = ScheduleBase.getInstance();
		closeButton = (Button) this.findViewById(R.id.btnExit);
		addButton = (Button) this.findViewById(R.id.btnAddRule);
		configButton = (Button) this.findViewById(R.id.btnConfigTool);
		scheduleView = (ListView) this.findViewById(R.id.scheduleView);
		this.closeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		this.addButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				addOrEditRule(true, 0);
			}
		});
		this.configButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(IntentGroup.CONFIG);
				startActivity(i);
			}
		});
	}

	private void addOrEditRule(boolean add, long id) {
		Intent i;
		if (add) {
			i = new Intent(IntentGroup.ADDRULE);
			i.putExtra(EditConstants.INTENT, IntentGroup.ADDRULE);
			startActivity(i);
		} else {
			// edit rule id
			i = new Intent(IntentGroup.EDITRULE);
			i.putExtra(EditConstants.INTENT, IntentGroup.EDITRULE);
			i.putExtra(EditConstants.ITEMKEY, id);
			startActivity(i);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case ProgramConstants.DELRULEMENUITEM:
			RuleUtil.deleteSingleRule(this, schedule, info.id);
			updateScheduleView();
			break;
		case ProgramConstants.DELALLMENUITEM:
			RuleUtil.deleteAllRules(this, schedule);
			updateScheduleView();
			break;
		case ProgramConstants.EDITEVENTMENUITEM:
			addOrEditRule(false, info.id);
			break;
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (schedule.getJobCount() == 0
				|| (schedule.getJobCount() == 1 && !schedule.getJobs().get(0)
						.isValid())) {
			return;
		}
		menu.add(0, ProgramConstants.EDITEVENTMENUITEM, 0,
				R.string.editThisRule);
		menu.add(0, ProgramConstants.DELRULEMENUITEM, 1, R.string.deleteThis);
		menu.add(0, ProgramConstants.DELALLMENUITEM, 2, R.string.deleteAll);
	}

	private void updateScheduleView() {
		JobAdapter jobAdapter = RuleUtil.readScheduleList(this, schedule);
		scheduleView.setAdapter(jobAdapter);
		BroadcastRequesterUtil.requestUpdate(this, false, 0);
	}

	@Override
	protected void onStart() {
		super.onStart();
		updateScheduleView();
		registerForContextMenu(scheduleView);
	}

}