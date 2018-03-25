package com.allardworks.workinator3.consumer;

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Created by jaya on 2/25/18.
 * A cache of partition configuration objects.
 * This is poorly implemented. A better solution is needed, but not urgent.
 * The workinator needs configuration for thing such as
 * - set due date when partition is released (needs maxIdleTime)
 * - consumer needs to know the max number of workers (maxWorkerCount)
 */
@RequiredArgsConstructor
public class StupidCache<TInput, TOutput> {
    // TODO: switch this to use spring caching.
    // default memory cache doesn't have ttl. need to dig into that more
    // to determine appropriate solution. Maybe caffeine?

    private final Function<TInput, TOutput> lookup;
    private final Map<TInput, CacheItem<TOutput>> cache = new HashMap<>();

    @RequiredArgsConstructor
    private final static class CacheItem<T> {
        private final T item;
        private final LocalDateTime expires;

        private boolean isExpired() {
            return expires.isBefore(LocalDateTime.now());
        }
    }

    private CacheItem<TOutput> getFromCache(final TInput key) {
        return cache.computeIfAbsent(key, pk -> new CacheItem<>(lookup.apply(key), LocalDateTime.now().plus(5, MINUTES)));
    }

    /**
     * Gets the item from the cache.
     * if the item isn't in the cache, or is expired, it will execute the lookup method.
     * @param key
     * @return
     */
    public TOutput getItem(final TInput key) {
        val item = getFromCache(key);
        if (item == null) {
            return null;
        }

        if (!item.isExpired()) {
            return item.item;
        }

        // this sucks, but is temporary pending research of proper cache solution.
        cache.remove(key);
        val itemAttempt2 = getFromCache(key);
        return itemAttempt2 == null
                ? null
                : itemAttempt2.item;
    }
}
