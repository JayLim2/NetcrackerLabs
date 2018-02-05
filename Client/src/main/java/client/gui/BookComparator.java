package client.gui;

import java.util.Comparator;

/**
 * Comparator for AuthorRecords
 * <p>
 * This comparator compares two books alphabetically.
 * Uses to sorting rows in BOOKS TABLE.
 *
 * @author Sergey Komarov
 */
public class BookComparator implements Comparator<Controller.BookRecord> {
    /**
     * Compare two rows of BOOKS table.
     * The comparison is made in alphabetical order.
     *
     * @param o1 first books table row
     * @param o2 second books table row
     * @return positive number if FIRST operand bigger than SECOND operand, negative number if FIRST operand less than SECOND operand and 0 if operands are equals
     */
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
