package util.tasks;

import java.io.IOException;
import java.net.ServerSocket;

import task.ITask;

/**
 * A task for checking the availability of a given port on the local machine.
 * @param <T> - The return type of this tasks <code>call</code> implementation.
 */
public class PortAvailableTask<T> implements ITask<T>{
	
	private final int targetPort;
	private boolean isComplete = false;

  public PortAvailableTask(int targetPort) {
	  if(targetPort < 1 || targetPort > 65535)
	  {
		  throw new IllegalArgumentException("Port number out of range. Port number must be in range 1 - 65535 inclusive. Provided value: " + targetPort);
	  }
	  this.targetPort = targetPort;
	}

@Override
  public boolean isComplete() {
    return this.isComplete;
  }

  @Override
  public T call(Class<T> targetClass) throws ClassCastException {
    try {
		ServerSocket serverSocket = new ServerSocket(this.targetPort);
		serverSocket.close();
		this.setIsComplete();
		T result = targetClass.cast(true);
		return result;
	} catch (IOException e) {
		e.printStackTrace();
		T result = targetClass.cast(false);
		return result;
	}
  }
  
  private void setIsComplete()
  {
	  this.isComplete = true;
  }

}
