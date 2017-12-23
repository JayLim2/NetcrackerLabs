/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol;

import java.io.FilterInputStream;
import java.io.InputStream;

/**
 *
 * @author Алескандр
 */
public class NoCloseInputStream extends FilterInputStream {
    public NoCloseInputStream(InputStream in) {
        super(in);
    }
    
    public void close() {} // ignore close
}
