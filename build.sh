#!/bin/bash

mvn clean package

cp lalg-core/target/*.jar dist/lalg-core.jar
cp lalg-compiler/target/*-with-dependencies.jar dist/lalg-compiler.jar
cp lalg-vm/target/*-with-dependencies.jar dist/lalg-vm.jar
cp lalg-ide/target/*-with-dependencies.jar dist/lalg-ide.jar