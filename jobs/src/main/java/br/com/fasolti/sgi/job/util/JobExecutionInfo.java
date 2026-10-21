package br.com.fasolti.sgi.job.util;

import java.time.LocalDateTime;

public class JobExecutionInfo {

	private final String jobName;
	private final LocalDateTime startedAt;

	public JobExecutionInfo(String jobName, LocalDateTime startedAt) {
		this.jobName = jobName;
		this.startedAt = startedAt;
	}

	public String getJobName() {
		return jobName;
	}

	public LocalDateTime getStartedAt() {
		return startedAt;
	}

	@Override
	public String toString() {
		return jobName + " @ " + startedAt;
	}
}
