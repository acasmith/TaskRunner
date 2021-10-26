package client;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import task.TaskRunner;
import util.tasks.FileCheckerTask;
import util.tasks.PortAvailableTask;

/**
 * Main program for creating and submitting tasks to the Task Runner.
 */
public class TaskSubmitter {
  
  /**
   * Creates two tasks and submits them to the TaskRunner.
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception 
  {
	  TaskRunner taskRunner = new TaskRunner(2);
	  String fileName = "pom.xml";
	  // int portNumber = 8080;
	  
	  FileCheckerTask<Boolean> fileCheckerTask = new FileCheckerTask<Boolean>(fileName);
	  //PortAvailableTask<Boolean> portAvailableTask = new PortAvailableTask<Boolean>(8080);    
    
	  Future<Boolean> fileCheck = taskRunner.runTaskAsync(fileCheckerTask, 5, 1000, Boolean.class);
	  //Future<Boolean> portCheck = taskRunner.runTaskAsync(portAvailableTask, 5, 1000, Boolean.class);
	  
	  boolean fileResult = fileCheck.get();
	  //boolean portResult = portCheck.get();
	  
      /* Print the results */
	  System.out.println("File '" + fileName + "' exists: " + fileResult);
	  //System.out.println("Port " + portNumber + " is available: " + portResult);
	  
	  taskRunner.Shutdown(1000, TimeUnit.MILLISECONDS);
  }
}
