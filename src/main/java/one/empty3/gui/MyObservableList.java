package one.empty3.gui;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by manue on 01-07-19.
 */
public class MyObservableList<T> extends ArrayList<T> implements ObservableList<T> {
    @Override
    public void addListener(ListChangeListener<? super T> listener) {

    }

    @Override
    public void removeListener(ListChangeListener<? super T> listener) {

    }

    @Override
    public boolean addAll(T[] elements) {
        return false;
    }

    @Override
    public boolean setAll(T[] elements) {
        return false;
    }

    @Override
    public boolean setAll(Collection<? extends T> col) {
        return false;
    }

    @Override
    public boolean removeAll(T[] elements) {
        return false;
    }

    @Override
    public boolean retainAll(T[] elements) {
        return false;
    }

    @Override
    public void remove(int from, int to) {

    }

    @Override
    public void addListener(InvalidationListener listener) {

    }

    @Override
    public void removeListener(InvalidationListener listener) {

    }
}