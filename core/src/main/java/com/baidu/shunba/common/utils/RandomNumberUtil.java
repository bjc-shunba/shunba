package com.baidu.shunba.common.utils;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RandomNumberUtil {
	public static final int DefaultSequenceLength = 18;
	private static volatile AtomicLong seed = new AtomicLong(0l);
	
	public static synchronized String generateUid(int length) {
		return generateNumberSequence(new Date(), length);
	}
	
	public static synchronized String generateNumberSequence(final Date date, final int length) {
		String defaultSeq = "999999999999999999";
		try {
			final long now = date.getTime();
			final int timeLen = String.valueOf(now).length();
			
			StringBuilder seq = new StringBuilder();
			
			if (now > seed.get()) {
				seed.set(now);
			} else {
				seed.incrementAndGet();
			}
			
			Random random = new Random(seed.get());
			final int rule = random.nextInt(9) + 1;
			
			long ruleAdd = 0;
			for (int i = 0; i < timeLen; i ++) {
				ruleAdd += rule * Math.pow(10, i);
			}
			final long addTime = now + ruleAdd;
			
			int timeStrLen = (timeLen + 1) % 100;
			seq.append(rule + String.format("%0" + timeStrLen +"d", addTime));
			
			int lenDif = length - seq.length();
			if (lenDif > 0) {
				long subSeed = seed.get() % ((long) Math.pow(10, lenDif));
				seq.append(String.format("%0" + lenDif + "d", subSeed));
			}
			
			if (seq.length() == length) {
				return seq.toString();
			} else if (length >= DefaultSequenceLength) {
				return seq.substring(0, length);
			} else {
				int start = random.nextInt(seq.length() - length);
				return seq.substring(start, start + length);
			}
		} catch (Exception exp) {
			return defaultSeq;
		}
	}
}
