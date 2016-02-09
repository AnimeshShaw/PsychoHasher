# PsychoHasher
A Java based utility for hashing and hash verification.

A Java based tool to generate and verify hashes for text, files, drives. It's a program which can compute the following hashes: MD5, SHA1, SHA-224, SHA-256, SHA-384 and SHA-512.

It can compute hashes for text string, individual files, no. of files, single hash for group of files. It also provides limited customization for the user to this application. After hash computation the user has the option to export the results as a TSV (Tab-Separated) file.

## Current Features

* Compute hash value of text string.
* Compute hash value of individual files or files in a folder.
* Compute Single hash for group of files or folder.

## Screenshots

__1. Startscreen__

![](http://i.imgur.com/MJqUJbY.png)

__2. HashText__

![](http://i.imgur.com/MJTW0V4.png)

__3. Hash Files [Single/Multiple/Folder]__

![](http://i.imgur.com/nGm4I1H.png)

![](http://i.imgur.com/eE8os6t.png)

__4. Single hash for group of files__

![](http://i.imgur.com/Klp9ivm.png)

![](http://i.imgur.com/NFhJQKT.png)

## Building project from source

Before we proceed further I am assuming that you have **git** installed and in system path, and **JAVA_HOME** variable is set.

**Step 1: Clone the repository from github**

    git clone https://github.com/AnimeshShaw/PsychoHasher.git

![](http://i.imgur.com/131Zccs.png)

**Step 2: Build the project**

The project was built with gradle, so if you have gradle installed in your system and the path is set then you can simple build as follows:

    gradle build

Even if you don't have gradle, don't worry the project comes along with wrappers for both windows [gradlew.bat] and linux [gradlew]. Simply execute the following [in case of windows] and let it do its magic

    gradlew.bat build

**Step 3: Run the project**

Now its time to run our application. Simply execute the following:

    gradlew.bat run

Observe the screenshot to understand better.

![](http://i.imgur.com/TicXQDB.png)

## ToDo

* Add an option to compute hash for logical volumes.
* Add Verify hashes section.

