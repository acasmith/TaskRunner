package task;

import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
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
	private ExecutorService threadPool;
	
	TaskRunner()
	{
		this.threadPool = Executors.newCachedThreadPool();
	}
	
  public <V> Future<V> runTaskAsync(ITask<V> task, int times, long sleepMillis, Class<V> targetClass) {
    CompletableFuture<V> completableFuture = new CompletableFuture<V>();
    this.threadPool.submit(() -> {
    	completableFuture.complete(task.call(targetClass));
    });
    return completableFuture;
  }
}
