/*
 * 
 */
package fr.giletvin.ornidroid.helper;

/**
 * The Class OrnidroidException.
 */
public class OrnidroidException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2330116343182553820L;

	/** The error type. */
	private final OrnidroidError errorType;

	/**
	 * Instantiates a new ornidroid exception.
	 * 
	 * @param pErrorType
	 *            the error type
	 */
	public OrnidroidException(OrnidroidError pErrorType) {
		super();
		errorType = pErrorType;
	}

	/**
	 * Gets the error type.
	 * 
	 * @return the error type
	 */
	public OrnidroidError getErrorType() {
		return errorType;
	}

}
