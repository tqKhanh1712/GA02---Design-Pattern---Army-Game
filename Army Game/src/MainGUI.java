import armygame.game.GameUIFrame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Entry Point - Army Game GUI Version
 * Chạy giao diện Swing
 */
public class MainGUI {
    public static void main(String[] args) throws Exception {
        // UTF-8 setup
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        
        // Launch GUI
        new GameUIFrame();
    }
}

