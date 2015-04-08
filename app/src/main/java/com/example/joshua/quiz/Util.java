package com.example.joshua.quiz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Utility functions
 */
public class Util {
    public static final int MILLISECOND_TO_SECOND = 1000;
    public static final int SECOND_TO_MINUTE = 60;

    /**
     * Copy the list and randomize the copied list
     *
     * @param input the original list
     * @return the copied list in randomized order
     */
    public static <T> List<T> copyAndShuffle(Collection<T> input) {
        List<T> copy = new ArrayList<>(input);
        Collections.shuffle(copy);
        return copy;
    }
}
