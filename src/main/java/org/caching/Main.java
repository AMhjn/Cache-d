package org.caching;

import org.caching.Caches.LRUCache;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        LRUCache myCache = new LRUCache(3);
        System.out.println(myCache.get(1));
        myCache.put(1,"ABhir");
        myCache.put(2,"ABhi");
        myCache.put(3,"ABh");
        myCache.put(4,"AB");
        System.out.println(myCache.get(1));
        System.out.println(myCache.get(2));
        System.out.println(myCache.get(3));
        System.out.println(myCache.get(4));

    }
}