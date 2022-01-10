package com.jyashu;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author J Yashu
 */

public class SoundPlayer {

    private Clip music;

    private static final Map<Integer, String> sounds = new HashMap<>();

    private AudioInputStream audioInputStream;
    private static String filePath;

    public SoundPlayer() {

        sounds.put(0, "res/sounds/levelCleared.wav");
        sounds.put(1, "res/sounds/brickBreak.wav");
        sounds.put(2, "res/sounds/wallDeflect.wav");
        sounds.put(3, "res/sounds/playerDeflect.wav");
        sounds.put(4, "res/sounds/gameLost.wav");
        sounds.put(5, "res/sounds/highScore.wav");
        sounds.put(6, "res/sounds/enterKeyPress.wav");
        sounds.put(7, "res/sounds/movement.wav");
        sounds.put(8, "res/sounds/level.wav");
        sounds.put(9, "res/sounds/level1.wav");
        sounds.put(10, "res/sounds/level2.wav");
    }

    // play the effects
    public void play(int num) {
        // create AudioInputStream object
        try {

            filePath = sounds.get(num);
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

            Clip effect = AudioSystem.getClip();
            effect.open(audioInputStream);

            effect.start();

        } catch (Exception e) {
            System.out.println("Error with playing sound.");
            e.printStackTrace();
        }
    }

    // play the music
    public void cue() {
        try {
            setAudioStream();

            music.start();

        } catch (Exception e) {
            System.out.println("Error with playing sound.");
            e.printStackTrace();
        }
    }

    // stop the audio
    public void stopAudio() {
        try {
            music.stop();
            music.close();
        } catch (Exception e) {
            System.out.println("Failed to stop the audio.");
            e.printStackTrace();
        }
    }

    // set audio stream
    public void setAudioStream() {
        try {
            filePath = sounds.get((int) (Math.random()*3 + 8));
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            music = AudioSystem.getClip();
            music.open(audioInputStream);
            FloatControl gainControl = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-8.0f);
            music.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println("Failed to set the audio.");
            e.printStackTrace();
        }
    }

}
