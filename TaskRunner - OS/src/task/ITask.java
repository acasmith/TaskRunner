package task;

/**
 * Represents a result bearing task
 *
 * @param <T> 
 */
public interface ITask<T> {
  /**
   * A task is complete if its objective 
   * has been met through an invocation
   * of the 'call' method.
   * 
   */
  public boolean isComplete();
  
  /**
   * Does the actual work and returns 
   * a result.
   *  
   */
  public T call();
}

