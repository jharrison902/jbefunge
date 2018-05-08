# jbefunge
Java-based Befunge Interpreter

## About
Just an attempt at implementing the Befunge-93 spec and Funge-98 spec in java.
See https://github.com/catseye/Funge-98/blob/master/doc/funge98.markdown for the spec

## Version History

1.1 - Support for Befunge '93 spec as per https://esolangs.org/wiki/Befunge.

1.0 - Initial snapshot

## Installing

Run `gradle build` and then `gradle shadowJar` to create the jar file for the release. It'll be located under `build/libs`

## Running J-BeFunge

`java -jar jbefunge-1.0-SNAPSHOT-all.jar -f hello.bf` will execute a befunge 93 file with a tick rate of 1 tick per second. This can be adjusted with the `-t` argument.

## Debugging

JBefunge supports a debug mode with `-d true` or `--debug true` where output is buffered and displayed after the program finishes execution. during this time you will see the cursor moving as a `0` through the application.
![Hello, World Demo](https://github.com/jharrison902/jbefunge/raw/master/hello_demo_befunge.gif)

Additionally there is `-p` or `--printTrace` which will show the operations as they occur so you can track the flow of your program.

## Known Issues/Disclaimer

I have not extensively tested this interpreter. There may be bugs and errors. Use at your own risk.

## Road Map

Ideally, I'd like to say getting Funge-98 working will be as easy as Befunge-93. I've moved the Funge-98 work to the 2.0 branch. 1.x will be Befunge-93 specs. Personally, I'm enjoying the work on the 93 spec right now and will likely add some stuff to it before the re-write for 98 spec support.

### Feature Goals (1.x)

* Make the API friendlier for embedding (in case anyone would want to do that?)
* Add some extensions (Maybe an off-shoot from 1.x? Odd versions 93 spec, even 98?)
  * Would likely be version 3.x using `x` to enable extensions
* Improve debugging
* Change default values to something more meaningful

### Feature Goals (2.x)

* Implement...

### Feature Goals (3.x)

* Add Extension commands via `x`
  * For example `xo"txt.olleh""!dlrow ,olleh"xw` to write `hello, world!` to a file `hello.txt`
* GUI Support? Seems to be all the rage these days...
