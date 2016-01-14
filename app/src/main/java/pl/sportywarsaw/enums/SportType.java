package pl.sportywarsaw.enums;

import pl.sportywarsaw.MyApplication;
import pl.sportywarsaw.R;

/**
 * Created by Jan Kierzkowski on 04.01.2016.
 */
public enum SportType {
    Football(R.string.football),
    Tennis(R.string.tennis),
    TableTennis(R.string.table_tennis),
    Basketball(R.string.basketball),
    Volleyball(R.string.volleyball),
    Swimming(R.string.swimming),
    Handball(R.string.handball),
    Badminton(R.string.badminton),
    Squash(R.string.squash);

    private int resourceId;

    SportType(int id) {
        resourceId = id;
    }

    @Override
    public String toString() {
        return MyApplication.getContext().getString(resourceId);
    }
}