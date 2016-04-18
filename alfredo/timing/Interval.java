package alfredo.timing;

/**
 * An Interval provides methods for managing timing and game loops.
 * @author TheMonsterFromTheDeep
 */
public abstract class Interval {
    
    private class IntervalThread implements Runnable {
        volatile long delay;
        volatile long stretch; //Milliseconds to add to the loop in order to delay it longer than normal.
        volatile boolean running;
        
        volatile long time;
        
        public IntervalThread(long delay) {
            this.delay = delay;
        }

        @Override
        public final void run() {
            running = true;
            
            while(running) {
                time = System.currentTimeMillis();
                loop();
                time = System.currentTimeMillis() - time; //Get difference in time when calling loop
                if(time <= delay) {
                    try {
                        Thread.sleep(stretch + delay - time);
                    }
                    catch (InterruptedException ex) { }
                }
            }
        }
    }
    
    private final IntervalThread looper;
    
    /**
     * Construct a new Interval with the specified delay, in milliseconds.
     * 
     * This Interval will not do anything until start() is called.
     * @param delay The delay between each loop, in milliseconds.
     */
    public Interval(long delay) {
        looper = new IntervalThread(delay);
    }
    
    /**
     * Starts the Interval looping.
     */
    public final void start() {
        looper.run();
    }
    
    /**
     * Makes the Interval stop looping.
     */
    public final void stop() {
        looper.running = false;
    }
    
    /**
     * Sets the delay of the Interval to the specified delay in milliseconds.
     * @param milliseconds The new delay of the Interval, in milliseconds. (Must be positive)
     */
    public final void setDelay(long milliseconds) {
        if(milliseconds < 0) { throw new IllegalArgumentException("[Interval.setDelay] Delay must be greater than or equal to zero"); }
        else {
            looper.delay = milliseconds;
        }
    }
    
    /**
     * Causes the Interval to wait the specified number longer during its next loop.
     * @param milliseconds The number of milliseconds longer to wait. (Must be positive)
     */
    public final void delay(long milliseconds) {
        if(milliseconds < 0) { throw new IllegalArgumentException("[Interval.delay] Delay added must be greater than or equal to zero"); }
        else {
            looper.stretch = milliseconds;
        }
    }
    
    /**
     * The loop method that is called by the Interval. This is overridden by a subclass to run arbitrary code
     * in a loop with a millisecond delay.
     */
    public abstract void loop();
}