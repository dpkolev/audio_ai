package edu.sofia.fmi.audiorec.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.sofia.fmi.audiorec.enums.FileType;

public class FSHelper {
	
	public static List<File> loadFilesFromFolder(String rootFolder, FileType ft) {
		List<File> found = new ArrayList<File>();
		reccursiveListing(found, new File(rootFolder), ft);
		return found;
	}
	
	private static void reccursiveListing(List<File> found, File folder, FileType ft) {
		for (File f: folder.listFiles()) {
			if (f.isFile() && f.getName().endsWith(ft.value())) {
				found.add(f);
			} else if (f.isDirectory()) {
				reccursiveListing(found, f, ft);
			}
		}
	}
	
}
