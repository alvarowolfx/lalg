# LALG
This is a compiler project that I develop when I was doing Computer Science at UFMT. The LALG language it's a academic proposal to study Compilers.

The language specification can be found on the docs folder, together with some documentation that I generated to help on developing the compiler.

The interesting part of LALG it's a compiled language that generate some "bytecode" to be executed on a Virtual Machine, that can be found on the lalg-vm project on this repo.

Some code examples can be found on the examples folder.

## Running
To run this project you should have at least Java 7 to run the compiler and the virtual machine.

There's two ways to compile LALG code:

* Using the lalg-compiler CLI to compile a LALG code on the terminal. This way you will generate the .blalg file to be executed by the virtual machine.
```bash
$ java -jar dist/lalg-compiler.jar examples/testeProcedures.lalg
```
* Using the lalg-ide and compiling your code and saving the compiled code to run on the virtual machine.
```bash
$ java -jar dist/lalg-ide.jar
```

Next you have to run the code on the virtual machine, but unfortunatly there's no CLI yet, so you have to open the GUI application "lalg-vm" and open your .blalg file to execute.
```bash
$ java -jar dist/lalg-vm.jar
```

## Building
You can build the project yourself with Maven. I made a bash script that simple build the project and copy to the dist folder.
```bash
$ ./build.sh
```
Or build directly with Maven, but the jars will be on the target folders of each project.
```bash
$ mvn clean package
```