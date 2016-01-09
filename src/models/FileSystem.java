package models;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FileSystem {
	
	public ArrayList<String> getDrives() {
		ArrayList<String> stringList = new ArrayList<>();
		try {
			Process process = new ProcessBuilder("fsutil", "fsinfo", "drives").start();
			fillInfo(process, stringList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<String> resultList = new ArrayList<>();
		for(String string : stringList) {
			String[] splitStrings = string.split(" ");
            resultList.addAll(Arrays.asList(splitStrings));
		}
		resultList.remove(0);
		resultList.remove(0);
		return resultList;
	}
	
	public ArrayList<String> getFilesAndDirectories(String path) {
		ArrayList<String> stringList = new ArrayList<>();
		File[] files = new File(path).listFiles();
		if(files != null)
            for(File file : files)
                if (file.isDirectory())
                    stringList.add(file.getName() + "|DIR");
                else
                    stringList.add(file.getName() + "|???");
		return stringList;
	}

	public boolean openFileLocally(String path) {
		boolean solved = false;
		try {
			if(!new File(path).isFile())
				return false;
			if(Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(new File(path));
				solved = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return solved;
	}

	public byte[] fileToByte(String path) {
		File file = new File(path);
		if(!file.isFile())
			return null;
		return new byte[(int)file.length()];
	}

	public boolean sendFile(byte[] bytes, OutputStream output) {
		boolean solved = false;
		try {
			output.write(bytes, 0, bytes.length);
			output.flush();
			solved = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return solved;
	}
	
	private void fillInfo(Process process, ArrayList<String> filler) throws IOException {
		String line;
		BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
		while((line = input.readLine()) != null)
			filler.add(line);
	}
}
