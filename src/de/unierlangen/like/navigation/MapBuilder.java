package de.unierlangen.like.navigation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import android.util.Log;

public class MapBuilder {
    private static final String TAG = MapBuilder.class.getSimpleName();
    private ArrayList<Wall> walls;
    private ArrayList<Door> doors;
    private float wallX1;
    private float wallY1;
    private float wallX2;
    private float wallY2;
    private double alpha;
    private FileReader fileReader = new FileReader();

    public MapBuilder(String path) throws IOException {
        /** Create input channel and read from the file */
        recognizeString(fileReader.getDataFromFile(path), false);
    }

    /**
     * Debug constructor, use only for debug
     * 
     * @param content
     * @param debug
     */
    public MapBuilder(String content, boolean debug) {
        recognizeString(content, debug);
    }

    private void recognizeString(String content, boolean debug) {
        walls = new ArrayList<Wall>();
        doors = new ArrayList<Door>();
        Pattern oneNumber = Pattern.compile(",");
        for (String entry : fileReader.splitStringContent(content)) {
            String[] singlenumber = oneNumber.split(entry);
            if (singlenumber[0].equals("w")) {
                wallX1 = Float.parseFloat(singlenumber[1]);
                wallY1 = Float.parseFloat(singlenumber[2]);
                wallX2 = Float.parseFloat(singlenumber[3]);
                wallY2 = Float.parseFloat(singlenumber[4]);
                Wall wall = new Wall(wallX1, wallY1, wallX2, wallY2);
                alpha = wall.getAlpha();
                Log.d(TAG, "Created " + wall.toString());
                walls.add(wall);
            } else if (singlenumber[0].equals("d")) {
                Door door = new Door(Float.parseFloat(singlenumber[1]),
                        Float.parseFloat(singlenumber[2]), Float.parseFloat(singlenumber[3]),
                        Float.parseFloat(singlenumber[4]), Float.parseFloat(singlenumber[5]));
                Log.d(TAG, "Created " + door.toString());
                doors.add(door);
            } else {
                Door door = new Door(Float.parseFloat(singlenumber[0]),
                        Float.parseFloat(singlenumber[1]), Float.parseFloat(singlenumber[2]),
                        wallX1, wallY1, alpha);
                Log.d(TAG, "Created " + door.toString());
                doors.add(door);

            }
        }
    }

    // Methods
    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }

}
