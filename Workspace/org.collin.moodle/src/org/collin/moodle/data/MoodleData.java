package org.collin.moodle.data;

public class MoodleData {
	
	private String module;
	
	private String activity;
	
	private long moduleId, activityId;
	
	private String uri;
	
	private long totalTime;

	public MoodleData(String module, long moduleId, String activity, long activityId, long totalTime ) {
		this( module, moduleId, activity, activityId, null, totalTime );
	}
	
	public MoodleData(String module, long moduleId, String activity, long activityId, String uri, long totalTime) {
		super();
		this.module = module;
		this.activity = activity;
		this.moduleId = moduleId;
		this.activityId = activityId;
		this.uri = uri;
		this.totalTime = totalTime;
	}

	public String getModule() {
		return module;
	}

	public String getActivity() {
		return activity;
	}

	public long getModuleId() {
		return moduleId;
	}

	public long getActivityId() {
		return activityId;
	}

	public String getUri() {
		return uri;
	}

	public long getTotalTime() {
		return totalTime;
	}
}
