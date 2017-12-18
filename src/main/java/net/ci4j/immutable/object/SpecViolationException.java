package net.ci4j.immutable.object;

public class SpecViolationException extends RuntimeException
{
	public SpecViolationException()
	{
	}

	public SpecViolationException(String message)
	{
		super(message);
	}

	public SpecViolationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SpecViolationException(Throwable cause)
	{
		super(cause);
	}
}
