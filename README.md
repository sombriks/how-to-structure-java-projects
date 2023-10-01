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

You must create a [build.xml](05-ant-project/build.xml) file.

```bash
cd 05-ant-project
ant -v
java -jar target/star-wars-3.jar
```

Command line way cleaner, all complexity hidden inside another config file.

But the problem is mostly moved to under the carpet, we still need to deal with
large amounts of dependencies as the project grows, and suddenly a new problem
appears: the proper version among dependencies inside lib folder. 

Also, files that aren't exactly source code appears in the project and we need
to deal with them as well.

## 06-maven-project

Maven is when things get a little complicated. It is more declarative than
imperative (ant has his tasks grouped inside targets) and enforces a very
specific folder structure. It needs internet to work properly. It takes control.

For instance, maven has one of the ugliest command lines ever conceived to init
an empty project:

```bash
mvn archetype:generate -DinteractiveMode=false \
                       -DgroupId=sample.structure \
                       -DartifactId=06-maven-project \
                       -DarchetypeGroupId=org.apache.maven.archetypes \
                       -DarchetypeArtifactId=maven-archetype-quickstart \
                       -DarchetypeVersion=1.4
```

And we kinda lose the simple command line to execute the program. It becomes
this abomination:

```bash
cd 06-maven-project
mvn clean package
java -cp ~/.m2/repository/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar:target/06-maven-project-1.0-SNAPSHOT.jar sample.structure.HelloThere
```

You can add the exec plugin to try to make things a little more bearable:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>sample.structure</groupId>
  <artifactId>06-maven-project</artifactId>
  <version>1.0-SNAPSHOT</version>

  <name>06-maven-project</name>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
  </properties>

  <dependencies>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>2.10.1</version>
    </dependency>

    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <version>5.9.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>exec-maven-plugin</artifactId>
        <version>3.1.0</version>
        <executions>
          <execution>
            <goals>
              <goal>java</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <mainClass>sample.structure.HelloThere</mainClass>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

With this plugin, run the application becomes a very easy task:

```bash
cd 06-maven-project
mvn clean package exec:java
```

## 07-gradle-project

When stuff went full madness.

```bash
cd 07-gradle-project
gradle init \
  --type java-application \
  --dsl groovy \
  --test-framework junit-jupiter \
  --package sample.structure \
  --project-name 07-gradle-project \
  --no-split-project
```

Note that this command line isn't enough and it still enters into interactive
mode and asks additional configs. 

The built project follows most conventions invented by maven. It is this way to
make things easier to you to import an existing maven project.

The generated project layout is something as well:

```bash
07-gradle-project
├── app
│   ├── build.gradle
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── sample
│       │   │       └── structure
│       │   │           └── App.java
│       │   └── resources
│       └── test
│           ├── java
│           │   └── sample
│           │       └── structure
│           │           └── AppTest.java
│           └── resources
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── messages.json
└── settings.gradle
```

It not only added the [wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html)
plugin but also defined a [submodule](https://docs.gradle.org/current/userguide/multi_project_builds.html)
to house the application code.

And last but not least important, the [build.gradle](07-gradle-project/app/build.gradle)
file which is the gradle equivalent to a `pom.xml` file:

```groovy
plugins {
    id 'application'
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.9.3'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'

    implementation 'com.google.guava:guava:32.1.1-jre'
    implementation 'com.google.code.gson:gson:2.10.1'
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = 'sample.structure.HelloThere'
    // to fix an issue with the submodule
    tasks.run.workingDir = rootProject.projectDir
}

tasks.named('test') {
    useJUnitPlatform()
}
```

It has the same declarative principle that a maven pom.xml has, but it uses a
full-featured script language. Because.

You run your project with this command:

```bash
cd 07-gradle-project
./gradlew run
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
