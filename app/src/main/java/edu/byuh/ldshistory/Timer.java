package edu.byuh.ldshistory;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class Timer extends Handler {

	private List<TickListener> followers;
	private final int DELAY = 10;
	private volatile boolean paused;

	public Timer() {
		followers = new ArrayList<TickListener>();
		restart();
	}

	@Override
	public void handleMessage(Message msg) {
		//pause();
		synchronized(this) {
			if (!paused){
				//MainActivity.say("TIMER ON!");
				for (TickListener t : followers) {
					t.onTick();
				}
				//this.sendMessageDelayed(this.obtainMessage(0), DELAY);
				if (!paused) {
					//Main.say("about to call restart()");
					restart();
				} /*else {
					Main.say("whoops... paused. Gotta stop.");
				}*/
			}
		}
	}

	public void addFollower(TickListener t) {
		followers.add(t);
	}

	public void removeFollower(TickListener t) {
		followers.remove(t);
	}

	public void clearFollowers() {
		followers.clear();
	}

	public synchronized void pause() {
		paused = true;
		this.removeMessages(0);
		//Main.say("TIMER STOPPED");
	}

	public synchronized void restart() {
		paused = false;
		//Main.say("RESTARTING TIMER!!!");
		this.sendEmptyMessageDelayed(0, DELAY);
	}


}
