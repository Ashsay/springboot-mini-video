package com.wx.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FFMpegTest {
	
	private String ffmpegEXE;

	public FFMpegTest(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}

	public void convertor(String videoInputPath, String videoOutputPath) throws Exception {
		
		//ffmpeg -i input.mp4 output.avi
		
		List<String> command = new ArrayList<>();
		command.add(ffmpegEXE);
		
		command.add("-i");
		command.add(videoInputPath);
		command.add("-y");
		command.add(videoOutputPath);
		
		for (  String c : command ) {
			System.out.println(c);
		}
		
		ProcessBuilder builder = new ProcessBuilder(command);
		Process process =  builder.start();
		
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
	
	public static void main(String[] args) {
		
		FFMpegTest ffmpeg = new FFMpegTest("/usr/local/Cellar/ffmpeg/4.2.1/bin/ffmpeg");
		try {
			ffmpeg.convertor("/Users/ashsay/Downloads/a.mp4", "/Users/ashsay/Downloads/aa.avi");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
