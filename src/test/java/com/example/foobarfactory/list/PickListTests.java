package com.example.foobarfactory.list;

import com.example.foobarfactory.Product;
import com.example.foobarfactory.product.Bar;
import com.example.foobarfactory.product.Coin;
import com.example.foobarfactory.product.Foo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class PickListTests {

    @Test
    public void should_pick_a_dedicated_element() {
        // given
        var products = new ArrayList<Product>();
        var pickList = new PickList<>(products);

        products.add(new Foo("test1"));
        products.add(new Bar("test2"));
        assertThat(products.size()).isEqualTo(2);

        // when
        var element = pickList.pickFirst(Foo.class);

        // then
        assertThat(element).isPresent();
        assertThat(element.get()).isInstanceOf(Foo.class);
        assertThat(element.get().toString()).isEqualTo("test1");
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    public void should_return_empty_optional_if_element_not_found() {
        // given
        var products = new ArrayList<Product>();
        var pickList = new PickList<>(products);

        // when
        var element = pickList.pickFirst(Coin.class);

        // then
        assertThat(element).isNotPresent();
    }

    @Test
    public void should_pick_2_elements() {
        // given
        var products = new ArrayList<Product>();
        var pickList = new PickList<>(products);

        products.add(new Foo("test1"));
        products.add(new Foo("test2"));
        products.add(new Foo("test3"));


        // when
        var elements = pickList.pick(2, Foo.class);

        // then
        assertThat(elements.size()).isEqualTo(2);
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    public void should_pick_0_element() {
        // given
        var products = new ArrayList<Product>();
        var pickList = new PickList<>(products);

        products.add(new Foo("test1"));

        // when
        var elements = pickList.pick(2, Foo.class);

        // then
        assertThat(elements).isEmpty();
    }
}
