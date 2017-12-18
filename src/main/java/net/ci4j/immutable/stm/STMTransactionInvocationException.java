package net.ci4j.immutable.stm;

public class STMTransactionInvocationException extends Exception
{
	public STMTransactionInvocationException()
	{
	}

	public STMTransactionInvocationException(String message)
	{
		super(message);
	}

	public STMTransactionInvocationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public STMTransactionInvocationException(Throwable cause)
	{
		super(cause);
	}
}
