package br.com.fasolti.sgi.job.util;

public interface SgiJob {

	void process(JobExecutionInfo execution) throws Exception;
}
