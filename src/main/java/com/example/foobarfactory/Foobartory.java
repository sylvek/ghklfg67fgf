package com.example.foobarfactory;

import com.example.foobarfactory.human.Darlene;
import com.example.foobarfactory.product.Bar;
import com.example.foobarfactory.product.Coin;
import com.example.foobarfactory.product.Foo;
import com.example.foobarfactory.product.FooBar;
import com.example.foobarfactory.robot.SimpleStupidRobot;
import org.assertj.core.util.Lists;
import org.assertj.core.util.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Foobartory {

    private static final Logger LOGGER = LoggerFactory.getLogger(Foobartory.class);
    private static final long TICK_IN_MS = 1L;

    private final AtomicLong tick = new AtomicLong();

    private final Human human;
    private final List<Robot> robots;
    private final List<Product> products;
    private final int maxNumberOfRobots;

    public Foobartory(int maxNumberOfRobots, Human human, List<Robot> robots, List<Product> products) {
        this.maxNumberOfRobots = maxNumberOfRobots;
        this.human = human;
        this.robots = robots;
        this.products = products;
    }

    public void operate() throws InterruptedException {
        while (running()) {
            oneTick();
            process();
        }
    }

    @VisibleForTesting
    void process() {
        // finds Robots ready to work, collect products an order new actions
        robots.stream()
                .filter(robot -> !robot.isWorking())
                .forEach(robot -> {
                    robot.collectProducts().forEach(products::add);
                    human.order(robot, products);
                });

        // if a Robot was bought, we move it on the foobartory
        var newRobots = products.stream()
                .filter(product -> product instanceof Robot)
                .map(product -> (Robot) product)
                .collect(Collectors.toList());
        newRobots.forEach(robots::add);
        newRobots.forEach(products::remove);
    }

    private void oneTick() throws InterruptedException {
        var currentTick = tick.incrementAndGet();
        LOGGER.debug("tick: {}", currentTick);
        Thread.sleep(TICK_IN_MS);
    }

    @VisibleForTesting
    boolean running() {
        var totalRobots = robots.size();
        if (LOGGER.isInfoEnabled()) {
            var foobars = products.stream().filter(product -> product instanceof FooBar).count();
            var foo = products.stream().filter(product -> product instanceof Foo).count();
            var bar = products.stream().filter(product -> product instanceof Bar).count();
            var coin = products.stream().filter(product -> product instanceof Coin).count();
            LOGGER.info("totalRobots: {}, foobars: {}, coin: {}, foo: {}, bar: {}",
                    totalRobots,
                    foobars,
                    coin,
                    foo,
                    bar);
        }

        return totalRobots < this.maxNumberOfRobots;
    }

    public static void main(String[] args) throws InterruptedException {
        LOGGER.info("Hello.");

        var foobartory = new Foobartory(
                30,
                new Darlene(),
                Lists.newArrayList(
                        new SimpleStupidRobot(UUID.randomUUID().toString(), Action.nothing),
                        new SimpleStupidRobot(UUID.randomUUID().toString(), Action.nothing)),
                Lists.newArrayList());
        foobartory.operate();

        LOGGER.info("ciao.");
        System.exit(0);
    }
}
