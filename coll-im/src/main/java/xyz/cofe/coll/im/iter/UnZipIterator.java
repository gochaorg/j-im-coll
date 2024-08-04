package xyz.cofe.coll.im.iter;

import xyz.cofe.coll.im.Either;
import xyz.cofe.coll.im.Fn1;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class UnZipIterator<A, L, R> {
    private final Iterator<A> source;
    private final Fn1<A, Either<L, R>> separate;
    private final boolean sync;
    private final ReentrantLock sourceLock;

    public UnZipIterator(Iterator<A> source, Fn1<A, Either<L, R>> separate, boolean sync) {
        if (separate == null) throw new IllegalArgumentException("separate==null");
        if (source == null) throw new IllegalArgumentException("source==null");
        this.source = source;
        this.separate = separate;
        this.sync = sync;
        leftQueue = sync ? new ConcurrentLinkedQueue<>() : new LinkedList<>();
        rightQueue = sync ? new ConcurrentLinkedQueue<>() : new LinkedList<>();
        sourceLock = sync ? new ReentrantLock() : null;
    }

    private final Queue<L> leftQueue;
    private final Queue<R> rightQueue;

    private Optional<Either<L, R>> fetch() {
        Supplier<Optional<Either<L,R>>> extract = ()-> {
            if (!source.hasNext()) return Optional.empty();
            return Optional.of(separate.apply(source.next()));
        };

        var lock = sourceLock;
        if( lock!=null ){
            try {
                lock.lock();
                return extract.get();
            } finally {
                lock.unlock();
            }
        }else{
            return extract.get();
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean prefetch() {
        Supplier<Boolean> extract = ()-> {
            var fetchRes = fetch();
            if (fetchRes.isEmpty()) {
                return false;
            }

            var fetched = fetchRes.get();
            return fetched.fold(left -> {
                leftQueue.add(left);
                return true;
            }, right -> {
                rightQueue.add(right);
                return true;
            });
        };

        var lock = sourceLock;
        if( lock!=null ){
            try {
                lock.lock();
                return extract.get();
            } finally {
                lock.unlock();
            }
        }else{
            return extract.get();
        }
    }
    private boolean prefetchIntoLeft(){
        while (leftQueue.isEmpty()){
            if( !prefetch() )return false;
        }
        return true;
    }
    private boolean prefetchIntoRight(){
        while (rightQueue.isEmpty()){
            if( !prefetch() )return false;
        }
        return true;
    }

    public class LeftIterator implements Iterator<L> {
        public LeftIterator(){
            prefetched = prefetchIntoLeft() ? Optional.of((L) Objects.requireNonNull(leftQueue.poll())) : Optional.empty();
        }

        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        private Optional<L> prefetched;

        @Override
        public boolean hasNext() {
            return prefetched.isPresent();
        }

        @Override
        public L next() {
            var res = prefetched;
            if( res.isEmpty() )return null;
            if( !prefetchIntoLeft() ){
                prefetched = Optional.empty();
            }else{
                prefetched = Optional.of((L) Objects.requireNonNull(leftQueue.poll()));
            }
            return res.get();
        }
    }
    public class RightIterator implements Iterator<R> {
        public RightIterator(){
            prefetched = prefetchIntoRight() ? Optional.of((R) Objects.requireNonNull(rightQueue.poll())) : Optional.empty();
        }

        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        private Optional<R> prefetched;

        @Override
        public boolean hasNext() {
            return prefetched.isPresent();
        }

        @Override
        public R next() {
            var res = prefetched;
            if( res.isEmpty() )return null;
            if( !prefetchIntoRight() ){
                prefetched = Optional.empty();
            }else{
                prefetched = Optional.of((R) Objects.requireNonNull(rightQueue.poll()));
            }
            return res.get();
        }
    }

    private volatile LeftIterator leftIterator;
    public LeftIterator getLeftIterator(){
        if( leftIterator!=null )return leftIterator;
        synchronized (this) {
            if (leftIterator != null) return leftIterator;
            leftIterator = new LeftIterator();
            return leftIterator;
        }
    }

    private volatile RightIterator rightIterator;
    public RightIterator getRightIterator(){
        if(rightIterator!=null)return rightIterator;
        synchronized (this){
            if(rightIterator!=null)return rightIterator;
            rightIterator = new RightIterator();
            return rightIterator;
        }
    }
}
