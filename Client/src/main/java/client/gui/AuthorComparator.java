package client.gui;

import java.util.Comparator;

public class AuthorComparator implements Comparator<Controller.AuthorRecord> {
    @Override
    public int compare(Controller.AuthorRecord o1, Controller.AuthorRecord o2) {
        String str1 = o1.getName();
        String str2 = o2.getName();
        int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
        if (res == 0) {
            res = str1.compareTo(str2);
        }
        return res;
    }
}
