package com.example.foobarfactory.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PickList<T> {

    private final List<T> list;

    public PickList(List<T> list) {
        this.list = list;
    }

    public long contains(Class<? extends T> tClass) {
        return this.list.stream().filter(tClass::isInstance).count();
    }

    public Optional<T> pickFirst(Class<? extends T> tClass) {
        var elements = this.list.stream().filter(tClass::isInstance).collect(Collectors.toList());
        if (!elements.isEmpty()) {
            var element = elements.get(0);
            if (this.list.remove(element)) {
                return Optional.of(element);
            }
        }
        return Optional.empty();
    }

    public List<T> pick(int numberOfElement, Class<? extends T> tClass) {
        var elements = this.list.stream().filter(tClass::isInstance).collect(Collectors.toList());
        var subList = new ArrayList<T>();
        if (elements.size() >= numberOfElement) {
            elements.subList(0, numberOfElement).forEach(element -> subList.add(pickFirst(tClass).get()));
        }
        return subList;
    }
}
