package task;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TaskRunnerTests {

	@Test
	public void GivenASingleTaskWhenTaskSucceedsFirstTimeThenTaskRunnerReturnsCorrectly() throws InterruptedException, ExecutionException {
		// Arrange
		TaskRunner taskRunner = new TaskRunner();
		ITask<Boolean> mockSuccessfulTask = (ITask<Boolean>) mock(ITask.class);
		when(mockSuccessfulTask.call(Boolean.class)).thenReturn(true);
		when(mockSuccessfulTask.isComplete()).thenReturn(true);
		
		
		// Act
		Future<Boolean> pendingResult = taskRunner.runTaskAsync(mockSuccessfulTask, 1, 0, Boolean.class);
		boolean result = pendingResult.get();
		
		// Assert
		assertTrue("Task should be successfully complete. Expected: true, actual: " + result, result);
	}

}
