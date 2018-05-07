# jbefunge
Java-based Befunge Interpreter

## About
Just an attempt at implementing the Befunge-98 spec in java.
See https://github.com/catseye/Funge-98/blob/master/doc/funge98.markdown for the spec

## Version History

1.0 - Support for Befunge '93 spec as per https://esolangs.org/wiki/Befunge.

## Installing

Run `gradle build` and then `gradle shadowJar` to create the jar file for the release. It'll be located under `build/libs`

## Running J-BeFunge

`java -jar jbefunge-1.0-SNAPSHOT-all.jar -f hell.bf` will execute a befunge 93 file with a tick rate of 1 tick per second. This can be adjusted with the `-t` argument.

## Debugging

JBefunge supports a debug mode with `-d true` or `--debug true` where output is buffered and displayed after the program finishes execution. during this time you will see the cursor moving as a `0` through the application.
![Hello, World Demo](https://github.com/jharrison902/jbefunge/raw/master/hello_demo_befunge.gif)

Additionally there is `-p` or `--printTrace` which will show the operations as they occur so you can track the flow of your program.

## Known Issues

I have not extensively tested this interpreter. There may be bugs and errors. Use at your own risk.
