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
   *  @param targetClass - Intended class for the return. Specifying this allows hard-casting the result inside the method. This enables us to fail fast in a controlled environment in the event of a type mismatch.
   */
  public T call(Class<T> targetClass) throws ClassCastException;
}

