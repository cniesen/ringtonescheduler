package fan.ringtone.datastructure;

import fan.ringtone.util.ProgramConstants.NOTIFYTYPE;

public class JobEvent {

	private DateNode eventTime;
	private NOTIFYTYPE ringType;
	private Job associatedJob;

	public JobEvent(DateNode time, NOTIFYTYPE rType, Job j) {
		eventTime = time;
		ringType = rType;
		setAssociatedJob(j);
	}

	public void setEventTime(DateNode eventTime) {
		this.eventTime = eventTime;
	}

	public DateNode getEventTime() {
		return eventTime;
	}

	public void setRingType(NOTIFYTYPE ringType) {
		this.ringType = ringType;
	}

	public NOTIFYTYPE getRingType() {
		return ringType;
	}

	public String toString() {
		return eventTime.toString() + " " + ringType.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof JobEvent))
			return false;
		JobEvent test = (JobEvent) o;
		return (this.ringType.equals(test.getRingType()) && this.eventTime
				.getTime() == test.getEventTime().getTime());

	}

	public void setAssociatedJob(Job associatedJob) {
		this.associatedJob = associatedJob;
	}

	public Job getAssociatedJob() {
		return associatedJob;
	}

}
