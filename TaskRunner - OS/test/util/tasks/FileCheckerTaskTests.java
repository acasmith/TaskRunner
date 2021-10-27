package util.tasks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileCheckerTaskTests
{
	private static final String existsFileName = "exists.txt";
	private static final String nonExistantFileName = "doesNotExist.txt";

	// Flakey unit tests - relying on file system access.
	// For production code refactor to inject file checking object to enable mocking
	// of filesystem interactions.
	// Haven't done so here as it felt a bit OTT for a short-lived toy problem!
	@BeforeClass
	public static void SetupValidFile()
	{
		final File testFile = new File(FileCheckerTaskTests.existsFileName);
		try
		{
			testFile.createNewFile();
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void ClearDownValidFile()
	{
		final File testFile = new File(FileCheckerTaskTests.existsFileName);
		try
		{
			testFile.delete();
		} catch (final SecurityException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void GivenValidFileNameAndFileExistsWhenFileIsCheckedThenResultIsCorrect()
	{
		// Arrange
		final FileCheckerTask<Boolean> fileChecker = new FileCheckerTask<Boolean>(FileCheckerTaskTests.existsFileName);
		assertFalse(fileChecker.isComplete());

		// Act
		final boolean result = fileChecker.call(Boolean.class);
		final boolean isComplete = fileChecker.isComplete();

		// Assert
		assertTrue("Calling the check should return true. Expected: true, actual: " + result, result);
		assertTrue("Task should be complete. Expected: true, actual: " + isComplete, isComplete);
	}

	@Test
	public void GivenValidFileNameAndFileDoesNotExistWhenFileIsCheckedTheResultIsCorrect()
	{
		// Arrange
		final FileCheckerTask<Boolean> fileChecker = new FileCheckerTask<Boolean>(
				FileCheckerTaskTests.nonExistantFileName);
		assertFalse(fileChecker.isComplete());

		// Act
		final boolean result = fileChecker.call(Boolean.class);
		final boolean isComplete = fileChecker.isComplete();

		// Assert
		assertFalse("Calling the check check should return false. Expected: false, actual: " + result, result);
		assertFalse("Task should not be complete. Expected: false, actual: " + isComplete, isComplete);
	}

	@Test(expected = NullPointerException.class)
	public void GivenInvalidFileNameWhenTaskCreatedThenAnExceptionIsThrown()
	{
		// Arrange
		new FileCheckerTask<Boolean>(null);

		// Act

		// Assert
	}

	@Test(expected = ClassCastException.class)
	public void GivenIncorrectReturnTypeWhenFileCheckedThenAnExceptionIsThrown()
	{
		// Arrange
		final FileCheckerTask<String> fileChecker = new FileCheckerTask<String>(FileCheckerTaskTests.existsFileName);
		assertFalse(fileChecker.isComplete());

		// Act
		fileChecker.call(String.class);

		// Assert
	}

}
