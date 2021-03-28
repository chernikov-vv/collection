import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyCollection<E> implements Collection<E> {

    private int size;

    private Object[] elementData = new Object[10];

    private final MyIterator<E> it = new MyIterator<>();


    @Override
    public boolean add(final E e) {
        if (size == elementData.length) {
            elementData = Arrays.copyOf(elementData, (int) (size * 1.5f));
        }
        elementData[size++] = e;
        return true;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator<>();
    }

    @Override
    public boolean contains(final Object o) {
        while (it.hasNext()) {
            try {
                if (it.next().equals(o)) {
                    return true;
                }
            } catch (NullPointerException e) {
                if (elementData[it.cursor - 1] == o) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size()];
        int i = 0;
        while (it.hasNext()) {
            array[i] = it.next();
            i++;
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = Arrays.copyOf(a, size);
        }
        for (int i = 0; i < a.length; i++) {
            if (it.hasNext()) {
                a[i] = (T) it.next();
            }
        }
        return a;
    }

    @Override
    public boolean remove(final Object o) {
        while (it.hasNext()) {
            try {
                if (it.next().equals(o)) {
                    it.remove();
                    it.cursor++;
                    return true;
                }
            } catch (NullPointerException e) {
                if (elementData[it.cursor - 1] == o) {
                    it.remove();
                    it.cursor++;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        boolean flag = true;
        for (Object o : c) {
            it.cursor = 0;
            if (!contains(o)) {
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        final int START_SIZE = size;
        for (Object o : c) {
            while (it.hasNext()) {
                try {
                    if (it.next().equals(o)) {
                        it.remove();
                        it.cursor++;
                        it.isNext = true;
                    }
                } catch (NullPointerException e) {
                    if (elementData[it.cursor - 1] == o) {
                        it.remove();
                        it.cursor++;
                        it.isNext = true;
                    }
                }
            }
            it.cursor = 0;
        }
        return START_SIZE != size;
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        while (it.hasNext()) {
            boolean flag = true;
            it.next();
            for (Object o : c) {
                if (elementData[it.cursor - 1] == o) {
                    flag = true;
                    break;
                } else {
                    flag = false;
                }

            }
            if (!flag) {
                it.isNext = true;
                it.remove();
            }
        }
        return true;
    }

    @Override
    public void clear() {
        size = 0;
    }

    private class MyIterator<T> implements Iterator<T> {

        private boolean isNext;

        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            if (cursor >= size) {
                throw new NoSuchElementException();
            }
            isNext = true;
            return (T) elementData[cursor++];
        }

        @Override
        public void remove() {
            if (isNext) {
                for (int i = cursor - 1; i < size; i++) {
                    if (hasNext()) {
                        elementData[i] = elementData[i + 1];
                    }
                }
                size--;
                cursor--;
            } else {
                throw new IllegalStateException();
            }
            isNext = false;
        }
    }
}