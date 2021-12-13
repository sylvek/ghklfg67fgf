package com.example.foobarfactory;

import java.util.List;

public interface Robot {

    boolean isWorking();

    int execute(Action action, String serialNumber, Product... supplies);

    List<Product> collectProducts();
}
