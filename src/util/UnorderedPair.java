package util;

public class UnorderedPair<T> {

    private T k;
    private T v;

    public UnorderedPair(T k, T v) {
        this.k = k;
        this.v = v;
    }

    public T getA() {
        return k;
    }

    public T getB() {
        return v;
    }

    @Override
    public int hashCode() {
        return k.hashCode() ^ v.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UnorderedPair))
            return false;
        UnorderedPair p = (UnorderedPair) obj;
        if (v.equals(p.v) && k.equals(p.k))
            return true;
        if (v.equals(p.k) && k.equals(p.v))
            return true;
        return false;
    }
}
