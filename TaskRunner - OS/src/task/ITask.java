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
   *  Added a target class so we can cast the result inside the call method and fail fast.
   */
  public T call(Class<T> targetClass) throws ClassCastException;
}

