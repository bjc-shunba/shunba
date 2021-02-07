package com.baidu.shunba.common.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

public class SUSequenceUtils {
	public static interface SUSequenceSeedProvier {
		public void checkBeforeUse();
		public long getSeed();
	}
	
	
	protected static SUSequenceSeedProvier currentProvier = new SUSequenceSeedProvier() {
		private final AtomicLong seed = new AtomicLong(0l);
		
		@Override
		public void checkBeforeUse() {
			long now = System.currentTimeMillis();
			if (now > seed.get()) {
				seed.set(now);
			} else {
				seed.incrementAndGet();
			}
		}
		
		@Override
		public long getSeed() {
			return seed.get();
		}
	};
	
//	protected static SUSequenceSeedProvier currentProvier = defaultProvier;
	
	protected static final int NormalNumberCharSequenceLength = 12;
	protected static final int NormalTimeLength = 13;
	public static final int DefaultSequenceLength = 18;
	
	
	public static SUSequenceSeedProvier getProvier() {
		return currentProvier;
	}

	public static void setProvier(SUSequenceSeedProvier provier) {
		if (provier != null) {
			currentProvier = provier;
		}
	}

	public static synchronized String generateNumberSequenceWithoutEncrypt(final Date date) {
		return generateNumberSequenceWithoutEncrypt(date, DefaultSequenceLength);
	}
	
	public static synchronized String generateNumberSequenceWithoutEncrypt(final Date date, final int length) {
		String defaultSeq = "999999999999999999";
		try {
			final long now = date.getTime();
			StringBuilder seq = new StringBuilder();
			
			getProvier().checkBeforeUse();
			
			seq.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(now));
		
			int lenDif = length - seq.length();
			if (lenDif > 0) {
				long subSeed = getProvier().getSeed() % ((long) Math.pow(10, lenDif));
				seq.append(String.format("%0" + lenDif + "d", subSeed));
			}
			
			//System.out.println("rule("+rule+"), len("+String.format("%03d", timeStrLen)+"),orgTime("+ now +"),ruleAdd("+ruleAdd+"),addTime("+addTime+"),addedTime("+String.format("%0" + timeStrLen +"d", addTime)+"):" + seq);
			//}
			if (seq.length() == length) {
				return seq.toString();
			} else if (length >= DefaultSequenceLength) {
				return seq.substring(0, length);
			} else {
				Random random = new Random(getProvier().getSeed());
				int start = random.nextInt(seq.length() - length);
				//System.out.println("start:" + start);
				return seq.substring(start, start + length);
			}
		} catch (Exception exp) {
			return defaultSeq;
		}
	}
	
	
	public static synchronized String generateNumberSequence(final Date date, final int length) {
		String defaultSeq = "999999999999999999";
		try {
			final long now = date.getTime();
			final int timeLen = String.valueOf(now).length();
			
			StringBuilder seq = new StringBuilder();
			
			getProvier().checkBeforeUse();
			
			Random random = new Random(getProvier().getSeed());
			final int rule = random.nextInt(9) + 1;
			
			long ruleAdd = 0;
			for (int i = 0; i < timeLen; i ++) {
				ruleAdd += rule * Math.pow(10, i);
			}
			final long addTime = now + ruleAdd;
			
			int timeStrLen = (timeLen + 1) % 100;
			seq.append(rule + String.format("%0" + timeStrLen +"d", addTime));
			
//				timeStrLen %= 10;
//				String lenStr = String.format("%02d", timeLen + 1);
//				seq.insert(rule + 1, lenStr);
		
			int lenDif = length - seq.length();
			if (lenDif > 0) {
				long subSeed = getProvier().getSeed() % ((long) Math.pow(10, lenDif));
				seq.append(String.format("%0" + lenDif + "d", subSeed));
			}
			
			//System.out.println("rule("+rule+"), len("+String.format("%03d", timeStrLen)+"),orgTime("+ now +"),ruleAdd("+ruleAdd+"),addTime("+addTime+"),addedTime("+String.format("%0" + timeStrLen +"d", addTime)+"):" + seq);
			//}
			if (seq.length() == length) {
				return seq.toString();
			} else if (length >= DefaultSequenceLength) {
				return seq.substring(0, length);
			} else {
				int start = random.nextInt(seq.length() - length);
				//System.out.println("start:" + start);
				return seq.substring(start, start + length);
			}
		} catch (Exception exp) {
			return defaultSeq;
		}
	}
	
	
	
	public static synchronized String generateNumberSequence() {
		return generateNumberSequence(new Date(), DefaultSequenceLength);
	}
	
	public static synchronized String generateNumberSequence(Date date) {
		return generateNumberSequence(date, DefaultSequenceLength);
	}
	
	public static synchronized boolean verifyNumberSequence(String seq, Date date) {
		try {
			long time = date.getTime();
			int timeLen = (time + "").length();
			int rule = Integer.valueOf(seq.substring(0, 1));
			long seqRule = Long.valueOf(seq.substring(1, timeLen + 2));
			
			long ruleAdd = 0;
			for (int i = 0; i < timeLen; i ++) {
				ruleAdd += rule * Math.pow(10, i);
			}
			long realTime = seqRule - ruleAdd;
			
			return realTime == time;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static synchronized Date getTimeFromSequence(String seq) {
		try {
			final int rule = Integer.valueOf(seq.substring(0, 1));
			final long seqRule = Long.valueOf(seq.substring(1, NormalTimeLength + 1 + 1));
			
			long ruleAdd = 0;
			for (int i = 0; i < NormalTimeLength; i ++) {
				ruleAdd += rule * Math.pow(10, i);
			}
			long realTime = seqRule - ruleAdd;
			//System.out.println("rule("+rule+"),ruleAdd("+ruleAdd+"),seqRule("+seqRule+"):"+realTime);
			
			return new Date(realTime);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static synchronized String generateUid(int length) {
		return generateNumberSequence(new Date(), length);
	}
	
	
	
	
	
	//mixseq
	public static String generateNumberCharSequence() {
		return generateNumberCharSequence(NormalNumberCharSequenceLength);
	}
	
	public static String generateNumberCharSequence(final int length) {
		String noSeq = generateNumberSequence();
		StringBuilder seq = new StringBuilder(Long.toString(Long.valueOf(noSeq), Character.MAX_RADIX));
		if (seq.length() > length) {
			return seq.substring(0, length);
		} else if (seq.length() < length) {
			seq.append(generateRandomNumberChar(length - seq.length()));
		}
		return seq.toString();
	}
	
	public static synchronized Date getTimeFromeNumberCharSequence(final String seq) {
		try {
			String timeCharSeq;
			if (seq.length() > NormalNumberCharSequenceLength) {
				timeCharSeq = seq.substring(0, NormalNumberCharSequenceLength);
			} else {
				timeCharSeq = seq;
			}
			String timeSeq = Long.valueOf(timeCharSeq, Character.MAX_RADIX).toString();
			Date seqDate = getTimeFromSequence(timeSeq);
			if (seqDate.getTime() < System.currentTimeMillis()) {
				return seqDate;
			} else {
				timeCharSeq = seq.substring(0, NormalNumberCharSequenceLength - 1);
				timeSeq = Long.valueOf(timeCharSeq, Character.MAX_RADIX).toString();
				return getTimeFromSequence(timeSeq);
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	protected static String generateRandomNumberChar(final int length) {
		StringBuilder str = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			boolean b = random.nextBoolean();
			if (b) { // 字符串
				// int choice = random.nextBoolean() ? 65 : 97; 取得65大写字母还是97小写字母
				str.append((char) (97 + random.nextInt(26)));// 取得小写字母
			} else { // 数字
				str.append(random.nextInt(10));
			}
		}
		return str.toString().toUpperCase();
	}
	
	
	public static void mainTest(String[] args) throws Exception {
		
		Set<String> keys = new HashSet<>();
		
		for (int i = 0; i < 100000; i++) {
			
			String seq = generateNumberSequence(new Date());
			if (keys.contains(seq)) {
				System.err.println("xxxxxxxxx");
				break;
			}
		}
		
		System.out.println("finish");
		
		if (true) {
			return;
		}
		
		for (int i = 0; i < 100; i++) {
			String seq = generateNumberCharSequence(12);
			Date date = getTimeFromeNumberCharSequence(seq);
			System.out.println(seq);
			System.out.println(DateUtils.getDateStringWithFormat(date, "yyyy-MM-dd HH:mm:ss SSS"));
			System.out.println("         ");
		}
		
		if (true) {
			return;
		}
		
//		System.out.println(new SimpleDateFormat("YYMMddHHmmssSSS").format(new Date()));
//		System.out.println(new SimpleDateFormat("YYYYMMddHHmmssSSS").parse("00160603154556736").getTime());
//		System.out.println(System.nanoTime());
//		System.out.println(System.currentTimeMillis());
//		System.out.println(new Date().getTime());
//		System.out.println((System.currentTimeMillis() + "").length());
//		
		
//		System.err.println(generateUid(18));
//		System.err.println(generateUid(18));
//		System.err.println(generateUid(18));
//		if (true) {
//			return;
//		}
		
		//test repeate
//		Set<String> test = new HashSet<String>();
//		for (int i = 0; i < 200000; i++) {
//			String id = generateUid(18);
////			System.out.println(id);
////			System.out.println(getTimeFromeSequence(id));
//			if (test.contains(id)) {
//				System.err.println("xxxxxxxxxx(" + i + ")：" + id);
//				break;
//			}
//			test.add(id);
//		}
//		System.out.println("over");
//		
//		System.out.println(generateNumberSequence());
//		
//		Date now = new Date();
//		String seq = generateNumberSequence(now);
//		boolean isok = verifyNumberSequence(seq, now);
//		
//		System.out.println("(" + seq +")" + isok);
		
		
		//test sync
		final int TestSize = 100;
		final Vector<Vector<String>> testRes = new Vector<>();
		final Vector<Boolean> hasChecked = new Vector<>();
		
		for (int i = 0; i < TestSize; i++) {
			new Thread() {
				public void run() {
					Vector<String> res = new Vector<>();
					testRes.add(res);
					
					for (int j = 0; j < 1000; j ++) {
						res.add(generateNumberSequence());
					}
					
					hasChecked.add(true);
				}
			}.start();
		}
		
		new Thread() {
			public void run() {
				while (hasChecked.size() < TestSize) {
					try {
						sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("xxxxxxxxxxx");
				}
				
				List<String> allRes = new ArrayList<>();
				List<String> errRes = new ArrayList<>();
				for (List<String> aRes : testRes) {
					for (String seq : aRes) {
						if (!allRes.contains(seq)) {
							allRes.add(seq);
						} else {
							errRes.add(seq);
						}
					}
				}
				System.err.println("error res (" + errRes.size() + "):");
			}
		}.start();
	}
}
