package fr.giletvin.ornidroid.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.giletvin.ornidroid.bo.Bird;
import fr.giletvin.ornidroid.bo.BirdFactoryImpl;
import fr.giletvin.ornidroid.bo.OrnidroidFileType;
import fr.giletvin.ornidroid.helper.BasicConstants;
import fr.giletvin.ornidroid.helper.OrnidroidException;
import fr.giletvin.ornidroid.tests.AbstractTest;

/**
 * The Class OrnidroidIOServiceImplTest.
 */
public class OrnidroidIOServiceImplTest extends AbstractTest {

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
		// second : with a non existing directory
		testDir = new File(TEST_DIRECTORY + File.separator
				+ "unknown_directory");
		this.ornidroidIOService.checkAndCreateDirectory(testDir);
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
		File ornidroidHome = new File(TEST_DIRECTORY);
		Assert.assertTrue(ornidroidHome.exists());
		File ornidroidHomeImages = new File(TEST_DIRECTORY + File.separator
				+ BasicConstants.IMAGES_DIRECTORY);
		Assert.assertFalse(ornidroidHomeImages.exists());
		File ornidroidHomeAudio = new File(TEST_DIRECTORY + File.separator
				+ BasicConstants.AUDIO_DIRECTORY);
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

		File ornidroidHomeImages = new File(TEST_DIRECTORY + File.separator
				+ BasicConstants.IMAGES_DIRECTORY);
		File ornidroidHomeAudio = new File(TEST_DIRECTORY + File.separator
				+ BasicConstants.AUDIO_DIRECTORY);
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
		String ornidroidHomePath = TEST_DIRECTORY + File.separator
				+ "unexistingDirectory";
		File ornidroidHome = new File(ornidroidHomePath);
		Assert.assertFalse(ornidroidHome.exists());
		File ornidroidHomeImages = new File(ornidroidHomePath + File.separator
				+ BasicConstants.IMAGES_DIRECTORY);
		Assert.assertFalse(ornidroidHomeImages.exists());
		File ornidroidHomeAudio = new File(ornidroidHomePath + File.separator
				+ BasicConstants.AUDIO_DIRECTORY);
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
		File ornidroidHome = new File(TEST_DIRECTORY);
		ornidroidHome.setReadOnly();
		try {
			this.ornidroidIOService.checkOrnidroidHome(TEST_DIRECTORY);
			Assert.fail("an exception should have occurred");
		} catch (OrnidroidException e) {
			Assert.assertTrue("ok, normal exception", true);
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
		Bird bird = this.birdFactory.createBird(1, "taxon", "",
				"barge_a_queue_noire", "", "", "");
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
		Date date = new Date();
		Thread.sleep(1000);

		// run 2: with a bird with pictures. Should just load the files in the
		// List of pictures
		bird = this.birdFactory.createBird(1, "taxon", "",
				"barge_a_queue_noire", "", "", "");
		Assert.assertNull("list of pictures should be null", bird.getPictures());
		this.ornidroidIOService.loadMediaFiles(TEST_DIRECTORY + File.separator
				+ "/images", bird, OrnidroidFileType.PICTURE);
		Assert.assertNotNull(bird.getPictures());
		Assert.assertTrue(bird.getNumberOfPictures() == 2);
		Assert.assertTrue(FileUtils.isFileOlder(new File(TEST_DIRECTORY
				+ File.separator + BasicConstants.IMAGES_DIRECTORY
				+ File.separator
				+ "barge_a_queue_noire/barge_a_queue_noire_1.jpg"), date));

	}

	/**
	 * Test is directory empty.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testIsDirectoryEmpty() throws IOException {
		File testDir = new File(TEST_DIRECTORY);
		Assert.assertTrue(this.ornidroidIOService.isDirectoryEmpty(testDir));
		FileUtils.touch(new File(TEST_DIRECTORY + File.separator + "test.txt"));
		Assert.assertFalse(this.ornidroidIOService.isDirectoryEmpty(testDir));

	}

	/**
	 * Test load media files bird without files on internet.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	@Test
	public void testLoadMediaFilesBirdWithoutFiles() throws OrnidroidException {
		Bird bird = this.birdFactory.createBird(1, "taxon", "",
				"bird_without_pictures", "", "", "");
		Assert.assertNull("list of pictures should be null", bird.getPictures());
		this.ornidroidIOService.loadMediaFiles(TEST_DIRECTORY, bird,
				OrnidroidFileType.PICTURE);
		Assert.assertNotNull(bird.getPictures());
		Assert.assertTrue(bird.getNumberOfPictures() == 0);
	}
}
