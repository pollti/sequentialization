package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim Pollandt
 *
 */

public class FileExport {

	/**
	 * Mapping of filenames to index. Empowers us to use next index for next file.
	 */
	static Map<String, Integer> indexSet = new HashMap<String, Integer>();

	/**
	 * Writes file to filesystem.
	 * 
	 * @param indexed  whether to use next index or just write one file with this
	 *                 name.
	 * @param filename relative; name root
	 * @param filetype file ending
	 * @param content  content to write, several strings are appended
	 * @throws IOException
	 */
	public static void writeNextFile(boolean indexed, String filename, String filetype, String... content)
			throws IOException {
		BufferedWriter writer;
		if (indexed) {
			int index = 0;
			if (indexSet.containsKey(filename + "." + filetype)) {
				index = indexSet.get(filename + "." + filetype) + 1;
			}
			indexSet.put(filename + "." + filetype, index);
			writer = new BufferedWriter(new FileWriter(filename + index + "." + filetype));
		} else {
			writer = new BufferedWriter(new FileWriter(filename + "." + filetype));
		}
		for (String str : content) {
			writer.write(str);
		}
		writer.close();
	}

	/**
	 * Appends to a filesystem.
	 * 
	 * @param indexed  whether to use last index or just write in unindexed file
	 * @param filename relative; name root
	 * @param filetype file ending
	 * @param content  content to write, several strings are appended
	 * @throws IOException
	 */
	public static void appendToFile(boolean indexed, String filename, String filetype, String... content)
			throws IOException {
		BufferedWriter writer = null;
		if (indexed) {
			if (indexSet.containsKey(filename + "." + filetype)) {
				int index = indexSet.get(filename + "." + filetype);
				writer = new BufferedWriter(new FileWriter(filename + index + "." + filetype, true));
			} else {
				throw new IOException("File index not found. Appending failed.");
			}
		} else {
			writer = new BufferedWriter(new FileWriter(filename + "." + filetype, true));
		}
		for (String str : content) {
			writer.append(str);
		}
		writer.close();
	}

	/**
	 * Deletes files with specific name and ending. Indexed and unindexed files are
	 * being deleted.
	 * 
	 * @param filename relative; name root
	 * @param filetype file ending
	 */
	public static void clearFiles(String filename, String filetype) {
		new File(filename + ".tpt").delete();
		int i = 0;
		while (new File(filename + i + "." + filetype).delete()) {
			i++;
		}
		indexSet.put(filename, -1);
	}
}
