package com.aexp.gcs.performance;

public class Statistics {
	private long count, max, min;
	private double avg;

	public Statistics() {
		avg = count = max = 0;
		min = Long.MAX_VALUE;
	}

	public synchronized String add(long duration) {
		avg = (avg * count + duration) / ++count;
		if (max < duration) {
			max = duration;
		}
		if (min > duration) {
			min=duration;
		}
		return toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Current stats: Avg=%f, Min=%d, Max=%d, Cnt=%d", avg, min, max, count);
	}

}
