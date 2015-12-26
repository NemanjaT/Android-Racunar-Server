package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileSystem {
	Process process;
	ProcessBuilder processBuilder;
	
	public FileSystem() {
		processBuilder = new ProcessBuilder();
	}
	
	public ArrayList<String> getDrives() {
		ArrayList<String> stringList = new ArrayList<String>();
		String line;
		try {
			process = new ProcessBuilder().command("fsutil", "fsinfo", "drives").start();
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while((line = input.readLine()) != null)
				stringList.add(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<String> temp = new ArrayList<String>();
		for(String s : stringList) {
			String[] ss = s.split(" ");
			for(String sss : ss)
				temp.add(sss);
		}
		return temp;
	}
}
