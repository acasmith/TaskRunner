package task;

import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
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
	
	public TaskRunner(int threadPoolSize)
	{
		this.threadPool = Executors.newScheduledThreadPool(threadPoolSize);
	}
	
  public <V> Future<V> runTaskAsync(ITask<V> task, int times, long sleepMillis, Class<V> targetClass) throws InterruptedException, ExecutionException {
	if(times < 1 || times > 5)
	{
		throw new IllegalArgumentException("Illegal value given for 'times' argument. The value should be in the range 1-5 inclusive. Actual value: " + times);
	}
	else if(sleepMillis < 1 || sleepMillis > 5000)
	{
		throw new IllegalArgumentException("Illegal value given for 'sleepMillis' argument. The value should be in the range 1-5000 inclusive. Actual value: " + sleepMillis);
	}
	else if(task == null)
	{
		throw new NullPointerException("'Task' argument cannot be null.");
	}
	
    CompletableFuture<V> completableFuture = new CompletableFuture<V>();
    this.threadPool.schedule(() -> {
    	this.executeTask(task, times, sleepMillis, targetClass, completableFuture);
	}, 0, TimeUnit.MILLISECONDS);    
    return completableFuture;
  }
  
  /**
   * Helper method for handling task execution.
   * If the task finishes and is 'complete' then the result is returned as the task result.
   * If the task finishes but is not 'complete' and has attempts remaining then the task will be scheduled to run on a new thread to try and ensure fair access to resources for queued tasks.
   * If the task finishes but is not 'complete' and has no attempts remaining then the last result is returned as the task result.
   * If the task throws an exception and has attempts remaining then an attempt will be made to honour the timeout before running the task again on the same thread. This is to minimise complexity of ensuring the Future remains completable.
   * If the task throws an exception and has no attempts remaining then the exception is propagated as the result of the task.
   * If the task throws a ClassCastException then it is assumed to be programmer error when specifying task return type and the task completes early.
   * @param task
   * @param remainingAttempts
   * @param sleepMillis
   * @param targetClass
   * @param completableFuture
   */
  private <V> void executeTask(ITask<V> task, int remainingAttempts, long sleepMillis, Class<V> targetClass, CompletableFuture<V> completableFuture)
  {	
	try
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
	catch(Exception e)
	{		
		if(e.getClass() == ClassCastException.class || remainingAttempts <=1)
		{
			completableFuture.completeExceptionally(e);
		}		
		else
		{
			e.printStackTrace();
			try {
				Thread.sleep(sleepMillis);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			finally
			{
				this.executeTask(task, remainingAttempts - 1, sleepMillis, targetClass, completableFuture);
			}
		}	
	}	
  }
  
  public boolean Shutdown(long timeout, TimeUnit unit) throws InterruptedException
  {
	  return this.threadPool.awaitTermination(timeout, unit);
  }
}
