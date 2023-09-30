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
```

## 01-some-files

A few Java files separating your code and concerns. Needs a better command line
to compile.

```bash
```

## 02-source-dest-folders

Adding the idea of source and dest folders. Avoid possible collision with
produced artifacts.

```bash
```

## 03-package-as-jar

The java way (:tm:) to distribute programs.

```bash
```

## 04-managing-dependencies

Using code from others so you don't reinvent the wheel.

```bash
```

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
