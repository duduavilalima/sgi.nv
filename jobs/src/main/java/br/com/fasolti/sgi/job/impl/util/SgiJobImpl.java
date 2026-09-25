package br.com.fasolti.sgi.job.impl.util;

import br.com.fasolti.queue.commons.api.model.entities.JobExecutionInfo;
import br.com.fasolti.sgi.job.util.SgiJob;

public abstract class SgiJobImpl implements SgiJob {

	protected abstract void doProcess(JobExecutionInfo execution) throws Exception;

	@Override
	public void process(JobExecutionInfo execution) throws Exception {
		doProcess(execution);
	}
}
