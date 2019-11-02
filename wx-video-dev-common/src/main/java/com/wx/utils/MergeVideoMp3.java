package com.wx.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MergeVideoMp3 {
	
	private String ffmpegEXE;

	public MergeVideoMp3(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}

	public void convertor(String videoInputPath, String mp3InputPath, double seconds, String videoOutputPath ) throws Exception {
		
		//ffmpeg -i input.mp4 -i bgm.mp3 -t -7 -y output.avi
		
		List<String> command = new ArrayList<>();
		command.add(ffmpegEXE);
		
		command.add("-i");
		command.add(videoInputPath);
		command.add("-i");
		command.add(mp3InputPath);
		command.add("-t");
		command.add(String.valueOf(seconds));
		command.add("-y");
		command.add(videoOutputPath);
		
		for (  String c : command ) {
			System.out.print(c + " ");
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
		
		MergeVideoMp3 ffmpeg = new MergeVideoMp3("/usr/local/Cellar/ffmpeg/4.2.1/bin/ffmpeg");
		try {
			ffmpeg.convertor("/Users/ashsay/Downloads/a.mp4", "/Users/ashsay/Downloads/b.mp3", 28, "/Users/ashsay/Downloads/c.mp4");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
