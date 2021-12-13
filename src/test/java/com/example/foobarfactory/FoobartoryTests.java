package com.example.foobarfactory;

import com.example.foobarfactory.robot.SimpleStupidRobot;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.example.foobarfactory.Action.nothing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FoobartoryTests {

    @Test
    public void should_continue_if_there_is_not_the_maximum_of_robots() {
        // given
        var human = mock(Human.class);
        var foobartory = new Foobartory(1, human, List.of(), List.of());

        // when
        var result = foobartory.running();

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void should_stop_if_there_is_the_maximum_of_robots() {
        // given
        var human = mock(Human.class);
        var robot = mock(Robot.class);
        var foobartory = new Foobartory(1, human, List.of(robot), List.of());

        // when
        var result = foobartory.running();

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void should_process_two_robots() throws InterruptedException {
        // given
        var human = mock(Human.class);
        var robot1 = mock(Robot.class);
        var robot2 = mock(Robot.class);
        var products = new ArrayList<Product>();
        var foobartory = new Foobartory(0, human, List.of(robot1, robot2), products);

        // when
        foobartory.process();

        // then
        verify(human).order(robot1, products);
        verify(human).order(robot2, products);
    }

    @Test
    public void should_add_a_new_robot() {
        // given
        var human = mock(Human.class);
        var robot1 = new SimpleStupidRobot("", nothing);
        var robots = new ArrayList<Robot>();
        var products = new ArrayList<Product>();
        products.add(robot1);
        var foobartory = new Foobartory(0, human, robots, products);

        // when
        foobartory.process();

        // then
        assertThat(robots).contains(robot1);
        assertThat(products).isEmpty();
    }
}
