package fr.ornidroid.helper;

/**
 * The Class OrnidroidException.
 */
public class OrnidroidException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2330116343182553820L;

	/** The error type. */
	private final OrnidroidError errorType;

	/** The source exception. */
	private final Exception sourceException;

	/**
	 * Instantiates a new ornidroid exception.
	 * 
	 * @param pErrorType
	 *            the error type
	 * @param pSourceException
	 *            the source exception
	 */
	public OrnidroidException(final OrnidroidError pErrorType,
			final Exception pSourceException) {
		super();
		this.errorType = pErrorType;
		this.sourceException = pSourceException;
	}

	/**
	 * Gets the error type.
	 * 
	 * @return the error type
	 */
	public OrnidroidError getErrorType() {
		return this.errorType;
	}

	/**
	 * Gets the source exception.
	 * 
	 * @return the source exception
	 */
	public String getSourceExceptionMessage() {

		if (null != this.sourceException) {
			StringBuffer sbuf = new StringBuffer(
					this.sourceException.toString());
			sbuf.append(BasicConstants.CARRIAGE_RETURN);
			if (this.sourceException.getStackTrace() != null) {
				for (StackTraceElement ste : this.sourceException
						.getStackTrace()) {
					sbuf.append(ste.toString());
					sbuf.append(BasicConstants.CARRIAGE_RETURN);
				}
			}
			return sbuf.toString();
		}
		return BasicConstants.EMPTY_STRING;
	}

}
