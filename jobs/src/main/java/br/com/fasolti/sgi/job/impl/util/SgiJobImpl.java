package br.com.fasolti.sgi.job.impl.util;

import br.com.fasolti.sgi.job.util.JobExecutionInfo;
import br.com.fasolti.sgi.job.util.SgiJob;

public abstract class SgiJobImpl implements SgiJob {

	protected abstract void doProcess(JobExecutionInfo execution) throws Exception;

	@Override
	public void process(JobExecutionInfo execution) throws Exception {
		doProcess(execution);
	}
}
