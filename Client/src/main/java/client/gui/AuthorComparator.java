package client.gui;

import java.util.Comparator;

/**
 * Comparator for AuthorRecords
 * <p>
 * This comparator compares two authors alphabetically.
 * Uses to sorting rows in AUTHORS TABLE.
 *
 * @author Sergey Komarov
 */
public class AuthorComparator implements Comparator<Controller.AuthorRecord> {
    /**
     * Compare two rows of AUTHORS table.
     * The comparison is made in alphabetical order.
     *
     * @param o1 first authors table row
     * @param o2 second authors table row
     * @return positive number if FIRST operand bigger than SECOND operand, negative number if FIRST operand less than SECOND operand and 0 if operands are equals
     */
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
