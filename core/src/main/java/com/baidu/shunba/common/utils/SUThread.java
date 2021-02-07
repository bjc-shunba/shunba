package com.baidu.shunba.common.utils;

public class SUThread {
	
	public static interface SUThreadProtocol {
		public void prepare();
		public boolean process() throws Exception;
		public void finish();
		public void onGetError(String error);
		public void onFinally();
		
	}
	
	private volatile boolean threadReleased = true;
	private volatile boolean threadExitFlag = false;
	private volatile boolean threadIsPause = false;
	
	public static SUThread newThread(SUThreadProtocol callbck) {
		return new SUThread(callbck);
	}
	
	private final SUThreadProtocol callback;
	private FWThreadImplement thread;
	
	public SUThread(SUThreadProtocol callbck) {
		this.callback = callbck;
	}
	
	public void start() {
		try {
			if (thread != null) {
				try {
					thread.interrupt();
				} catch (Exception e) {
				}
				thread = null;
			}
			
			thread = new FWThreadImplement();
			this.threadReleased = false;
			this.threadExitFlag = false;
			this.threadIsPause = false;
			thread.start();
		} catch (Exception e) {
			callback.onGetError(e.getLocalizedMessage());
		}
	}
	
	public void pause() {
		if (threadReleased) {
			return;
		}
		threadIsPause = true;
	}
	
	public void resume() {
		if (threadReleased) {
			return;
		}
		threadIsPause = false;
	}
	
	public void stop() {
		if (threadReleased) {
			return;
		}
		threadExitFlag = true;
		threadReleased = true;
		try {
			thread.interrupt();
		} catch (Exception e) {
			callback.onGetError(e.getLocalizedMessage());
		} finally {
			thread = null;
		}
	}
	
	public boolean isStopped() {
		return threadReleased;
	}
	
	private Thread.UncaughtExceptionHandler exceptionHandler = new Thread.UncaughtExceptionHandler(){
		public void uncaughtException(Thread t, Throwable e) {
			callback.onGetError(e.getLocalizedMessage());
		}
	};
	
	private class FWThreadImplement extends Thread {
		
		public void run() {
			Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
			try {
				callback.prepare();
				
				boolean shouldProcess = true;
				while (!threadExitFlag && shouldProcess) {
					shouldProcess = callback.process();
				}
				
				callback.finish();
			} catch (Exception e) {
				if (e.getLocalizedMessage() == null) {
					callback.onGetError(e.toString());
				} else {
					callback.onGetError(e.getLocalizedMessage());
				}
				
			} finally {
				try {
					callback.onFinally();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
