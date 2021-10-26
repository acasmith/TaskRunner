package task;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
   * Runs a submitted task <code>times</code> number of times
   * and supports a sleep interval of <code>sleepMillis</code> between
   * each run and returns a Future result.
   * 
   * This method returns immediately with a Future object 
   * which can be used to obtain the result of the task 
   * when the task completes. 
   * 
   * @param task
   * @param times
   * @param sleepMillis
 */

public class TaskRunner {
	private ScheduledExecutorService threadPool;
	
	TaskRunner(int threadPoolSize)
	{
		this.threadPool = Executors.newScheduledThreadPool(threadPoolSize);
	}
	
  public <V> Future<V> runTaskAsync(ITask<V> task, int times, long sleepMillis, Class<V> targetClass) throws InterruptedException, ExecutionException {
    CompletableFuture<V> completableFuture = new CompletableFuture<V>();
    this.threadPool.schedule(() -> {
    	this.executeTask(task, times, sleepMillis, targetClass, completableFuture);
	}, 0, TimeUnit.MILLISECONDS);    
    return completableFuture;
  }
  
  private <V> void executeTask(ITask<V> task, int remainingAttempts, long sleepMillis, Class<V> targetClass, CompletableFuture<V> completableFuture)
  {	  
	V result = task.call(targetClass);
	if(task.isComplete() || remainingAttempts <= 1)
	{	    		
		completableFuture.complete(result);
	}
	else
	{	
		this.threadPool.schedule(() -> {
			this.executeTask(task, remainingAttempts - 1, sleepMillis, targetClass, completableFuture);
	    }, sleepMillis, TimeUnit.MILLISECONDS);		
	}
  }
  
  public boolean Shutdown(long timeout, TimeUnit unit) throws InterruptedException
  {
	  return this.threadPool.awaitTermination(timeout, unit);
  }
}
