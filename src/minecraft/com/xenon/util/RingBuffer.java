package com.xenon.util;

public interface RingBuffer<T> {


    int size();
    void clear();
    T poll();
    boolean offer(T t);


    @SuppressWarnings("unchecked")
    class Growable<T> implements RingBuffer<T> {

        private Object[] data;
        private int head, tail, count;

        Growable(int initialCapacity) {
            if (initialCapacity == 0 || (initialCapacity & (initialCapacity - 1)) != 0)
                throw new IllegalArgumentException("RingBuffer capacity must be a power of two");
            data = new Object[initialCapacity];
        }

        @Override
        public int size() {
            return count;
        }

        @Override
        public void clear() {
            head = tail = count = 0;
        }

        @Override
        public T poll() {
            T t = (T) data[tail];
            tail = (tail + 1) & (data.length - 1);
            count--;
            return t;
        }

        @Override
        public boolean offer(T t) {
            if (count == data.length)
                grow();

            data[head] = t;
            head = (head + 1) & (data.length - 1);
            count++;
            return true;
        }

        private void grow() {
            //TODO
        }
    }

    @SuppressWarnings("unchecked")
    class FixedSize<T> implements RingBuffer<T> {

        private final Object[] data;
        private int head, tail, count;

        FixedSize(int initialCapacity) {
            if (initialCapacity == 0 || (initialCapacity & (initialCapacity - 1)) != 0)
                throw new IllegalArgumentException("RingBuffer capacity must be a power of two");
            data = new Object[initialCapacity];
        }

        @Override
        public int size() {
            return count;
        }

        @Override
        public void clear() {
            head = tail = count = 0;
        }

        @Override
        public T poll() {
            T t = (T) data[tail];
            tail = (tail + 1) & (data.length - 1);
            count--;
            return t;
        }

        @Override
        public boolean offer(T t) {
            if (count == data.length)
                return false;

            data[head] = t;
            head = (head + 1) & (data.length - 1);
            count++;
            return true;
        }
    }
}
