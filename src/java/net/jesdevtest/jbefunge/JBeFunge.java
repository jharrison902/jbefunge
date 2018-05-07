package net.jesdevtest.jbefunge;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The main JBeFunge Class. This class handles the core logic of the BeFunge interpreter.
 */
public class JBeFunge {
    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("JBeFunge").build()
                .defaultHelp(true)
                .description("Java-based BeFunge 98 interpreter");
        parser.addArgument("-w", "--width").type(Integer.class).help("The width of funge space").setDefault(80);
        parser.addArgument("-l", "--length").type(Integer.class).help("The length of funge space").setDefault(25);
        parser.addArgument("-d", "--debug").type(Boolean.class).help("Enable debugging").setDefault(false);
        parser.addArgument("-p", "--printTrace").type(Boolean.class).help("Print the trace as the program runs").setDefault(false);
        parser.addArgument("-f", "--file").type(String.class).help("The file to interpret").required(true);
        parser.addArgument("-t", "--tickrate").type(Integer.class).help("The tick rate for the program").setDefault(1000);

        Namespace res;
        try {
            res = parser.parseArgs(args);
            Integer width = res.getInt("width");
            Integer length = res.getInt("length");
            if (width == null) {
                width = Integer.MAX_VALUE;
            }
            if (length == null) {
                length = Integer.MAX_VALUE;
            }
            FungeSpace space = new FungeSpace(width, length);
            try {
                String fileString = new String(Files.readAllBytes(Paths.get(res.getString("file"))), StandardCharsets.UTF_8);
                space.loadSpace(fileString);
                space.start(res.getInt("tickrate"), res.getBoolean("debug"), res.getBoolean("printTrace"));
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
    }
}
