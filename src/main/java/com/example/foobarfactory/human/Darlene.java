package com.example.foobarfactory.human;

import com.example.foobarfactory.Human;
import com.example.foobarfactory.Product;
import com.example.foobarfactory.Robot;
import com.example.foobarfactory.list.PickList;
import com.example.foobarfactory.product.Bar;
import com.example.foobarfactory.product.Coin;
import com.example.foobarfactory.product.Foo;
import com.example.foobarfactory.product.FooBar;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.example.foobarfactory.Action.*;

public class Darlene implements Human {

    @Override
    public boolean order(Robot robot, List<Product> products) {

        var serialNumber = UUID.randomUUID().toString();
        var picker = new PickList<>(products);

        // if we have what is needed to buy a robot...
        if (picker.contains(Foo.class) >= 6 && picker.contains(Coin.class) >= 3) {
            var foos = picker.pick(6, Foo.class);
            var coins = picker.pick(3, Coin.class);
            return robot.execute(buy, serialNumber, Stream.concat(coins.stream(), foos.stream()).toArray(Product[]::new)) > 0;
        }

        // it seems, what we don't have enough foos,
        if (picker.contains(Coin.class) >= 3) {
            return robot.execute(mine_foo, serialNumber) > 0;
        }

        // otherwise, we are trying to sell FooBars.
        var foobars = picker.pick(5, FooBar.class);
        if (!foobars.isEmpty()) {
            return robot.execute(sell, serialNumber, foobars.toArray(Product[]::new)) > 0;
        }

        // At least, we need foobar
        if (picker.contains(Foo.class) > 0 && picker.contains(Bar.class) > 0) {
            var foo = picker.pickFirst(Foo.class);
            var bar = picker.pickFirst(Bar.class);
            return robot.execute(merge_foo_and_bar, serialNumber, foo.get(), bar.get()) > 0;
        }

        if (picker.contains(Bar.class) > 0) {
            return robot.execute(mine_foo, serialNumber) > 0;
        }

        return robot.execute(mine_bar, serialNumber) > 0;
    }
}
