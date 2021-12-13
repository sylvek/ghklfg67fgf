# FoobarTory

## Introduction

FoobarTory is a factory building Robots.
Darlene controls it and orders the construction of 30 robots.

## Implementation details

To be more specific, this program starts with `Foobartory.main()`.
Two `SimpleStupidRobots` are created _(their initial state is `nothing` and their first step will cost 50 ticks)_.

`Tick` is a step and "costs" 1 millisecond of time.
Every Tick, `Footbartory` select available `Robots` and `Human` processes actions on it.

Every time a `SimpleStupidRobot.isWorking()` is called, we decrease the number of `Tick` needed to finish the work.

`Robots` executes an `Action` and returns a `Product`, when work is done.

Those `Products` are `Foo`, `Bar`, `FooBar` but also, `Coin` and `Robot`.

Finally, bought `Robots` are moved in `Footbartory` and used at the next `Tick`.

## How to run it?

It's more suitable to use docker.
Tests are played anytime you build the image.

```
docker build -t foobartory .
docker run --rm foobartory
```

Also, you could run code with Maven and Java.

```
mvn package
java -jar target/foobartory-1.0-SNAPSHOT.jar
```