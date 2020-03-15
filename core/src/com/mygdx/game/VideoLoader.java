package com.mygdx.game;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class VideoLoader
{
    private float fps;
    private long milliDelay;
    private Music audio;
    private ArrayList<byte[]> videoDataArray;
    private Pixmap pixmap = null;
    private Texture texture = null;
    public long lastTime = 0;

    public VideoLoader(Music audio, FileHandle video) {
        this.audio = audio;

        int i;

        ByteBuffer buffer = ByteBuffer.wrap(video.readBytes());

        fps = buffer.getFloat();
        milliDelay = (long)(1000 / fps);
        int videoCount = buffer.getInt();
        int[] videoPositions = new int[videoCount];
        int[] videoSizes = new int[videoCount];
        for (i = 0; i < videoCount; i++)
        {
            videoPositions[i] = buffer.getInt();
            videoSizes[i] = buffer.getInt();
        }

        videoDataArray = new ArrayList<byte[]>();
        for (i = 0; i < videoCount; i++)
        {
            byte[] videoData = new byte[videoSizes[i]];
            buffer.get(videoData);
            videoDataArray.add(videoData);
        }

        texture = new Texture(2048, 2048, Format.RGB888);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
    }

    public long getMilliDelay()
    {
        return milliDelay;
    }

    public Texture getFrame()
    {
        return getFrameAtTime(audio.getPosition());
    }

    public Texture getFrame(int frame) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime >= getMilliDelay())
        {
            if (videoDataArray.get(frame).length > 0)
            {
                pixmap = new Pixmap(videoDataArray.get(frame), 0, videoDataArray.get(frame).length);
                texture.draw(pixmap, 0, 0);
                pixmap.dispose();
                pixmap = null;
            }
            lastTime = currentTime;
        }

        return texture;
    }

    public Texture getFrameAtTime(float seconds)
    {
        return getFrame((int)(fps * seconds));
    }

    public void play()
    {
        audio.play();
    }

    public void stop()
    {
        audio.stop();
    }
}