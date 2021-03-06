package util.tasks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.Test;

public class PortAvailableTaskTests
{

	// Flakey unit tests - relying on actual ports in the tests.
	// For production code refactor to inject socket to enable mocking of port
	// interactions.
	// Haven't done so here as it felt a bit OTT for a short-lived toy problem!

	private static int targetPort = 56789;

	@Test
	public void GivenAValidAvailablePortNumberWhenPortAvailabilityCheckedThenCorrectResultReturned()
	{
		// Arrange
		final PortAvailableTask<Boolean> portAvailableTask = new PortAvailableTask<Boolean>(targetPort);

		// Act
		final boolean result = portAvailableTask.call(Boolean.class);

		// Assert
		assertTrue("Port should be available. Expected: true, actual: " + result, result);
	}

	@Test
	public void GivenAValidUnavailablePortNumberWhenPortAvailabilityCheckedThenCorrectResultReturned()
			throws IOException
	{
		// Arrange
		final PortAvailableTask<Boolean> portAvailableTask = new PortAvailableTask<Boolean>(targetPort);
		final ServerSocket blockingSocket = new ServerSocket(targetPort);

		// Act
		final boolean result = portAvailableTask.call(Boolean.class);

		// Assert
		assertFalse("Port should be available. Expected: false, actual: " + result, result);
		blockingSocket.close();
	}

	@Test(expected = IllegalArgumentException.class)
	public void GivenAPortNumberBelowLowerBoundWhenTaskCreatedThenCorrectExceptionThrown() throws IOException
	{
		// Arrange
		new PortAvailableTask<Boolean>(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void GivenAPortNumberAboveUpperBoundWhenTaskCreatedThenCorrectExceptionThrown() throws IOException
	{
		// Arrange
		new PortAvailableTask<Boolean>(70000);
	}
}
