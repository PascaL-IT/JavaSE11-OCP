package directory;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

/* Test creation of directories by several concurrent threads */

public class CreateDirectories {

	/* MAIN */
	public static void main(String[] args) {

		Path dir = Path.of("C:\\Users\\User\\Workspace\\JavaProjects\\Oracle_practices\\practice14\\MultithreadingTests\\A");
		
		Thread task1 = getThreadInstance(dir, "task1");

		task1.start();
		
		CreateDirectories.printThread(task1);

		if (task1.isAlive()) {

//			try {
				
				task1.interrupt();
				
				//task1.wait();
				//task1.join();

//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

		}

		System.out.println("End program with " + task1.getName() + " in state " + task1.getState());

	}

	/**
	 * print Thread 
	 * @param t
	 * @param i (optional)
	 */
	public static void printThread(Thread t, int... i) {
		if (i.length == 0) {
			i = new int[] { 0 };
		}
		System.out.println("["+i[0]+"]: "+ t.getName() + " is in state=" + t.getState() + " - id=" + t.getId() + " - priority="
				+ t.getPriority() + " - " + t.toString());

	}

	/**
	 * Builder of Thread to create directory
	 * 
	 * @param dirPath
	 * @param taskName
	 * @return Thread
	 */
	public static Thread getThreadInstance(Path dirPath, String taskName) {
		return new Thread(new CreateDirectories.CreateDirRunnable(dirPath, taskName), taskName);
	}
	
	

	/*
	 * CreateDirRunnable - public inner static 
	 */
	public static class CreateDirRunnable implements Runnable {

		private volatile Path directory;
		private volatile String taskName;

		// Constructor
		public CreateDirRunnable(Path dirPath, String taskName) {
			this.directory = dirPath;
			this.taskName = taskName;
		}
		
		// Get Task Name
		private String getTaskName() {
			return this.taskName;
		}

		@Override
		public void run() {

			Thread ct = Thread.currentThread();

			while (! ct.isInterrupted()) { // catch interrupt from inside

				try {
					Thread.sleep(1000);
					
				} catch (InterruptedException ie) {
					System.out.println("Interrupted exception catched by " + ct.getName() + " from outside -> " + ie.getMessage());
					ct.interrupt(); // launch an interrupt, but continue to next try block
				}
				
				try {
					// Files.createDirectories(dirBak);
					Files.createDirectory(directory);

					if (Files.exists(directory)) {
						System.out.println(directory + " has been created by " + ct.getName());
						break; // exit while... loop
					}

				} catch (FileAlreadyExistsException fex) {
					System.out.println(directory + " already exists (not created by " + ct.getName() + ")");
					// break; // exit while... loop, alternative to ct.interrupt() but not equivalent !

				} catch (IOException ioe) {
					System.err.println("ERROR - " + ct.getName() + " : exception: " + ioe);
				}

			}

			System.out.println("Task: " + getTaskName() + " ended"); // with state=" + ct.getState()); // always RUNNABLE
		}

	}

}
