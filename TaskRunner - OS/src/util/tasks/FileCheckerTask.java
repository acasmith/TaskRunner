package util.tasks;

import java.io.File;

import task.ITask;
import java.lang.Boolean;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 
 * Checks if the passed in file name exists
 *
 * @param <T>
 */
public class FileCheckerTask<T> implements ITask<T>{
  private File fileToCheck;
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
