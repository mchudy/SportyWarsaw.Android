package pl.sportywarsaw.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import pl.sportywarsaw.R;
import pl.sportywarsaw.models.PositionModel;
import pl.sportywarsaw.models.SportFacilityPlusModel;

/**
 * Created by Marcin on 17/01/2016.
 */
public class MapUtils {

    public static Intent showMap(SportFacilityPlusModel model, Context context) {
        PositionModel position = model.getPosition();
        try {
            String label = URLEncoder.encode(model.getDescription(), "utf-8");
            String coords = "<" + position.getLatitude() + ">,<" + position.getLongitude() + ">";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + coords + "?q=" + coords
                    + "(" + label + ")"));
            context.startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            Log.e("Map", e.getMessage());
            Toast.makeText(context, R.string.error_loading_map,
                    Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
