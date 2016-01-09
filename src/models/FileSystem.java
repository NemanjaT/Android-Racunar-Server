package models;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
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
	
	public ArrayList<Poruka> getFilesAndDirectories(String path) {
		ArrayList<Poruka> stringList = new ArrayList<>();
		File[] files = new File(path).listFiles();
		if(files != null)
            for(File file : files)
                if (file.isDirectory())
                    stringList.add(new Poruka("", file.getName(), "DIR"));
                else {
					String[] delovi = file.getName().split("\\.");
					stringList.add(new Poruka("", file.getName(), delovi[delovi.length - 1].toUpperCase()));
				}
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

	public boolean deleteFileLocally(String path) {
		boolean solved = false;
		File file = new File(path);
		if(!file.isDirectory())
			if(file.delete())
				solved = true;
		return solved;
	}

	public boolean sendFileToClient(File file, OutputStream os) {
		boolean solved = false;
		BufferedInputStream bis = null;
		FileInputStream fis = null;
		try {
			byte[] byteArray = new byte[(int) file.length()];
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			if(bis.read(byteArray, 0, byteArray.length) > -1) {
				os.write(byteArray, 0, byteArray.length);
				os.flush();
				solved = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (bis != null)
					bis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
