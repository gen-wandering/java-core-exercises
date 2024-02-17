package com.tasks.completed.expiringcache;

/*
 * Thread-Safe Cache with Expiration:
 *    Implement a thread-safe cache using ConcurrentHashMap with an
 *    added feature of expiring entries after a specified time period.
 * */

public class Main {
    public static void main(String[] args) {
        ExpiringCache<String, Integer> cache = new ExpiringCache<>();

        // Put entries with a 5-second expiration
        cache.put("one", 1, 5000);
        cache.put("two", 2, 5000);
        cache.put("three", 3, 5000);

        // Retrieve entries
        System.out.println(cache.get("one"));    // Output: 1
        System.out.println(cache.get("two"));    // Output: 2
        System.out.println(cache.get("three"));  // Output: 3

        // Wait for entries to expire
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        // Retrieve expired entries should return null
        System.out.println(cache.get("one"));    // Output: null
        System.out.println(cache.get("two"));    // Output: null
        System.out.println(cache.get("three"));  // Output: null
    }
}