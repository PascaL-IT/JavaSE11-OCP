package test;

import java.nio.file.Path;

import directory.CreateDirectories;

public class ThreadTest {

	public static void main(String[] args) {

		Path dirA = Path.of("C:\\Users\\User\\Workspace\\JavaProjects\\Oracle_practices\\practice14\\MultithreadingTests\\A");
		Path dirAA = Path.of(dirA.toString(), "\\A");
		Path dirAB = Path.of(dirA.toString(), "\\B");
		
		Thread[] threads = new Thread[] { 
				CreateDirectories.getThreadInstance(dirA, "taskA") , 
				CreateDirectories.getThreadInstance(dirAA, "taskAA") ,
				CreateDirectories.getThreadInstance(dirAB, "taskAB")
		};

		
		try {
			
			for (int i=0; i < threads.length; ++i) {
				// CreateDirectories.printThread(threads[i]);
				threads[i].start();
				CreateDirectories.printThread(threads[i], i);
			}
			
			for (Thread t : threads) {
				  t.join();
			}
			
			threads[threads.length - 1].join(1);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (int i=0; i < threads.length; ++i) {
			System.out.println("Program ends with " + threads[i].getName() + " in state " + threads[i].getState());
		}
		
	}
	

}
