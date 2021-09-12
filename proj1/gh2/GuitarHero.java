package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHero {
    public static final double CONCERT_A = 440.0;
    public static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        GuitarString[] string = new GuitarString[37];
        for (int i = 0; i < string.length; i++){
            string[i] = new GuitarString(CONCERT_A * Math.pow(2, (i-24) / 12.0));
        }
        int index = 0;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                index = (keyboard.indexOf(key) != -1) ? keyboard.indexOf(key): 0;
                if (index != -1) {
                    string[index].pluck();
                }
            }
            double sample = string[index].sample();
            StdAudio.play(sample);
            string[index].tic();
        }
    }
}

