package com.example.semesterproject.select_vehicle_files.dummy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class YearContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<YearItem> ITEMS = new ArrayList<YearItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, YearItem> ITEM_MAP = new HashMap<String, YearItem>();


    static {
        // Add some sample items.
        for (int i = Calendar.getInstance().get(Calendar.YEAR); i >= 1950; i--) {
            //addItem(createDummyItem(i));
        }
    }

    public static void addItem(String item) {
        YearItem itemObj = new YearItem(item);
        ITEMS.add(itemObj);
        ITEM_MAP.put(itemObj.year, itemObj);
    }

//    private static YearItem createDummyItem(String position) {
//        return new YearItem(Integer.toString(position));
//    }



    /**
     * A dummy item representing a piece of content.
     */
    public static class YearItem {
        public final String year;

        public YearItem(String year) {
            this.year = year;
        }

        @Override
        public String toString() {
            return year;
        }
    }
}
