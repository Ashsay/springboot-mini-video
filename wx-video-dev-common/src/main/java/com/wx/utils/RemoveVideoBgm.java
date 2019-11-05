package com.wx.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RemoveVideoBgm {

	private String ffmpegEXE;
	
	public RemoveVideoBgm(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}
	
	public void replaceBgm(String videoInputPath, String outputPath ) throws IOException, InterruptedException {
		
		List<String> command = new ArrayList<>();
		
		//ffmpeg -i ipt.mp4 -c:v copy -an output.mp4
		
		command.add(ffmpegEXE);
		command.add("-i");
		command.add(videoInputPath);
		command.add("-c:v");
		command.add("copy");
		command.add("-an");
		command.add(outputPath);
		
		ProcessBuilder builder = new ProcessBuilder(command);
		Process process = builder.start();
		
		InputStream errorStream = process.getErrorStream();
		InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
		BufferedReader br = new BufferedReader(inputStreamReader);
		
		String line = "";
		while( (line = br.readLine()) != null ) {}
		
		if(br != null) {
			br.close();
		}
		
		if(inputStreamReader != null) {
			inputStreamReader.close();
		}
		
		if(errorStream != null) {
			errorStream.close();
		}
		
	}
	
	public static void main(String arg[]) {
		RemoveVideoBgm ffmpeg = new RemoveVideoBgm("/usr/local/Cellar/ffmpeg/4.2.1/bin/ffmpeg");
		try {
			ffmpeg.replaceBgm("/Users/ashsay/Downloads/a.mp4","/Users/ashsay/Downloads/c.mp4");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
