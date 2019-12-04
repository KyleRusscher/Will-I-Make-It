package com.example.semesterproject.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample instructions for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DirectionContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DirectionItem> ITEMS = new ArrayList<DirectionItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DirectionItem> ITEM_MAP = new HashMap<String, DirectionItem>();

    private static final int COUNT = 25;


    public static void addItem(String distance, String instructions) {
        DirectionItem item = new DirectionItem(distance, instructions);
        ITEMS.add(item);
        ITEM_MAP.put(item.distance, item);
    }

    public static void clearItems(){
        ITEMS.clear();
        ITEM_MAP.clear();
    }



    /**
     * A dummy item representing a piece of instructions.
     */
    public static class DirectionItem {
        public final String distance;
        public final String instructions;

        public DirectionItem(String distance, String instructions) {
            this.distance = distance;
            this.instructions = instructions;
        }

        @Override
        public String toString() {
            return instructions;
        }
    }
}