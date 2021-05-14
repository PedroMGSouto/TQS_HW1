package ua.tqs.hw1;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirQualityCache<K, T> {

    private long timeToLive;
    private long timer;
    private Map<K, CacheObject> mycache;
    private int requests;
    private int hits;
    private int misses;
    private long lastRefresh = System.currentTimeMillis();

    protected class CacheObject extends AirQuality {
        public long lastAccessed = System.currentTimeMillis();
        public T value;

        protected CacheObject(T value) {
            this.value = value;
        }

    }

    public AirQualityCache(long timeToLive, final long timer) {
        this.timeToLive = timeToLive * 1000;
        this.timer = timer * 1000;
        this.mycache = new HashMap<>();

        if (timeToLive > 0 && timer > 0) {

            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(timer * 1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        cleanup();
                    }
                }
            });
            t.setDaemon(true);
            t.start();
        } else
            throw new IllegalArgumentException("Error: TTL and timer values can't be less than 0!!");
    }

    public void put(K key, T value) {
        synchronized (mycache) {
            this.mycache.put(key, new CacheObject(value));
        }
    }

    @SuppressWarnings("unchecked")
    public T get(K key) {
        synchronized (mycache) {
            CacheObject cacheObject = (CacheObject) mycache.get(key);
            this.requests++;
            if (cacheObject == null) {
                this.misses++;
                return null;
            }
            this.hits++;
            cacheObject.lastAccessed = System.currentTimeMillis();
            return cacheObject.value;
        }
    }

    public void remove(String key) {
        synchronized (mycache) {
            if(this.mycache.remove(key) == null)
                throw new NullPointerException();
        }
    }

    public int size() {
        synchronized (mycache) {
            return mycache.size();
        }
    }


    @SuppressWarnings("unchecked")
    public void cleanup() {

        long now = System.currentTimeMillis();
        this.lastRefresh = now;

        List<K> expiredObjects = new ArrayList<>();

        synchronized (mycache) {

            for (K key : mycache.keySet()) {

                CacheObject cacheObject = mycache.get(key);

                if (cacheObject != null && now > (timeToLive + cacheObject.lastAccessed)) {
                    expiredObjects.add(key);
                }
            }
        }

        for (K key : expiredObjects) {
            synchronized (mycache) {
                if(this.mycache.remove(key) == null)
                    throw new NullPointerException();
            }

            Thread.yield();
        }
    }

    public void clean() {
        synchronized (mycache) {
            this.mycache = new HashMap<>();
            this.requests = 0;
            this.hits = 0;
            this.misses = 0;
            this.lastRefresh = System.currentTimeMillis();
        }
    }

    public int getRequests() {
        return requests;
    }

    public int getHits() {
        return hits;
    }

    public int getMisses() {
        return misses;
    }

    public long getTimeToLive() {
        return timeToLive/1000;
    }
    public long getTimer() {
        return timer/1000;
    }

    public long getLastRefresh() {
        return lastRefresh;
    }

    public Map<K, CacheObject> getMyCacheObjs() {
        return mycache;
    }

}