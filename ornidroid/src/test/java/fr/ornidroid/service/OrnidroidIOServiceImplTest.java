package fr.ornidroid.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.ornidroid.bo.AbstractOrnidroidFile;
import fr.ornidroid.bo.AudioOrnidroidFile;
import fr.ornidroid.bo.Bird;
import fr.ornidroid.bo.BirdFactoryImpl;
import fr.ornidroid.bo.OrnidroidFileFactoryImpl;
import fr.ornidroid.bo.OrnidroidFileType;
import fr.ornidroid.bo.PictureOrnidroidFile;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.FileHelper;
import fr.ornidroid.helper.I18nHelper;
import fr.ornidroid.helper.OrnidroidError;
import fr.ornidroid.helper.OrnidroidException;
import fr.ornidroid.tests.AbstractTest;

/**
 * The Class OrnidroidIOServiceImplTest.
 */
public class OrnidroidIOServiceImplTest extends AbstractTest {

	/**
	 * Tests if the specified <code>File</code> is older than the specified
	 * <code>Date</code>.
	 * 
	 * @param file
	 *            the <code>File</code> of which the modification date must be
	 *            compared, must not be <code>null</code>
	 * @param date
	 *            the date reference, must not be <code>null</code>
	 * @return true if the <code>File</code> exists and has been modified before
	 *         the given <code>Date</code>.
	 * @throws IllegalArgumentException
	 *             if the file is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code>
	 */
	private static boolean isFileOlder(final File file, final Date date) {
		if (date == null) {
			throw new IllegalArgumentException("No specified date");
		}
		return isFileOlder(file, date.getTime());
	}

	/**
	 * Tests if the specified <code>File</code> is older than the specified time
	 * reference.
	 * 
	 * @param file
	 *            the <code>File</code> of which the modification date must be
	 *            compared, must not be <code>null</code>
	 * @param timeMillis
	 *            the time reference measured in milliseconds since the epoch
	 *            (00:00:00 GMT, January 1, 1970)
	 * @return true if the <code>File</code> exists and has been modified before
	 *         the given time reference.
	 * @throws IllegalArgumentException
	 *             if the file is <code>null</code>
	 */
	private static boolean isFileOlder(final File file, final long timeMillis) {
		if (file == null) {
			throw new IllegalArgumentException("No specified file");
		}
		if (!file.exists()) {
			return false;
		}
		return file.lastModified() < timeMillis;
	}

	/** The bird factory. */
	private BirdFactoryImpl birdFactory;

	/** The ornidroid io service. */
	private IOrnidroidIOService ornidroidIOService;

	/**
	 * Sets the up.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	@Before
	public void setUp() throws IOException {
		super.setUp();
		this.ornidroidIOService = new OrnidroidIOServiceImpl();
		this.birdFactory = new BirdFactoryImpl();
	}

	/**
	 * Test check and create directory.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testCheckAndCreateDirectory() throws OrnidroidException {

		// first : with an existing directory
		File testDir = new File(TEST_DIRECTORY);
		this.ornidroidIOService.checkAndCreateDirectory(testDir);
		File noMediaFile = new File(testDir, BasicConstants.NO_MEDIA_FILENAME);
		Assert.assertTrue(noMediaFile.exists());
		// second : with a non existing directory
		testDir = new File(TEST_DIRECTORY + File.separator
				+ "unknown_directory");
		this.ornidroidIOService.checkAndCreateDirectory(testDir);
		noMediaFile = new File(testDir, BasicConstants.NO_MEDIA_FILENAME);
		Assert.assertTrue(noMediaFile.exists());
	}

	/**
	 * Test check ornidroid home existing and empty directory. Should not be a
	 * pb. The subdirectories should be created
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testCheckOrnidroidHomeExistingAndEmptyDirectory()
			throws OrnidroidException {
		final File ornidroidHome = new File(TEST_DIRECTORY);
		Assert.assertTrue(ornidroidHome.exists());
		final File ornidroidHomeImages = new File(TEST_DIRECTORY
				+ File.separator + BasicConstants.IMAGES_DIRECTORY);
		Assert.assertFalse(ornidroidHomeImages.exists());
		final File ornidroidHomeAudio = new File(TEST_DIRECTORY
				+ File.separator + BasicConstants.AUDIO_DIRECTORY);
		Assert.assertFalse(ornidroidHomeAudio.exists());

		this.ornidroidIOService.checkOrnidroidHome(TEST_DIRECTORY);

		Assert.assertTrue(ornidroidHomeImages.exists());
		Assert.assertTrue(ornidroidHomeAudio.exists());

	}

	/**
	 * Test check ornidroid home with and existing directory with the correct
	 * subdirectories. Should not be a pb.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testCheckOrnidroidHomeExistingAndExistingSubdirectories()
			throws OrnidroidException {
		// first : creation
		testCheckOrnidroidHomeExistingAndEmptyDirectory();

		final File ornidroidHomeImages = new File(TEST_DIRECTORY
				+ File.separator + BasicConstants.IMAGES_DIRECTORY);
		final File ornidroidHomeAudio = new File(TEST_DIRECTORY
				+ File.separator + BasicConstants.AUDIO_DIRECTORY);
		// second run should do anything
		this.ornidroidIOService.checkOrnidroidHome(TEST_DIRECTORY);
		Assert.assertTrue(ornidroidHomeImages.exists());
		Assert.assertTrue(ornidroidHomeAudio.exists());
	}

	/**
	 * Test check ornidroid home non existing directory. Should not be a pb. The
	 * subdirectories should be created
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testCheckOrnidroidHomeNonExistingDirectory()
			throws OrnidroidException {
		final String ornidroidHomePath = TEST_DIRECTORY + File.separator
				+ "unexistingDirectory";
		final File ornidroidHome = new File(ornidroidHomePath);
		Assert.assertFalse(ornidroidHome.exists());
		final File ornidroidHomeImages = new File(ornidroidHomePath
				+ File.separator + BasicConstants.IMAGES_DIRECTORY);
		Assert.assertFalse(ornidroidHomeImages.exists());
		final File ornidroidHomeAudio = new File(ornidroidHomePath
				+ File.separator + BasicConstants.AUDIO_DIRECTORY);
		Assert.assertFalse(ornidroidHomeAudio.exists());

		this.ornidroidIOService.checkOrnidroidHome(ornidroidHomePath);

		Assert.assertTrue(ornidroidHome.exists());
		Assert.assertTrue(ornidroidHomeImages.exists());
		Assert.assertTrue(ornidroidHomeAudio.exists());
	}

	/**
	 * Test check ornidroid home no rights to write.
	 */
	@Test
	public void testCheckOrnidroidHomeNoRightsToWrite() {
		final File ornidroidHome = new File(TEST_DIRECTORY);
		ornidroidHome.setReadOnly();
		try {
			this.ornidroidIOService.checkOrnidroidHome(TEST_DIRECTORY);
			Assert.fail("an exception should have occurred");
		} catch (final OrnidroidException e) {
			Assert.assertTrue("ok, normal exception", true);
		}
	}

	/**
	 * Test do add custom media files.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testDoAddCustomMediaFiles() throws IOException,
			OrnidroidException {
		final OrnidroidIOServiceImpl serviceImpl = (OrnidroidIOServiceImpl) this.ornidroidIOService;
		// SET UP : src and dest dirs
		final File srcDir = buildOrnidroidHomeTest(TEST_DIRECTORY + "/srcDir");
		final File destDir = new File(TEST_DIRECTORY + "/destDir");
		destDir.mkdirs();
		Assert.assertEquals("directory should be empty before the test", 0,
				destDir.list().length);

		// 1st case : add an image from src dir to dest dir
		File selectedFile = new File(srcDir.getAbsolutePath() + "/images/"
				+ "file1.jpg");
		File destFile = new File(destDir.getAbsolutePath()
				+ "/custom_file1.jpg");
		File propertiesFile = new File(TEST_DIRECTORY
				+ "/destDir/custom_file1.jpg.properties");
		serviceImpl.doAddCustomMediaFiles(OrnidroidFileType.PICTURE,
				selectedFile, destFile, propertiesFile, "comment");
		// assertions to check the final state
		Assert.assertEquals("directory should have 2 files after the test", 2,
				destDir.list().length);
		final OrnidroidFileFactoryImpl factory = OrnidroidFileFactoryImpl
				.getFactory();
		AbstractOrnidroidFile ornidroidFile = factory.createOrnidroidFile(
				destFile.getAbsolutePath(), OrnidroidFileType.PICTURE,
				I18nHelper.ENGLISH);
		Assert.assertEquals("comment", ornidroidFile
				.getProperty(PictureOrnidroidFile.IMAGE_DESCRIPTION_PROPERTY));

		// 2nd case : add a mp3 file from src dir to dest dir
		selectedFile = new File(srcDir.getAbsolutePath() + "/audio/"
				+ "file1.mp3");
		destFile = new File(destDir.getAbsolutePath() + "/custom_file1.mp3");
		propertiesFile = new File(TEST_DIRECTORY
				+ "/destDir/custom_file1.mp3.properties");
		serviceImpl.doAddCustomMediaFiles(OrnidroidFileType.AUDIO,
				selectedFile, destFile, propertiesFile, "comment");
		// assertions to check the final state
		Assert.assertEquals("directory should have 4 files after the test", 4,
				destDir.list().length);

		ornidroidFile = factory.createOrnidroidFile(destFile.getAbsolutePath(),
				OrnidroidFileType.AUDIO, I18nHelper.ENGLISH);
		Assert.assertEquals("comment", ornidroidFile
				.getProperty(AudioOrnidroidFile.AUDIO_TITLE_PROPERTY));

		// other cases : try to copy an unknown file
		selectedFile = new File(srcDir.getAbsolutePath() + "/images/"
				+ "unknown_file1.jpg");
		try {
			serviceImpl.doAddCustomMediaFiles(OrnidroidFileType.PICTURE,
					selectedFile, destFile, propertiesFile, "comment");
			Assert.fail("an exception should have occurred");
		} catch (final OrnidroidException e) {
			Assert.assertTrue(e.getErrorType().equals(
					OrnidroidError.ADD_CUSTOM_MEDIA_ERROR));
		}

		// copy to a directory that doesn't exist
		selectedFile = new File(srcDir.getAbsolutePath() + "/images/"
				+ "file1.jpg");
		destFile = new File(destDir.getAbsolutePath()
				+ "/unknownDir/custom_file1.jpg");
		try {
			serviceImpl.doAddCustomMediaFiles(OrnidroidFileType.PICTURE,
					selectedFile, destFile, propertiesFile, "comment");
			Assert.fail("an exception should have occurred");
		} catch (final OrnidroidException e) {
			Assert.assertTrue(e.getErrorType().equals(
					OrnidroidError.ADD_CUSTOM_MEDIA_ERROR));
		}

	}

	/**
	 * Test load media files.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testDownLoadMediaFiles() throws InterruptedException,
			OrnidroidException {
		// run 1: with a bird without pictures. Should download them from
		// internet
		Bird bird = this.birdFactory.createBird(1, "taxon", "", null,
				"barge_a_queue_noire", "", null, "", "", "", null, null);
		Assert.assertNull("list of pictures should be null", bird.getPictures());
		Assert.assertNull("list of sounds should be null", bird.getSounds());
		this.ornidroidIOService.downloadMediaFiles(TEST_DIRECTORY
				+ File.separator + BasicConstants.IMAGES_DIRECTORY, bird,
				OrnidroidFileType.PICTURE);
		Assert.assertNotNull(bird.getPictures());
		Assert.assertEquals(2, bird.getNumberOfPictures());
		this.ornidroidIOService.downloadMediaFiles(TEST_DIRECTORY
				+ File.separator + BasicConstants.AUDIO_DIRECTORY, bird,
				OrnidroidFileType.AUDIO);
		Assert.assertNotNull(bird.getPictures());
		Assert.assertEquals(2, bird.getNumberOfPictures());
		Assert.assertNotNull(bird.getSounds());
		Assert.assertEquals(1, bird.getNumberOfSounds());
		Thread.sleep(1000);
		final Date date = new Date();
		Thread.sleep(1000);

		// run 2: with a bird with pictures. Should just load the files in the
		// List of pictures
		bird = this.birdFactory.createBird(1, "taxon", "", null,
				"barge_a_queue_noire", "", null, "", "", "", null, null);
		Assert.assertNull("list of pictures should be null", bird.getPictures());
		this.ornidroidIOService.loadMediaFiles(TEST_DIRECTORY + File.separator
				+ "/images", bird, OrnidroidFileType.PICTURE);
		Assert.assertNotNull(bird.getPictures());
		Assert.assertTrue(bird.getNumberOfPictures() == 2);
		Assert.assertTrue(isFileOlder(new File(TEST_DIRECTORY + File.separator
				+ BasicConstants.IMAGES_DIRECTORY + File.separator
				+ "barge_a_queue_noire/barge_a_queue_noire_1.jpg"), date));

	}

	/**
	 * Test load media files bird without files on internet.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testLoadMediaFilesBirdWithoutFiles() throws OrnidroidException {
		final Bird bird = this.birdFactory.createBird(1, "taxon", "", null,
				"bird_without_pictures", "", null, "", "", "", null, null);
		Assert.assertNull("list of pictures should be null", bird.getPictures());
		this.ornidroidIOService.loadMediaFiles(TEST_DIRECTORY, bird,
				OrnidroidFileType.PICTURE);
		Assert.assertNotNull(bird.getPictures());
		Assert.assertTrue(bird.getNumberOfPictures() == 0);
	}

	@Test
	public void testRemoveCustomMediaFile() throws OrnidroidException,
			IOException {

		// SET up the test : ornidroid home with file1.jpg official file and
		// custom_file1.jpg custom file
		final File srcDir = buildOrnidroidHomeTest(TEST_DIRECTORY + "/srcDir");
		final File imagesDir = new File(srcDir.getAbsolutePath()
				+ File.separator + Constants.IMAGES_DIRECTORY);
		FileHelper.createEmptyFile(new File(imagesDir.getAbsolutePath()
				+ File.separator + BasicConstants.CUSTOM_MEDIA_FILE_PREFIX
				+ "file1.jpg"));
		FileHelper.createEmptyFile(new File(imagesDir.getAbsolutePath()
				+ File.separator + BasicConstants.CUSTOM_MEDIA_FILE_PREFIX
				+ "file1.jpg.properties"));

		final OrnidroidFileFactoryImpl factory = OrnidroidFileFactoryImpl
				.getFactory();
		final AbstractOrnidroidFile officialOrnidroidFile = factory
				.createOrnidroidFile(
						imagesDir.getAbsolutePath() + "/file1.jpg",
						OrnidroidFileType.PICTURE, I18nHelper.ENGLISH);
		final AbstractOrnidroidFile customOrnidroidFile = factory
				.createOrnidroidFile(imagesDir.getAbsolutePath()
						+ "/custom_file1.jpg", OrnidroidFileType.PICTURE,
						I18nHelper.ENGLISH);
		Assert.assertEquals(
				"there should be 4 files in the test directory : file1.jpg, custom_file1.jpg and their properties files",
				4, imagesDir.list().length);
		// SET UP FINISHED

		// TESTS
		// try to remove an official file : this should not remove anything
		this.ornidroidIOService.removeCustomMediaFile(officialOrnidroidFile);
		Assert.assertEquals(
				"there should be still 4 files in the test directory : file1.jpg, custom_file1.jpg and their properties files",
				4, imagesDir.list().length);

		this.ornidroidIOService.removeCustomMediaFile(customOrnidroidFile);
		Assert.assertEquals(
				"there should be 2 files in the test directory : file1.jpg,  and its properties file",
				2, imagesDir.list().length);
	}
}
