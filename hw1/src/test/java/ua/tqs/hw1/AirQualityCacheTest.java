package ua.tqs.hw1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AirQualityCacheTest {
    AirQualityCache<String,String> cache;

    @BeforeEach
    void setUp() {
        cache = new AirQualityCache<>(200,500);
        cache.put("C","Do");
        cache.put("D","Re");
        cache.put("E","Mi");
    }

    @AfterEach
    void tearDown() {
    }

    @DisplayName("Add to cache")
    @Test
    void put(){
        cache.put("F","Fa");
        assertEquals(4,cache.getMyCacheObjs().size());
        assertFalse(cache.getMyCacheObjs().isEmpty(), "Cache should have at least 1 element");
    }

    @DisplayName("Remove from cache")
    @Test
    void remove(){
        cache.remove("E");
        assertEquals(2,cache.size());
    }

    @DisplayName("Get from cache")
    @Test
    void get(){
        assertEquals("Do",cache.get("C"));
        cache = new AirQualityCache<>(200,500);
        assertNull(cache.get("F"));
    }

    @DisplayName("Cache Size")
    @Test
    void size() {
        assertEquals(3,cache.size());
        cache = new AirQualityCache<>(200,500);
        assertEquals(0,cache.size());
    }

    @DisplayName("IsEmpty")
    @Test
    void empty(){
        cache = new AirQualityCache<>(200,500);
        assertTrue(cache.getMyCacheObjs().isEmpty(), "Cache should be empty but it's not!");
    }

    @DisplayName("Expired")
    @Test
    void expired(){
        cache = new AirQualityCache<>(1,1);
        cache.put("C","Do");
        cache.put("D","Re");
        cache.put("E","Mi");
        try {
            Thread.sleep(3000);
            assertEquals(0,cache.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Requests")
    @Test
    void requests(){
        cache.get("C");
        cache.get("C");
        cache.get("C");
        assertEquals(3, cache.getRequests());
    }

    @DisplayName("Hits")
    @Test
    void hits(){
        cache.get("C");
        cache.get("C");
        cache.get("C");
        assertEquals(3, cache.getHits());
    }

    @DisplayName("Misses")
    @Test
    void misses(){
        cache.get("H");
        cache.get("H");
        cache.get("H");
        assertEquals(3, cache.getMisses());
    }

    @DisplayName("Clean")
    @Test
    void clean(){
        cache.clean();
        assertEquals(0, cache.getMisses());
        assertEquals(0, cache.getRequests());
        assertEquals(0, cache.getHits());
        assertEquals(0,cache.size());
    }

    @DisplayName("TTL")
    @Test
    void ttl(){
        assertThrows(IllegalArgumentException.class,()->cache=new AirQualityCache<>(-1,-1));
    }
}