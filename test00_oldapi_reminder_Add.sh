#!/bin/bash
git pull
git commit -a -m "auto commit"
git push
git status
java -ea -Dfile.encoding=UTF-8 -classpath \
./target/test-classes:\
./target/classes:\
./lib/* \
com.intellij.rt.execution.junit.JUnitStarter \
-ideVersion5 \
-junit5 \
tv.zodiac.dev.testAMS_oldAPI_Performance,\
test00_Add\(java.lang.String,java.lang.String,java.lang.String,int,int,long,long,int\)
