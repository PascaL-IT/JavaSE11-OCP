package test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class VolatileAtomicLockTests extends Thread {
	
	// Static - shared (class context)
	public static volatile boolean lock = false; // WITH VOLATILE keyword (tasks are ending more grouped) 
	//public static boolean lock = false; // WITHOUT VOLATILE keyword 
	
	// public static volatile int counter = 0; // WITH VOLATILE keyword 
	public static int counter = 0; // WITHOUT VOLATILE keyword 
	
	public static AtomicInteger acounter = new AtomicInteger(0); // WITHOUT VOLATILE keyword
	
	private final ReentrantLock lockRL = new ReentrantLock();

    public void incrementWithReentrantLock() {
    	 lockRL.lock();  // block until condition holds
	     try {
	       ++counter;
	       Thread ct = Thread.currentThread();
	       System.out.println("incrementWithReentrantLock by " + ct.getName() + " - " + String.format("%05d", counter));
	     } finally {
	    	 lockRL.unlock();
	     }
	}
		
	public static boolean isNotLocked() {
		return ! lock;
	}
	
	public static synchronized void increment() {
		++counter;
		Thread ct = Thread.currentThread();
		System.out.println("synchronized void increment by " + ct.getName() + " - " + String.format("%05d", counter));
	}
	
	public static void locking() {
		VolatileAtomicLockTests.lock = true;
		Thread ct = Thread.currentThread();
		System.out.println(ct.getName() + " -> set lock=" + VolatileAtomicLockTests.lock);
	}	
	
	
	private int mycounter = 0;  

	public int getMycounter() {
		return mycounter;
	}

	// Constructor with Thread name
	public VolatileAtomicLockTests(String name) {
		super(name);
	}
		
	// Runnable - increment counter while it is NOT not locked
	@Override
	public void run() {
		
		System.out.println(this.getName() + " -> " + this.getState());
				
		while (isNotLocked()) {
			
			try {
				Thread.sleep(0);
				++mycounter; // internal counter 
				// System.out.println(this.getName() + " -> counter=" + String.format("%05d", counter)); // WITHOUT ++counter
				// System.out.println(this.getName() + " -> counter=" + String.format("%05d", ++counter)); // WITH ++counter => NOK, result < 0 always/msot of the time
				
				/* Test various incrementation of global shared counter */ 
				// VolatileAtomicLockTests.increment(); // synchronized increment function => counter OK, result=0 always  
				incrementWithReentrantLock(); // synchronized with Reentrant Lock => counter OK & NOK ???, result=0 NOT always ??? TODO
				System.out.println(this.getName() + " -> acounter=" + String.format("%05d", acounter.addAndGet(1))); // WITH AtomicInteger => acounter OK, result=0 always 
				
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			
		}
		
		System.out.println(this.getName() + " -> end with mycounter= " + mycounter);
	}

	
	/*
	 *  MAIN
	 */
	public static void main(String[] vargs) {
		
		Runtime rt = Runtime.getRuntime();
		int processors = rt.availableProcessors();
	    System.out.println("CPU cores: " + processors + " - Free memory: " + rt.freeMemory());
	    
		VolatileAtomicLockTests[] tasks = new VolatileAtomicLockTests[] {
	    		new VolatileAtomicLockTests("task1") ,
	    		new VolatileAtomicLockTests("task2") ,
	    		new VolatileAtomicLockTests("task3") ,
	    		new VolatileAtomicLockTests("task4") ,
	    		new VolatileAtomicLockTests("task5") 
	    };
		
		try {
			//start
			for (Thread t : tasks) {
				t.start();
			}
			
			// pause, let's running 
			Thread.sleep(1000); 
			
			// stop
			locking(); 

			// Waiting before computing result 
			for (Thread t : tasks) {
				  t.join();
			}
			
			System.out.println("counter="+counter);
			System.out.println("atomic counter="+acounter);

			int result = counter; 
			for (VolatileAtomicLockTests t : tasks) {
				result -= t.getMycounter();
			}
			
			
		
			System.out.println("result="+result+" => " + ((result == 0) ? "OK" : "NOK") ); 
			
			result = acounter.getAcquire(); // or .get()
			for (VolatileAtomicLockTests t : tasks) {
				result -= t.getMycounter();
			}
			System.out.println("atomic result="+result+" => " + ((result == 0) ? "OK" : "NOK") );	

			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
				
//		// Test Collections.synchronizedList
//		List<VolatileAtomicLockTests> list = Collections.synchronizedList(Arrays.asList(tasks));
//		list.forEach((vt) -> System.out.println(vt));
//
//		// Test CopyOnWriteArrayList
//		CopyOnWriteArrayList<Thread> list2 = new CopyOnWriteArrayList<>(Arrays.asList(tasks));
//		list2.forEach((t) -> System.out.println(t));
		
	}

}
