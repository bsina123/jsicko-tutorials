/*
 * Copyright (C) 2018 Andrea Mocci and CodeLounge https://codelounge.si.usi.ch
 *
 * This file is part of jSicko - Java SImple Contract checKer.
 *
 *  jSicko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * jSicko is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jSicko.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package ch.usi.si.codelounge.jsicko.tutorials.stack;

import ch.usi.si.codelounge.jsicko.Contract;
import static ch.usi.si.codelounge.jsicko.Contract.old;
import static ch.usi.si.codelounge.jsicko.ContractUtils.*;

import java.util.Collection;
import java.util.stream.IntStream;

/**
 * A simple interface for a Stack of elements.
 * @param <T> the type for the stack elements.
 */
public interface Stack<T> extends Contract {
    
    @Invariant
    @Pure
    default boolean sizeNonNegative() {
        return size() >= 0;
    }

    /*
     * See frame_condition
     */
    @Invariant
    @Pure
    default boolean elementsNeverNull() {
        return forAllInts(0, size(), pos -> elementAt(pos) != null);
    }

    @Requires("!stack_is_empty")
    @Ensures({"returns_old_last_element", "size_decreases", "pop_frame_condition"})
    T pop();

    @Requires({"!stack_is_empty"})
    @Ensures({"returns_last_element"})
    @Pure
    T top();

    @Requires("element_not_null")
    @Ensures({"push_on_top", "size_increases", "push_frame_condition"})
    void push(T element);

    @Pure
    int size();

    @Requires("pos_is_valid")
    @Pure
    default T elementAt(int pos) {
        return null;
    }

    @Ensures("stack_is_empty")
    void clear();

    @Pure
    default boolean stack_is_empty() {
        return size() == 0;
    }

    @Pure
    default boolean pos_is_valid(int pos) {
        return pos >= 0 && pos < size();
    }

    @Pure
    default boolean element_not_null(T element) {
        return element != null;
    }

    @Pure
    default boolean returns_last_element(T returns) {
        return returns.equals(elementAt(size() - 1));
    }

    @Pure
    default boolean returns_old_last_element(T returns) {
        return returns.equals(old(this).elementAt(old(this).size() - 1));
    }

    @Pure
    default boolean size_increases() { return size() == old(this).size() + 1; }

    @Pure
    default boolean size_decreases() {
        return size() == old(this).size() - 1;
    }

    @Pure
    default boolean push_on_top(T element) {
        return top().equals(element);
    }

    @Pure
    default boolean frame_condition(int lastPos) {
        return forAllInts(0, lastPos, pos -> old(this).elementAt(pos).equals(elementAt(pos)));
    }

    @Pure
    default boolean push_frame_condition() {
        return frame_condition(old(this).size());
    }

    @Pure
    default boolean pop_frame_condition() {
        return frame_condition(size());
    }

    @Pure
    default boolean elems_not_null(Collection<T> elems) {
        return elems != null;
    }

    @Pure
    default boolean collection_initializer(Collection<T> elems) {
        return forAll(elems,elem -> existsInt(0,size(),pos -> elementAt(pos).equals(elem)));
    }

    /**
     * Returns a String representation of the Stack.
     * @return a String representation of the Stack.
     */
    @Pure
    String toString();

}
