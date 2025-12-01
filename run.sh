#!/bin/bash
echo "Compiling Note App..."

cd src/main/java

javac -d ../../../build/classes com/noteapp/model/Note.java
javac -d ../../../build/classes com/noteapp/service/NoteManager.java
javac -d ../../../build/classes com/noteapp/gui/NoteAppGUI.java
javac -d ../../../build/classes com/noteapp/NoteAppMain.java

cd ../../..

echo "Running Note App..."
java -cp build/classes com.noteapp.NoteAppMain
