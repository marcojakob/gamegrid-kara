package ch.gamegrid.kara.util;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import ch.aplu.jgamegrid.GGPath;

public class FileUtil {
	/**
	 * Opens a file chooser dialog to ask the user for a filename. If successful, 
	 * the given content is written to the chosen text file.
	 * 
	 * @param content Content to write to the file
	 */
	public static void saveToFile(String content) {
		File f = null;
		try {
			f = new File(new File(".").getCanonicalPath());
		} catch (IOException e) {
			// do nothing
		}
		JFileChooser fileChooser = new JFileChooser(f);
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "*.txt, *.TXT";
			}
			
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				String s = f.getName();
				return s.endsWith(".txt") || s.endsWith(".TXT");
			}
		});

		int retrival = fileChooser.showSaveDialog(null);

		if (retrival == JFileChooser.APPROVE_OPTION) {
			File chosenFile = fileChooser.getSelectedFile();
			
			if (chosenFile.exists()) {
				int option = JOptionPane.showConfirmDialog(null, "The file " + chosenFile + 
						" exists already. Do you want to overwrite the existing file?", "File Exists",
						JOptionPane.YES_NO_OPTION);
				if (option != JOptionPane.YES_OPTION) {
					// abort
					return;
				}
			}
			
			// write to file
			if (!chosenFile.getName().endsWith(".txt") && !chosenFile.getName().endsWith(".TXT")) {
				chosenFile = new File(chosenFile.getAbsolutePath() + ".txt");
			}
			GGPath.writeTextFile(chosenFile, content, false);
		}
	}
}
