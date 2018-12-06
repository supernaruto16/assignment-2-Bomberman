package uet.oop.bomberman.Sound;

import uet.oop.bomberman.Game;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

public class SoundManager {

    private static ArrayList<AudioPlayer> playing = new ArrayList<>();

    public static void play(String name) {
        if (!Game._allowMusic) return;
        AudioPlayer sound = null;
        String path = "res/sounds/" + name +".wav";
        try {
            sound = new AudioPlayer(path);
            sound.play();
            playing.add(sound);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void allMuteExceptBGM() {
        for (int i = 1; i < playing.size(); i++) {
            if (playing.get(i) != null) {
                try {
                    AudioPlayer sound = playing.get(i);
                    sound.stop();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void allMute() {
        for (AudioPlayer sound : playing) {
            try {
                sound.stop();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
        playing = new ArrayList<>();
    }
}
