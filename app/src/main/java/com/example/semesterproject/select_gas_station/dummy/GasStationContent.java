package com.example.semesterproject.select_gas_station.dummy;

import com.example.semesterproject.StationItem;
import com.example.semesterproject.select_vehicle_files.dummy.YearContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class GasStationContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<StationItem> ITEMS = new ArrayList<StationItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, StationItem> ITEM_MAP = new HashMap<String, StationItem>();

    private static final int COUNT = 25;

//    static {
//        // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createDummyItem(i));
//        }
//    }

//    private static void addItem(StationItem item) {
//        ITEMS.add(item);
//        ITEM_MAP.put(item.name, item);
//    }

    public static void addItem(StationItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.name, item);
    }

    public static void clearItems(){
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    public static void addItems(List<StationItem> items) {
        ITEMS.addAll(items);
        for(StationItem item : items){
            ITEM_MAP.put(item.name, item);
        }
    }

//    private static StationItem createDummyItem(int position) {
//        return new StationItem(String.valueOf(position), "Item " + position, makeDetails(position));
//    }
}
