/*
 * Copyright (c) 1997, 2014, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.xenon.util;

import com.xenon.util.readability.Hook;

import java.util.*;

/**
 * This is a mere copy-paste of Collections.shuffle used in some structure gen MC classes.
 *
 * @author Josh Bloch
 * @author Neal Gafter
 * @see java.util.Collections
 */
public class XenonCollections {


    private static final int SHUFFLE_THRESHOLD = 5;

    /**
     * @param list
     * @param rnd
     * @see java.util.Collections#shuffle(List, Random)
     */
    @Hook("net.minecraft.world.gen.structure.StructureOceanMonumentPieces#func_175836_a() -> line 870")
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void shuffle(List<?> list, PRNG rnd) {
        int size = list.size();
        if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
            for (int i = size; i > 1; i--)
                Collections.swap(list, i - 1, rnd.nextInt(i));
        } else {
            Object[] arr = list.toArray();

            // Shuffle array
            for (int i = size; i > 1; i--)
                swap(arr, i - 1, rnd.nextInt(i));

            // Dump array back into list
            // instead of using a raw type here, it's possible to capture
            // the wildcard but it will require a call to a supplementary
            // private method
            ListIterator it = list.listIterator();
            for (Object o : arr) {
                it.next();
                it.set(o);
            }
        }
    }

    private static void swap(Object[] arr, int i, int j) {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
