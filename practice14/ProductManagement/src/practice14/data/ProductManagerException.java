package practice14.data;

public class ProductManagerException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 514429260035668726L;

	/**
	 * Constructor ProductManagerException
	 */
	public ProductManagerException() {
		super();
	}

	/**
	 * Constructor ProductManagerException
	 * 
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ProductManagerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructor ProductManagerException
	 * 
	 * @param message
	 * @param cause
	 */
	public ProductManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor ProductManagerException
	 * 
	 * @param message
	 */
	public ProductManagerException(String message) {
		super(message);
	}

	/**
	 * Constructor ProductManagerException
	 * 
	 * @param cause
	 */
	public ProductManagerException(Throwable cause) {
		super(cause);
	}

}
