# [how-to-structure-java-projects](https://github.com/sombriks/how-to-structure-java-projects)

Sampling some project layouts to present in this article.

## Disclaimer

These projects are versioning jar and class files, **don't do that** in your
real project. 

It's bad because it can generate annoying git conflicts due their derived nature.

You usually have at least those lines on your `.gitignore` file:

```.gitignore
*.class
*.jar
bin
out
build
target
*.log
```

## 00-no-structure

Just your single Java file with code inside. Zero drama but does not scale.

```bash
cd 00-no-structure
java HelloThere.java
```

- create a folder
- create your Java file
- compile/run your file

## 01-some-files

A few Java files separating your code and concerns. Needs a better command line
to compile.

```bash
cd 01-some-files
javac HelloThere.java
java HelloThere
```

- create a folder
- create your files
- compile your sources (by compiling the entrypoint all other files will be compiled too)
- run your entrypoint (the class containing the main method)

## 02-source-dest-folders

Adding the idea of source and dest folders. Avoid possible collision with
produced artifacts.

```bash
cd 02-source-dest-folders
javac -d bin -cp bin src/Grievous.java
javac -d bin -cp bin src/HelloThere.java
java -cp bin HelloThere
```

- create a folder
- create src and bin folders
- create your files under src folder
- compile your secondary classes first. Use that special command line
- compile your entry point
- run your entry point indicating the bin folder as classpath

The project folder now acts as execution point, current working directory, among
other names.

## 03-package-as-jar

The java way (:tm:) to distribute programs.

```bash
cd 03-package-as-jar
javac -d bin -cp bin src/Grievous.java
javac -d bin -cp bin src/HelloThere.java
jar cvf target/star-wars.jar -C bin . 
java -cp target/star-wars.jar HelloThere
```

- create a folder
- create src and bin folders
- create your files under src folder
- compile your secondary classes first. Use that special command line
- compile your entry point
- package your classes using the **jar** command
- run your entry point indicating the jar file as classpath

The `-C bin` part of this command directs the Jar tool to go to the bin
directory, and the . following -C bin directs the Jar tool to archive all the
contents of that directory.

Another approach is to define a **Main-Class** to the jar so anyone running the
package will need little knowledge of how your program work internally:

```bash
cd 03-package-as-jar
javac -d bin -cp bin src/Grievous.java
javac -d bin -cp bin src/HelloThere.java
jar cvfe target/star-wars.jar HelloThere -C bin . 
java -jar target/star-wars.jar
```

## 04-managing-dependencies

Using code from others, so you don't reinvent the wheel.

Let's say we want to use
[the Gson library](https://search.maven.org/artifact/com.google.code.gson/gson)
to parse json data:

```bash
cd 04-managing-dependencies
javac -d bin -cp bin src/Item.java
javac -d bin -cp bin src/Quotes.java
javac -d bin -cp bin:lib/gson-2.10.1.jar src/Grievous.java
javac -d bin -cp bin src/HelloThere.java
jar cvfe target/star-wars-2.jar HelloThere -C bin . 
java -cp lib/gson-2.10.1.jar:target/star-wars-2.jar HelloThere
```

Now our command line to compile and run must take proper care of classpath
composition.

One alternative way to do that is to maintain a `MANIFEST.MF` file providing
both **Main-Class** and **Class-Path** information.
Set this file under [src/META-INF/MANIFEST.MF](04-managing-dependencies/src/META-INF/MANIFEST.MF)

```manifest
Manifest-Version: 1.0
Main-Class: HelloThere
Class-Path: gson-2.10.1.jar


```

_the two final empty lines are kinda important_.

The build process will look pretty much the same, but once again we got able to
hide some internals:

```bash
cd 04-managing-dependencies
javac -d bin -cp bin src/Item.java
javac -d bin -cp bin src/Quotes.java
javac -d bin -cp bin:lib/gson-2.10.1.jar src/Grievous.java
javac -d bin -cp bin src/HelloThere.java
jar cvfm target/star-wars-2.jar src/META-INF/MANIFEST.MF -C bin .
cp lib/gson-2.10.1.jar target/gson-2.10.1.jar
java -jar target/star-wars-2.jar
```

Note that since the `Class-Path` is calculated from the jar location and not
from the point of execution, we must moe our 3rd-party library from lib folder
to target folder.

And **YES**, too much effort for just **one** dependency. 
Imagine having hundreds of them, like enterprise java projects have!

## 05-ant-project

First attempt to have a higher level project configuration for java projects.

```bash
```

## 06-maven-project

When things got complicated

```bash
```

## 07-gradle-project

When stuff went full madness.

```bash
```

## 08-packaging-as-docker-image 

That one isn't a Java thing, it just what everything nowadays looks like.

```bash
```

## Conclusion

No one needs to start right up into complex tools for java projects. On the
other hand, it's very important to understand the value of such tools and how
things end up like this and, more important, what opportunities all those
indirection levels bring up.

One thing not even mentioned on this project was IDE-specific configurations.
Those ones has their own history and are worth to know and understand, but that
topic will be visited in another moment. Eventually.
