package net.jesdevtest.jbefunge;

import java.util.LinkedList;
import java.util.List;

/**
 * FungeSpace is the grid that the IP will move on.
 */
public class FungeSpace {
    private int width, length;
    private List<int[]> currentPos;
    private char[][] funge;

    /**
     * Create the funge space
     *
     * @param width  the width of the grid
     * @param length the length of the grid
     */
    public FungeSpace(int width, int length) {
        init(width, length);
    }

    public FungeSpace() {
        init(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    private void init(int width, int length) {
        this.width = width;
        this.length = length;
        funge = new char[width][length];
        currentPos = new LinkedList<>();
        currentPos.add(new int[]{0, 0});
    }

    /**
     * Load a string of funge data into the grid. Throw an exception if the lines exceed grid space.
     *
     * @param inputData the funge data
     * @throws RuntimeException if the funge data exceeds grid space
     */
    public void loadSpace(String inputData) throws RuntimeException {
        String lines[] = inputData.split("\\r?\\n");
        try {
            for (int i = 0; i < lines.length; i++) {
                System.arraycopy(lines[i].toCharArray(), 0, funge[i], 0, lines[i].length());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Start the funge process. Defaults to 1000ms tick rate.
     *
     * @param debug if true, the system will print the grid each cycle.
     */
    public void start(boolean debug) {
        start(1000, debug);
    }

    /**
     * Start the funge process with a specified tick rate.
     *
     * @param tickRate the number of ms to sleep between ticks.
     * @param debug    if true, the system will print the grid each cycle.
     */
    public void start(int tickRate, boolean debug) {

    }
}
