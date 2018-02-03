package client.gui;

import java.util.Comparator;

public class BookComparator implements Comparator<Controller.BookRecord> {
    public BookComparator() {
    }

    @Override
    public int compare(Controller.BookRecord o1, Controller.BookRecord o2) {
        String str1 = o1.getTitle();
        String str2 = o2.getTitle();
        int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
        if (res == 0) {
            res = str1.compareTo(str2);
        }
        return res;
    }
}