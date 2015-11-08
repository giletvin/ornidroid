package fr.ornidroid.bo;

public class ZipPackage {
	final int nbOfFilesToDownload;
	final String zipname;

	public ZipPackage(int nbOfFiles, String zipname) {
		this.nbOfFilesToDownload = nbOfFiles;
		this.zipname = zipname;
	}

	public int getNbOfFilesToDownload() {
		return nbOfFilesToDownload;
	}

	/***
	 * Returns the zip file name to download at the ith iteration
	 * 
	 * @param i
	 * @return the name of the zip file to download. This string contains two
	 *         parts : prefix : the name of the zip, "_", and the number of
	 *         files to download. Ex : wikipedia_1 (one wikipedia file to
	 *         download : wikipedia_1.zip) or images_5 (5 images packages to
	 *         download : images_1.zip, ...images_5.zip)
	 */
	public String getZipFileToDownload(int i) {

		StringBuffer sbuf = new StringBuffer();
		sbuf.append(zipname).append(i).append(".zip");
		return sbuf.toString();

	}
}
