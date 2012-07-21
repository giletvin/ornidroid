package fr.ornidroid.bo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * The Class AudioOrnidroidFile.
 */
public class AudioOrnidroidFile extends AbstractOrnidroidFile {

	/** The Constant LINE_1. */
	public final static String LINE_1 = "line_1";

	/** The Constant LINE_2. */
	public final static String LINE_2 = "line_2";

	/**
	 * Gets the properties for screen.
	 * 
	 * @return the properties for screen
	 */
	public Map<String, String> getPropertiesForScreen() {
		Map<String, String> mapForScreen = new HashMap<String, String>();
		StringBuffer line1 = new StringBuffer();
		StringBuffer line2 = new StringBuffer();
		final String space = " ";
		String title = getProperty(AbstractOrnidroidFile.AUDIO_TITLE_PROPERTY);
		String duration = getProperty(AbstractOrnidroidFile.AUDIO_DURATION_PROPERTY);
		String recordist = getProperty(AbstractOrnidroidFile.AUDIO_RECORDIST_PROPERTY);
		String ref = getProperty(AbstractOrnidroidFile.AUDIO_REF_PROPERTY);
		String remarks = getProperty(AbstractOrnidroidFile.AUDIO_REMARKS_PROPERTY);

		if (StringUtils.isBlank(title)) {
			title = extractFilenameFromPath();
		}
		line1.append(title);
		line1.append(space);
		line1.append(duration);
		line2.append(recordist);
		line2.append(space);
		line2.append(ref);
		line2.append(space);
		line2.append(remarks);

		mapForScreen.put(LINE_1, line1.toString());
		mapForScreen.put(LINE_2, line2.toString());
		return mapForScreen;
	}

}
