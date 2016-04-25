package core;

import java.util.HashMap;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.util.Duration;

public class SceneMaster {
    
    private final Context context;
    
    // game state
    private HashMap<String, Scene> scenes = new HashMap<>();
    private Scene scene;
    private boolean ticking = true;
    
    public SceneMaster(Context context) {
        this.context = context;
    }
    
    /**
     * Returns the current screen.
     *
     * @return the current screen
     */
    public Scene getScreen() {
        return scene;
    }
    
    /**
     * Sets the screen with the given name to be shown.
     *
     * @param name
     *            the name of the screen to be shown
     */
    public void showScreen(String name) {
        // dont try to set new screen
        if (scenes.get(name) == null) {
            System.err.println("scene name is invalid");
            return;
        }
        
        // update the showing scene
        boolean firstStart = (scene == null);
        scene = scenes.get(name);
        context.setScene(scene);
        
        if (!firstStart) {
            // show a fade in animation
            FadeTransition ft = new FadeTransition(Duration.millis(500));
            ft.setNode(scene.getRoot());
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
        }
    }
    
    /**
     * Adds a new screen with the given name.
     *
     * @param name
     *            the name of the screen
     * @param screen
     *            the screen
     */
    public void addScreen(String name, Scene screen) {
        scenes.put(name, screen);
    }
    
    /**
     * Removes the screen with the given name.
     *
     * @param name
     *            the name of the screen
     */
    public void removeScreen(String name) {
        scenes.remove(name);
    }
    
    public void tick(int ticks) {
        if (ticking && scene != null) {
        
        }
    }
    
    /**
     * Renders the current screen.
     */
    public void render() {
        if (scene != null) {
        }
    }
}
