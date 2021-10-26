package task;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TaskRunnerTests {

	@Test
	public void GivenASingleTaskWhenTaskSucceedsFirstTimeThenTaskRunnerReturnsCorrectly() throws InterruptedException, ExecutionException {
		// Arrange
		TaskRunner taskRunner = new TaskRunner(1);
		ITask<Boolean> mockSuccessfulTask = (ITask<Boolean>) mock(ITask.class);
		when(mockSuccessfulTask.call(Boolean.class)).thenReturn(true);
		when(mockSuccessfulTask.isComplete()).thenReturn(true);
		
		
		// Act
		Future<Boolean> pendingResult = taskRunner.runTaskAsync(mockSuccessfulTask, 1, 1, Boolean.class);
		boolean result = pendingResult.get();
		
		// Assert
		assertTrue("Task should return true on successful completion. Expected: true, actual: " + result, result);
	}
	
	@Test
	public void GivenASingleTaskWhenTaskSucceedsSecondTimeThenTaskRunnerReturnsCorrectly() throws InterruptedException, ExecutionException {
		// Arrange
		TaskRunner taskRunner = new TaskRunner(1);
		ITask<Boolean> mockSuccessfulTask = (ITask<Boolean>) mock(ITask.class);
		when(mockSuccessfulTask.call(Boolean.class)).thenReturn(false, true);
		when(mockSuccessfulTask.isComplete()).thenReturn(false, true);
		
		
		// Act
		Future<Boolean> pendingResult = taskRunner.runTaskAsync(mockSuccessfulTask, 2, 1, Boolean.class);
		boolean result = pendingResult.get();
		
		// Assert
		assertTrue("Task should return true on successful completion. Expected: true, actual: " + result, result);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void GivenASingleTaskWithAnOutOfBoundsRetryCountWhenTaskSubmittedThenExceptionThrown() throws InterruptedException, ExecutionException
	{
		// Arrange
		TaskRunner taskRunner = new TaskRunner(1);
		ITask<Boolean> mockSuccessfulTask = (ITask<Boolean>) mock(ITask.class);
		when(mockSuccessfulTask.call(Boolean.class)).thenReturn(true);
		when(mockSuccessfulTask.isComplete()).thenReturn(true);
		
		
		// Act
		Future<Boolean> pendingResult = taskRunner.runTaskAsync(mockSuccessfulTask, 0, 1, Boolean.class);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void GivenASingleTaskWithAnOutOfBoundsSleepDurationWhenTaskSubmittedThenExceptionThrown() throws InterruptedException, ExecutionException
	{
		// Arrange
		TaskRunner taskRunner = new TaskRunner(1);
		ITask<Boolean> mockSuccessfulTask = (ITask<Boolean>) mock(ITask.class);
		when(mockSuccessfulTask.call(Boolean.class)).thenReturn(true);
		when(mockSuccessfulTask.isComplete()).thenReturn(true);
		
		
		// Act
		Future<Boolean> pendingResult = taskRunner.runTaskAsync(mockSuccessfulTask, 1, 0, Boolean.class);
	}
	
	@Test(expected = NullPointerException.class)
	public void GivenNoTasknWhenTaskSubmittedThenExceptionThrown() throws InterruptedException, ExecutionException
	{
		// Arrange
		TaskRunner taskRunner = new TaskRunner(1);
		
		// Act
		Future<Boolean> pendingResult = taskRunner.runTaskAsync(null, 1, 1, Boolean.class);
	}
	
	@Test
	public void GivenTwoParallelTasksWhenBothTasksSucceedFirstTimeThenTaskRunnerReturnsCorrectly() throws InterruptedException, ExecutionException {
		// Arrange
		TaskRunner taskRunner = new TaskRunner(2);
		
		// Task 1 setup
		ITask<Boolean> mockSuccessfulTask1 = (ITask<Boolean>) mock(ITask.class);
		when(mockSuccessfulTask1.call(Boolean.class)).thenAnswer(new Answer() {
			public Boolean answer(InvocationOnMock invocation) throws InterruptedException
			{
				System.out.println("mock1 called");
				Thread.sleep(1000);
				System.out.println("mock1 completing");
				return true;
			}
		});
		when(mockSuccessfulTask1.isComplete()).thenReturn(true);
		
		// Task 2 Setup
		ITask<Boolean> mockSuccessfulTask2 = (ITask<Boolean>) mock(ITask.class);
		when(mockSuccessfulTask2.call(Boolean.class)).thenAnswer(new Answer() {
			public Boolean answer(InvocationOnMock invocation) throws InterruptedException
			{
				System.out.println("mock2 called");
				Thread.sleep(1000);
				System.out.println("mock2 completing");
				return true;
			}
		});
		when(mockSuccessfulTask2.isComplete()).thenReturn(true);
		
		
		// Act
		Future<Boolean> pendingResult1 = taskRunner.runTaskAsync(mockSuccessfulTask1, 1, 1, Boolean.class);
		Future<Boolean> pendingResult2 = taskRunner.runTaskAsync(mockSuccessfulTask2, 1, 1, Boolean.class);
		boolean result1 = pendingResult1.get();
		boolean result2 = pendingResult2.get();
		
		// Assert
		assertTrue("Both tasks should return true on successful completion. Expected: true, actual: " + (result1 && result2), (result1 && result2));
	}

}
