The directory tests should be placed somewhere on a web site to test the downloading features in ornidroid.

The web site has to be set in two files : 
 * DownloadConstants.java
	private static final String JUNIT_DOWNLOAD_SITE = "http://ornidroid.free.fr/tests";
 * AbstractTest.java
 	public static final String ORNIDROID_TEST_WEB_BASE_IMAGE_URL = "http://ornidroid.free.fr/tests/images";
 	

 