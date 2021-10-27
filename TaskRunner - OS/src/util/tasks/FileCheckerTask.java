package util.tasks;

import java.io.File;

import task.ITask;
import java.lang.Boolean;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * A task for checking the existence of a given filename on the local filesystem.
 * @param <T> - The return type of this tasks <code>call</code> implementation.
 */
public class FileCheckerTask<T> implements ITask<T>{
  private final File fileToCheck;
  private boolean isComplete = false;
  
  public FileCheckerTask(String fileName) throws NullPointerException {
    this.fileToCheck = new File(fileName);
  }
  
  @Override
  public boolean isComplete() {    
    return this.isComplete;
  }

  @Override
  public T call(Class<T> targetClass) throws ClassCastException {
	boolean fileExists = fileToCheck.exists();
	if(fileExists)
	{
		this.setIsComplete();
	}
    T result = targetClass.cast(fileExists);    
    return result;
  }
  
  private void setIsComplete()
  {
	  this.isComplete = true;
  }

}
