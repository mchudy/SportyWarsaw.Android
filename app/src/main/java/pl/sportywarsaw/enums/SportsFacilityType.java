package pl.sportywarsaw.enums;

import pl.sportywarsaw.MyApplication;
import pl.sportywarsaw.R;

/**
 * Created by Jan Kierzkowski on 04.01.2016.
 */
public enum SportsFacilityType {
    SwimmingPool (R.string.swimming_pool),
    SportsField(R.string.sports_field);

    private int resourceId;

    SportsFacilityType(int id) {
        resourceId = id;
    }

    @Override
    public String toString() {
        return MyApplication.getContext().getString(resourceId);
    }
}
