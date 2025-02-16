package motloung.koena.kovsiesbanking.helpers;

import android.widget.EditText;

import java.util.Arrays;

public class Helpers {
    public static boolean anyEmpty(EditText... controls) {
        return Arrays.stream(controls).anyMatch(c -> !(c.getText().toString().length() > 0));
    }
}
