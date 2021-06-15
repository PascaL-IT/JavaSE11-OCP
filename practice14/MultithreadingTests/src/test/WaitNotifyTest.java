package test;

public class WaitNotifyTest {


	public static void main(String[] args) {

		Object lock = new Object();
		
		Runnable r = () -> { 
			Thread ct = Thread.currentThread();
			System.out.println(ct);
			try {
				synchronized(lock) {
					lock.wait(); // wait on lock released/notified
				}
				Thread.sleep(5000); // sleep 5 sec
				
			} catch (InterruptedException e) {
				System.out.println(ct + " -> " + e.getMessage());
			}
		};
		
		Thread t = new Thread(r,"t");
		// t.yield();
		Thread ct = Thread.currentThread();
		System.out.println(ct);
				
		try {
			// ct.setDaemon(true); // ct as Daemon => IllegalThreadStateException
			t.setDaemon(true); // t as Daemon => Ok, the JVM exists when all remaining threads are daemon
			t.start(); // start t
			System.out.println(t.getName() + " state is " + t.getState());
			Thread.sleep(1000); // sleep 1 sec (main)
			System.out.println(ct.getName() + " state is " + ct.getState());
			
		} catch (InterruptedException e) {
			System.out.println(ct + " -> " + e.getMessage());
		} 
		
		synchronized(lock) {
			lock.notify(); // wake up (unlock)
		}
		
//		while (t.isAlive() && ! t.isDaemon()) {
//			System.out.println(ct + " and " + t + " in state " + t.getState());
//		}
		
		System.out.println("End with " + ct + " and " + t + " in state " + t.getState());
	}

}
