package com.mygdx.game;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VideoGenerator {
    public static void create(String input, String output, String videoFileName, float fps) {
        int i;

        // Lists files in directory and assign audio and video files
        // Searches for files with regex patterns
        File directory = new File(input);
        File[] files = directory.listFiles();
        ArrayList<File> videoFiles = new ArrayList<File>();

        for (i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            if (fileName.matches(videoFileName)) {
                videoFiles.add(files[i]);
            }
        }

        // sorts the video files by name
        Collections.sort(videoFiles, new Comparator<File>() {
            public int compare(File fileA, File fileB) {
                return fileA.getName().compareTo(fileB.getName());
            }
        });

        // read byte arrays of audio and video files
        ArrayList<byte[]> videoFilesBytes = new ArrayList<byte[]>();
        try {
            for (i = 0; i < videoFiles.size(); i++) {
                videoFilesBytes.add(Files.readAllBytes(videoFiles.get(i).toPath()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // creates header
        // header consists of frame rate, number of video files and each video file's position and size
        int headerSize = Float.BYTES + Integer.BYTES + (videoFilesBytes.size() * Integer.BYTES * 2);
        ByteBuffer headerBuffer = ByteBuffer.allocate(headerSize);
        int headerPosition = 0;
        headerBuffer.putFloat(fps);
        headerBuffer.putInt(videoFilesBytes.size());
        for (i = 0; i < videoFilesBytes.size(); i++) {
            headerBuffer.putInt(headerSize + headerPosition);
            headerBuffer.putInt(videoFilesBytes.get(i).length);
            headerPosition += videoFilesBytes.get(i).length;
        }

        // outputs the data to new file
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(output));
            outputStream.write(headerBuffer.array());
            for (i = 0; i < videoFilesBytes.size(); i++) {
                outputStream.write(videoFilesBytes.get(i));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}