[![Build Status](https://travis-ci.org/Francesco149/koohii.svg?branch=master)](https://travis-ci.org/Francesco149/koohii)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.francesco149/koohii/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.francesco149/koohii)

osu! pp and difficulty calculator. pure java implementation of
https://github.com/Francesco149/oppai-ng .

this is meant to be a small (~1500 LOC including documentation)
standalone single-file library that's as portable as possible,
using only java built-ins without any extra dependency.

it works even on legacy deprecated runtimes such as jamvm (which is
what I developed it on).

please don't come ask me for more java projects or associate me
with it. I don't like the language and I was half-memeing with
this project although it's fully functional.

# usage
just drop Koohii.java into your project's folder and you are ready
to go.

on a linux system (or git bash for windows) it can be done in two
commands like so:
```sh
cd my/project
curl https://raw.githubusercontent.com/Francesco149/koohii/master/src/main/java/com/github/francesco149/koohii/Koohii.java -O
```

for full documentation, read Koohii.java or

```
javadoc Koohii.java
```

to generate HTML documentation.

If you know any plain text javadoc generator or anything that can
be browsed without a GUI or third party software please let me know
and I will add instructions.

if you prefer using maven, koohii is now also available on
maven central, add this to the ```<dependencies>``` section of your
```pom.xml```:

```xml
    <dependency>
      <groupId>com.github.francesco149</groupId>
      <artifactId>koohii</artifactId>
      <version>1.2.0</version>
    </dependency>
```

# example
```java
package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.github.francesco149.koohii.*;

class Example {

public static
void main(String[] args) throws java.io.IOException
{
    BufferedReader stdin =
        new BufferedReader(new InputStreamReader(System.in)
    );

    Koohii.Map beatmap = new Koohii.Parser().map(stdin);
    Koohii.DiffCalc stars = new Koohii.DiffCalc().calc(beatmap);
    System.out.printf("%s stars\n", stars.total);

    Koohii.PPv2 pp = new Koohii.PPv2(
        stars.aim, stars.speed, beatmap
    );

    System.out.printf("%s pp\n", pp.total);
}

}
```

```
javac -d . Koohii.java
javac -d . Example.java
cat /path/to/file.osu | java com.example.Example
```

old compilers might require ```-1.5``` to properly build this.

if you are stuck with jamvm, replace java with jamvm

# performance
when running the test suite, speed is roughly equivalent to the C
implementation, but peak memory usage is almost 80 times higher.
if you are on a system with limited resources or you don't want to
spend time installing and setting up java, you can use the C
implementation which doesn't depend on any third party software.

test were performed on a i7-4790k inside a qemu + kvm debian
virtual machine (since I don't want to bootstrap jdk on my daily
driver musl-based distro).

openjdk:

```sh
$ java -version
openjdk version "1.8.0_131"
OpenJDK Runtime Environment (build 1.8.0_131-8u131-b11-2-b11)
OpenJDK Server VM (build 25.131-b11, mixed mode)
$ cd ~/src/Koohii
$ javac Test.java
$ busybox time -v java Test
...
    Command being timed: "java Test"
    User time (seconds): 9.16
    System time (seconds): 0.57
    Percent of CPU this job got: 119%
    Elapsed (wall clock) time (h:mm:ss or m:ss): 0m 8.18s
    Average shared text size (kbytes): 0
    Average unshared data size (kbytes): 0
    Average stack size (kbytes): 0
    Average total size (kbytes): 0
    Maximum resident set size (kbytes): 799408
    Average resident set size (kbytes): 0
    Major (requiring I/O) page faults: 0
    Minor (reclaiming a frame) page faults: 48411
    Voluntary context switches: 3188
    Involuntary context switches: 1277
    Swaps: 0
    File system inputs: 0
    File system outputs: 1416
    Socket messages sent: 0
    Socket messages received: 0
    Signals delivered: 0
    Page size (bytes): 4096
    Exit status: 0
```

oracle jdk:

```sh
$ java -version
java version "1.8.0_144"
Java(TM) SE Runtime Environment (build 1.8.0_144-b01)
Java HotSpot(TM) Server VM (build 25.144-b01, mixed mode)
$ cd ~/src/Koohii
$ javac Test.java
$ busybox time -v java Test
...
    Command being timed: "java Test"
    User time (seconds): 9.17
    System time (seconds): 0.67
    Percent of CPU this job got: 118%
    Elapsed (wall clock) time (h:mm:ss or m:ss): 0m 8.30s
    Average shared text size (kbytes): 0
    Average unshared data size (kbytes): 0
    Average stack size (kbytes): 0
    Average total size (kbytes): 0
    Maximum resident set size (kbytes): 796384
    Average resident set size (kbytes): 0
    Major (requiring I/O) page faults: 0
    Minor (reclaiming a frame) page faults: 48012
    Voluntary context switches: 3543
    Involuntary context switches: 1139
    Swaps: 0
    File system inputs: 0
    File system outputs: 1424
    Socket messages sent: 0
    Socket messages received: 0
    Signals delivered: 0
    Page size (bytes): 4096
    Exit status: 0
```

oppai-ng (C version):

```sh
$ cd ~/src/oppai-ng/test/
$ ./build
$ time -v ./oppai_test
...
    Command being timed: "./oppai_test"
    User time (seconds): 9.09
    System time (seconds): 0.06
    Percent of CPU this job got: 99%
    Elapsed (wall clock) time (h:mm:ss or m:ss): 0m 9.15s
    Average shared text size (kbytes): 0
    Average unshared data size (kbytes): 0
    Average stack size (kbytes): 0
    Average total size (kbytes): 0
    Maximum resident set size (kbytes): 11840
    Average resident set size (kbytes): 0
    Major (requiring I/O) page faults: 0
    Minor (reclaiming a frame) page faults: 304
    Voluntary context switches: 1
    Involuntary context switches: 39
    Swaps: 0
    File system inputs: 0
    File system outputs: 0
    Socket messages sent: 0
    Socket messages received: 0
    Signals delivered: 0
    Page size (bytes): 4096
    Exit status: 0
```
