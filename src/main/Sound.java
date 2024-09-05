package main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {

    // Method to play the sound
    public static void play(String filePath) {
        try {
            // Open the audio file
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            // Get a sound clip resource
            Clip clip = AudioSystem.getClip();

            // Open the clip and load the sound from the audio stream
            clip.open(audioStream);

            // Start playing the sound
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
