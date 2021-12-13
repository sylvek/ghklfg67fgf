package com.example.foobarfactory.robot;

import com.example.foobarfactory.Action;
import com.example.foobarfactory.Product;
import com.example.foobarfactory.Robot;
import com.example.foobarfactory.product.Bar;
import com.example.foobarfactory.product.Coin;
import com.example.foobarfactory.product.Foo;
import com.example.foobarfactory.product.FooBar;
import org.assertj.core.util.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.foobarfactory.Action.nothing;

public class SimpleStupidRobot extends Product implements Robot {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleStupidRobot.class);

    private volatile int ticks = 0;
    private Action previousAction;
    private List<Product> product = new ArrayList<>();
    private boolean isRandomizeEnabled = true;

    public SimpleStupidRobot(String serialNumber, Action previousAction) {
        super(serialNumber);
        this.previousAction = previousAction;
    }

    @VisibleForTesting
    void enableRandomize(boolean enable) {
        this.isRandomizeEnabled = enable;
    }

    @Override
    public boolean isWorking() {
        if (ticks > 0) {
            var isWorking = --ticks > 0;
            LOGGER.debug("{} - {} more ticks", this, ticks);
            return isWorking;
        }
        return false;
    }

    @Override
    public int execute(Action action, String serialNumber, Product... supplies) {
        if (ticks == 0) {

            if (action != previousAction) {
                LOGGER.debug("{} is moving (+50 ticks)", this);
                ticks += 50;
                this.previousAction = action;
            }

            switch (action) {
                case mine_bar:
                    this.ticks += Math.random() * (20 - 5 + 1) + 5;
                    this.product.add(new Bar(serialNumber));
                    break;
                case mine_foo:
                    this.ticks += 10;
                    this.product.add(new Foo(serialNumber));
                    break;
                case merge_foo_and_bar:
                    this.ticks += 20;

                    if (supplies == null
                            || supplies.length != 2
                            || Arrays.stream(supplies).noneMatch(foo -> foo instanceof Foo)
                            || Arrays.stream(supplies).noneMatch(bar -> bar instanceof Bar)) {
                        throw new IllegalArgumentException("Foo and Bar have to be supplied");
                    }

                    // 60% of success, only
                    if (shouldFail()) {
                        // failure !
                        LOGGER.warn("{} too bad! merging FooBar will fail", this);
                        this.product.add(Arrays.stream(supplies).filter(bar -> bar instanceof Bar).collect(Collectors.toList()).get(0));
                    } else {
                        this.product.add(new FooBar(serialNumber + "+" + supplies[0] + "+" + supplies[1]));
                    }
                    break;
                case sell:
                    this.ticks += 100;

                    if (supplies == null
                            || supplies.length == 0
                            || supplies.length > 5
                            || !Arrays.stream(supplies).allMatch(foobar -> foobar instanceof FooBar)) {
                        throw new IllegalArgumentException("1 to 5 FooBar have to be supplied");
                    }
                    for (int i = 0; i < supplies.length; i++) {
                        this.product.add(new Coin(serialNumber + ":" + i));
                    }
                    break;
                case buy:
                    this.ticks += 0;

                    if (supplies == null
                            || supplies.length != 9
                            || Arrays.stream(supplies).filter(p -> p instanceof Coin).count() != 3
                            || Arrays.stream(supplies).filter(p -> p instanceof Foo).count() != 6) {
                        throw new IllegalArgumentException("3 Coin and 6 Foo have to be supplied");
                    }
                    this.product.add(new SimpleStupidRobot(serialNumber, nothing));
                    break;
                case nothing:
                    this.ticks += 0;
                    this.product = null;
                    break;
                default:
                    this.ticks = 0;
                    break;
            }
        }

        LOGGER.info("{} is working on {} for {} ticks with serialNumber: {} and supplies: {}", this, action, ticks, serialNumber, supplies);
        return ticks;
    }

    @VisibleForTesting
    boolean shouldFail() {
        return isRandomizeEnabled && Math.random() > 0.6;
    }

    @Override
    public List<Product> collectProducts() {
        if (ticks == 0) {
            var product = List.copyOf(this.product);
            this.product.clear();
            return product;
        }
        return List.of();
    }
}