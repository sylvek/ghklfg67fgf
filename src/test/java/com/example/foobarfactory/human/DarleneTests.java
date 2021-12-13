package com.example.foobarfactory.human;

import com.example.foobarfactory.Product;
import com.example.foobarfactory.Robot;
import com.example.foobarfactory.product.Bar;
import com.example.foobarfactory.product.Coin;
import com.example.foobarfactory.product.Foo;
import com.example.foobarfactory.product.FooBar;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.foobarfactory.Action.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DarleneTests {

    @Test
    public void should_do_nothing() {
        // given
        var robot = mock(Robot.class);
        var human = new Darlene();

        // when
        var result = human.order(robot, List.of());

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void should_order_to_mine_a_bar() {
        // given
        var robot = mock(Robot.class);
        var human = new Darlene();

        // when
        var result = human.order(robot, List.of());

        // then
        verify(robot).execute(eq(mine_bar), anyString());
    }

    @Test
    public void should_order_to_build_foo() {
        // given
        var robot = mock(Robot.class);
        var elliot = new Darlene();

        // when
        elliot.order(robot, List.of(new Bar("serialNumber")));

        // then
        verify(robot).execute(eq(mine_foo), anyString());
    }

    @Test
    public void should_order_to_build_foobar_and_remove_foo_and_bar() {
        // given
        var robot = mock(Robot.class);
        var elliot = new Darlene();

        // when
        var foo = new Foo("serialNumberFoo");
        var bar = new Bar("serialNumberBar");
        var products = Lists.newArrayList(foo, bar);
        elliot.order(robot, products);

        // then
        verify(robot).execute(eq(merge_foo_and_bar), anyString(), eq(foo), eq(bar));
        assertThat(products).isEmpty();
    }

    @Test
    public void should_sell_5_foobars() {
        // given
        var robot = mock(Robot.class);
        var elliot = new Darlene();

        // when
        var foobar1 = new FooBar("serialNumberFooBar1");
        var foobar2 = new FooBar("serialNumberFooBar2");
        var foobar3 = new FooBar("serialNumberFooBar3");
        var foobar4 = new FooBar("serialNumberFooBar4");
        var foobar5 = new FooBar("serialNumberFooBar5");
        List<Product> products = Lists.newArrayList(foobar1, foobar2, foobar3, foobar4, foobar5);
        elliot.order(robot, products);

        // then
        verify(robot).execute(eq(sell), anyString(), eq(foobar1), eq(foobar2), eq(foobar3), eq(foobar4), eq(foobar5));
        assertThat(products).isEmpty();
    }

    @Test
    public void should_buy_a_robot_with_6foo_and_3coins() {
        // given
        var robot = mock(Robot.class);
        var elliot = new Darlene();

        // when
        var coin1 = new Coin("coin1");
        var coin2 = new Coin("coin2");
        var coin3 = new Coin("coin3");
        var foo1 = new Foo("foo1");
        var foo2 = new Foo("foo2");
        var foo3 = new Foo("foo3");
        var foo4 = new Foo("foo4");
        var foo5 = new Foo("foo5");
        var foo6 = new Foo("foo6");
        List<Product> products = Lists.newArrayList(coin1, coin2, coin3, foo1, foo2, foo3, foo4, foo5, foo6);
        elliot.order(robot, products);

        // then
        verify(robot).execute(eq(buy), anyString(), eq(coin1), eq(coin2), eq(coin3), eq(foo1), eq(foo2), eq(foo3), eq(foo4), eq(foo5), eq(foo6));
        assertThat(products).isEmpty();
    }
}
