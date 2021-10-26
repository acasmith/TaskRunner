package task;

import java.util.concurrent.Future;

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
  public <V> Future<V> runTaskAsync(ITask<V> task, int times, long sleepMillis) {
    return null;
  }
}
