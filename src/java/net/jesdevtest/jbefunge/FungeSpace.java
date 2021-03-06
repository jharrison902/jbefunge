package net.jesdevtest.jbefunge;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * FungeSpace is the grid that the IP will move on.
 */
public class FungeSpace {
    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

    public static final int STR_ON = 1;
    public static final int STR_OFF = 0;

    public static final int BRIDGE_ON = 3;
    public static final int BRIDGE_OFF = 2;

    private int width, length;
    private List<int[]> currentPos;
    private char[][] funge;
    private LinkedList<FungeVal> stack;
    private boolean quit;

    private boolean borrowedBuffer; //in case of debug or printTrace
    private StringBuilder endBuffer;

    private Scanner reader;
    private boolean explainOps; //whether we should explain the ops as they run

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
        funge = new char[length][width];
        currentPos = new LinkedList<>();
        currentPos.add(new int[]{0, 0, RIGHT, STR_OFF, BRIDGE_OFF}); //start at 0,0 moving right with string toggle off and bridging off.
        stack = new LinkedList<>(); //could be a stack, but going with this for now.
        endBuffer = new StringBuilder();
        reader = new Scanner(System.in);
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
    public void start(boolean debug, boolean printTrace) {
        start(1000, debug, printTrace);
    }

    /**
     * Start the funge process with a specified tick rate.
     *
     * @param tickRate the number of ms to sleep between ticks.
     * @param debug    if true, the system will print the grid each cycle.
     */
    public void start(int tickRate, boolean debug, boolean printTrace) {
        if (debug || printTrace) {
            borrowedBuffer = true;
            this.explainOps = printTrace;
        }
        while (!quit) {
            for (int[] ip : currentPos) {
                if (ip[4] == BRIDGE_ON) {
                    ip[4] = BRIDGE_OFF;
                } else {
                    //get current char at this position
                    char op = funge[ip[0]][ip[1]];
                    if (printTrace) {
                        System.out.println(op);
                    }
                    if (ip[3] == 0) {
                        doOp(op, ip);
                    } else {
                        push(op, ip);
                    }
                }
                moveIp(ip);
                try {
                    if (debug) {
                        displayFungeSpace();
                    }
                    Thread.sleep(tickRate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println();
        if (borrowedBuffer) {
            System.out.println(endBuffer.toString());
        }
        System.exit(0);
    }

    private void moveIp(int[] ip) {
        switch (ip[2]) {
            case UP:
                goUp(ip);
                break;
            case DOWN:
                goDown(ip);
                break;
            case LEFT:
                goLeft(ip);
                break;
            case RIGHT:
                goRight(ip);
                break;
            default:
                break;
        }
    }

    private void goUp(int[] ip) {
        if (explainOps) {
            System.out.println("Moving cursor up");
        }
        if (ip[0] == 0) {
            ip[0] = funge.length - 1;
        } else {
            ip[0] -= 1;
        }
    }

    private void goDown(int[] ip) {
        if (explainOps) {
            System.out.println("Moving cursor down");
        }
        if (ip[0] == funge.length - 1) {
            ip[0] = 0;
        } else {
            ip[0] += 1;
        }
    }

    private void goLeft(int[] ip) {
        if (explainOps) {
            System.out.println("Moving cursor left");
        }
        if (ip[1] == 0) {
            ip[1] = funge[0].length - 1;
        } else {
            ip[1] -= 1;
        }
    }

    private void goRight(int[] ip) {
        if (explainOps) {
            System.out.println("Moving cursor right");
        }
        if (ip[1] == funge[0].length - 1) {
            ip[1] = 0;
        } else {
            ip[1] += 1;
        }
    }

    private void doOp(char op, int[] ip) {
        switch (op) {
            case '+':
                add();
                break;
            case '-':
                sub();
                break;
            case '*':
                mul();
                break;
            case '/':
                div();
                break;
            case '%':
                mod();
                break;
            case '!':
                not();
                break;
            case '`':
                gt();
                break;
            case '>':
                right(ip);
                break;
            case '<':
                left(ip);
                break;
            case '^':
                up(ip);
                break;
            case 'v':
                down(ip);
                break;
            case '?':
                random(ip);
                break;
            case '_':
                horizIf(ip);
                break;
            case '|':
                vertIf(ip);
                break;
            case '"':
                toggleStr(ip);
                break;
            case ':':
                dup();
                break;
            case '\\':
                swap();
                break;
            case '$':
                discard();
                break;
            case '.':
                printInt();
                break;
            case ',':
                printChar();
                break;
            case '#':
                bridge(ip);
                break;
            case 'g':
                get();
                break;
            case 'p':
                put();
                break;
            case '&':
                getInt();
                break;
            case '~':
                getChar();
                break;
            case '@':
                end();
                break;
            case '0':
                push(0);
                break;
            case '1':
                push(1);
                break;
            case '2':
                push(2);
                break;
            case '3':
                push(3);
                break;
            case '4':
                push(4);
                break;
            case '5':
                push(5);
                break;
            case '6':
                push(6);
                break;
            case '7':
                push(7);
                break;
            case '8':
                push(8);
                break;
            case '9':
                push(9);
                break;
            default:
                break;
        }
    }

    //operation section. Stack stores FungeVals.
    private void add() {

        FungeVal a = popStack();
        FungeVal b = popStack();
        if (explainOps) {
            System.out.println("Adding " + a.getIntValue() + " + " + b.getIntValue());
        }
        stack.push(new FungeVal(a.getIntValue() + b.getIntValue()));
    }

    private void sub() {
        FungeVal a = popStack();
        FungeVal b = popStack();
        if (explainOps) {
            System.out.println("Subtracting " + b.getIntValue() + " - " + a.getIntValue());
        }
        stack.push(new FungeVal(b.getIntValue() - a.getIntValue()));
    }

    private void mul() {
        FungeVal a = popStack();
        FungeVal b = popStack();
        if (explainOps) {
            System.out.println("Multiplying " + b.getIntValue() + " * " + a.getIntValue());
        }
        stack.push(new FungeVal(b.getIntValue() * a.getIntValue()));
    }

    private void div() {
        FungeVal a = popStack();
        FungeVal b = popStack();
        if (explainOps) {
            System.out.println("Dividing " + b.getIntValue() + " / " + a.getIntValue());
        }
        stack.push(new FungeVal(b.getIntValue() / a.getIntValue()));
    }

    private void mod() {
        FungeVal a = popStack();
        FungeVal b = popStack();
        if (explainOps) {
            System.out.println("Modulating " + b.getIntValue() + " % " + a.getIntValue());
        }
        stack.push(new FungeVal(b.getIntValue() % a.getIntValue()));
    }

    private void not() {
        FungeVal a = popStack();
        if (a.getIntValue() == 0) {
            if (explainOps) {
                System.out.println("Pushing 1 to the stack");
            }
            stack.push(new FungeVal(1));
        } else {
            if (explainOps) {
                System.out.println("Pushing 0 to the stack");
            }
            stack.push(new FungeVal(0));
        }
    }

    private void gt() {
        FungeVal a = popStack();
        FungeVal b = popStack();
        if (b.getIntValue() > a.getIntValue()) {
            if (explainOps) {
                System.out.println(b.getIntValue() + " is greater than " + a.getIntValue() + ". Pushing 1");
            }
            stack.push(new FungeVal(1));
        } else {
            if (explainOps) {
                System.out.println(b.getIntValue() + " is not greater than " + a.getIntValue() + ". Pushing 0");
            }
            stack.push(new FungeVal(0));
        }
    }

    private void right(int[] ip) {
        ip[2] = RIGHT;
    }

    private void left(int[] ip) {
        ip[2] = LEFT;
    }

    private void up(int[] ip) {
        ip[2] = UP;
    }

    private void down(int[] ip) {
        ip[2] = DOWN;
    }

    private void random(int[] ip) {
        ip[2] = ThreadLocalRandom.current().nextInt(0, 4);
        if (explainOps) {
            System.out.println("Moving randomly");
        }
    }

    private void horizIf(int[] ip) {
        FungeVal a = popStack();
        if (a.getIntValue() == 0) {
            if (explainOps) {
                System.out.println(a.getIntValue() + " is 0. Go right");
            }
            right(ip);
        } else {
            if (explainOps) {
                System.out.println(a.getIntValue() + " is not 0. Go left");
            }
            left(ip);
        }
    }

    private void vertIf(int[] ip) {
        FungeVal a = popStack();
        if (a.getIntValue() == 0) {
            if (explainOps) {
                System.out.println(a.getIntValue() + " is 0. Go down");
            }
            down(ip);
        } else {
            if (explainOps) {
                System.out.println(a.getIntValue() + " is 0. Go up");
            }
            up(ip);
        }
    }

    private void dup() {
        FungeVal a = popStack();
        stack.push(new FungeVal(a));
        stack.push(new FungeVal(a));
        if (explainOps) {
            System.out.println(a.getIntValue() + " is duplicated");
        }
    }

    private void toggleStr(int[] ip) {
        if (ip[3] == STR_OFF) {
            if (explainOps) {
                System.out.println("String toggle on");
            }
            ip[3] = STR_ON;
        } else {
            if (explainOps) {
                System.out.println("String toggle off");
            }
            ip[3] = STR_OFF;
        }
    }

    private void swap() {
        FungeVal a = popStack();
        FungeVal b = popStack();
        stack.push(a);
        stack.push(b);
        if (explainOps) {
            System.out.println("Swapping " + a.getCharValue() + " and " + b.getCharValue());
        }
    }

    private void discard() {
        popStack();
        if (explainOps) {
            System.out.println("discard top of stack");
        }
    }

    private void printInt() {
        FungeVal a = popStack();
        if (explainOps) {
            System.out.println("Print integer " + a.getIntValue());
        }
        if (!borrowedBuffer) {
            System.out.print(a.getIntValue());
        } else {
            endBuffer.append(a.getIntValue());
        }
    }

    private void printChar() {
        FungeVal a = popStack();
        if (explainOps) {
            System.out.println("Print character " + a.getCharValue());
        }
        if (!borrowedBuffer) {
            System.out.print(a.getCharValue());
        } else {
            endBuffer.append(a.getCharValue());
        }
    }

    private void bridge(int[] ip) {
        if (explainOps) {
            System.out.println("Bridging next instruction");
        }
        ip[4] = BRIDGE_ON;
    }

    private void get() {
        int y = popStack().getIntValue();
        int x = popStack().getIntValue();
        if (x >= 0 && x < funge[0].length
                && y >= 0 && y < funge.length) {
            if (explainOps) {
                System.out.println("Getting value at (" + x + ", " + y + "): " + funge[y][x]);
            }
            stack.push(new FungeVal(funge[y][x]));
        } else {
            if (explainOps) {
                System.out.println("Getting value at (" + x + ", " + y + "): Out of range. Pushing Integer 0");
            }
            stack.push(new FungeVal(0));
        }
    }

    private void put() {
        int y = popStack().getIntValue();
        int x = popStack().getIntValue();
        FungeVal v = popStack();

        if (x >= 0 && x < funge[0].length
                && y >= 0 && y < funge.length) {
            if (explainOps) {
                System.out.println("Setting (" + x + ", " + y + ") to " + v.getCharValue());
            }
            funge[y][x] = v.getCharValue();
        }
    }

    private void getInt() {
        Scanner reader = new Scanner(System.in);
        FungeVal val = new FungeVal(reader.nextInt());
        stack.push(val);
        if (explainOps) {
            System.out.println("Received " + val.getIntValue());
        }
        reader.close();
    }

    private void getChar() {
        String input = reader.nextLine().trim();
        FungeVal val;
        if (input.isEmpty()) {
            val = new FungeVal('\n');
        } else {
            val = new FungeVal(input.charAt(0));
        }
        if (explainOps) {
            System.out.println("Received " + (val.getCharValue() == '\n' ? "LINE FEED" : val.getCharValue()));
        }
        stack.push(val); //limitation in keeping this platform agnostic sadly...
    }

    private void end() {
        if (explainOps) {
            System.out.println("Quit received. Ending after this cycle");
        }
        quit = true;
    }

    private void push(char c, int[] ip) {
        if (c != '"') {
            stack.push(new FungeVal(c));
        } else {
            toggleStr(ip);
        }
    }

    private void push(int i) {
        stack.push(new FungeVal(i));
    }

    private void displayFungeSpace() {
        StringBuilder buffer = new StringBuilder(width * length);
        buffer.append("\r");
        for (int i = 0; i < funge.length; i++) {
            char[] currentLine = new char[funge[i].length];
            System.arraycopy(funge[i], 0, currentLine, 0, funge[i].length);
            for (int[] ip : currentPos) {
                if (i == ip[0]) {
                    currentLine[ip[1]] = '0';
                }
            }
            buffer.append(currentLine);
            buffer.append(System.lineSeparator());
        }
        System.out.print(buffer.toString());
        System.out.flush();
    }

    private FungeVal popStack() {
        try {
            return stack.pop();
        } catch (Exception e) {
            return new FungeVal(0);
        }
    }
}
