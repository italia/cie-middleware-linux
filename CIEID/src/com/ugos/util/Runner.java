package com.ugos.util;

import java.util.Timer;
import java.util.TimerTask;

public class Runner
{
    public static void run(Runnable run)
    {
        new Thread(run).start();
    }

    public static void run(Runnable run, int stackSize)
    {
		new Thread(new ThreadGroup("runnable"), run, "runnable", stackSize).start();
    }

    public static void runDelayed(final Runnable run, int delay)
    {
    	TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				run.run();
			}
		};

    	new Timer().schedule(tt, delay);
    }
}
