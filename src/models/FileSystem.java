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

	public boolean sendFileToClient(String path, OutputStream os) {
		boolean solved = false;
		try {
			File file = new File(path);
			if (!file.isFile())
				return false;
			byte[] nizBitova = new byte[(int) file.length()];
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			int bytesRead = bis.read(nizBitova, 0, nizBitova.length);
			System.out.println(bytesRead);
			os.write(nizBitova, 0, nizBitova.length);
			os.flush();
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
