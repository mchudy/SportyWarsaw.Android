package net.azurewebsites.sportywarsaw.enums;

import net.azurewebsites.sportywarsaw.MyApplication;
import net.azurewebsites.sportywarsaw.R;

/**
 * Created by Jan Kierzkowski on 04.01.2016.
 */
public enum SportsFacilityType {
    SwimmingPool (R.string.swimming_pool),
    SportsField(R.string.sports_field);

    private int resourceId;

    private SportsFacilityType(int id) {
        resourceId = id;
    }

    @Override
    public String toString() {
        return MyApplication.getContext().getString(resourceId);
    }
}
