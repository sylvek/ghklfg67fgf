package com.example.foobarfactory.robot;

import com.example.foobarfactory.Robot;
import com.example.foobarfactory.product.Bar;
import com.example.foobarfactory.product.Coin;
import com.example.foobarfactory.product.Foo;
import com.example.foobarfactory.product.FooBar;
import org.junit.jupiter.api.Test;

import static com.example.foobarfactory.Action.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SimpleStupidRobotTests {

    @Test
    public void should_build_a_foo_from_nothing_for_50_plus_10_ticks() {
        // given
        var a_robot = new SimpleStupidRobot("", nothing);

        // when
        var ticks = a_robot.execute(mine_foo, "serial_number");

        // then
        assertThat(ticks).isEqualTo(60);

        // a_robot will work 59 ticks
        for (int i = 0; i < 59; i++) {
            assertThat(a_robot.isWorking()).isTrue();
            assertThat(a_robot.collectProducts()).isEmpty();
        }

        // a_robot will be available at 60th tick
        assertThat(a_robot.isWorking()).isFalse();
        assertThat(a_robot.collectProducts().get(0).toString()).isEqualTo("serial_number");
        assertThat(a_robot.collectProducts()).isEmpty();
    }

    @Test
    public void should_build_a_second_foo_for_10_ticks() {
        // given
        var a_robot = new SimpleStupidRobot("", mine_foo);

        // when
        var ticks = a_robot.execute(mine_foo, "serial_number");

        // then
        assertThat(ticks).isEqualTo(10);
    }

    @Test
    public void should_build_a_second_bar_for_5_to_20_ticks() {
        // given
        var a_robot = new SimpleStupidRobot("", mine_bar);

        // when
        var ticks = a_robot.execute(mine_bar, "serial_number");

        // then
        assertThat(ticks).isBetween(5, 20);
    }

    @Test
    public void should_build_a_foobar_if_supplies_contains_a_foo_and_a_bar() {
        // given
        var a_robot = new SimpleStupidRobot("", nothing);
        a_robot.enableRandomize(false);

        // when
        var ticks = a_robot.execute(merge_foo_and_bar, "foobar_serial_number",
                new Foo("foo"), new Bar("bar"));

        // then
        assertThat(ticks).isEqualTo(20 + 50);

        for (int i = 0; i < 70; i++) {
            a_robot.isWorking();
        }

        var product = a_robot.collectProducts().get(0);
        assertThat(product.toString()).isEqualTo("foobar_serial_number+foo+bar");
    }

    @Test
    public void should_throw_exception_if_foobar_build_does_not_contain_supply() {
        // given
        var a_robot = new SimpleStupidRobot("", nothing);

        // when
        assertThatThrownBy(() -> a_robot.execute(merge_foo_and_bar, "foobar_serial_number"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_throw_exception_if_foobar_build_does_not_contain_a_foo() {
        // given
        var a_robot = new SimpleStupidRobot("", nothing);

        // when
        assertThatThrownBy(() -> a_robot.execute(merge_foo_and_bar, "foobar_serial_number", new Bar("bar")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_throw_exception_if_foobar_build_does_not_contain_a_bar() {
        // given
        var a_robot = new SimpleStupidRobot("", nothing);

        // when
        assertThatThrownBy(() -> a_robot.execute(merge_foo_and_bar, "foobar_serial_number", new Foo("foo")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void should_return_three_coins() {
        // given
        var a_robot = new SimpleStupidRobot("", nothing);

        // when
        var ticks = a_robot.execute(sell,
                "sold",
                new FooBar("foobar1"),
                new FooBar("foobar2"),
                new FooBar("foobar3"));

        // then
        for (int i = 0; i < ticks; i++) {
            a_robot.isWorking();
        }

        var products = a_robot.collectProducts();
        assertThat(products.size()).isEqualTo(3);
        for (int i = 0; i < products.size(); i++) {
            assertThat(products.get(i).toString()).isEqualTo("sold:" + i);
        }
    }

    @Test
    public void should_return_a_robot() {
        // given
        var a_robot = new SimpleStupidRobot("", nothing);

        // when
        var ticks = a_robot.execute(buy,
                "buy",
                new Coin("coin1"),
                new Coin("coin2"),
                new Coin("coin3"),
                new Foo("foo1"),
                new Foo("foo2"),
                new Foo("foo3"),
                new Foo("foo4"),
                new Foo("foo5"),
                new Foo("foo6"));

        // then
        for (int i = 0; i < ticks; i++) {
            a_robot.isWorking();
        }

        var products = a_robot.collectProducts();
        assertThat(products.size()).isEqualTo(1);
        assertThat(products.get(0)).isInstanceOf(Robot.class);
    }
}
