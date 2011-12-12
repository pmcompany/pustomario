package com.github.pmcompany.pustomario.net;

import java.io.IOException;

/**
 * @author dector (dector9@gmail.com)
 */
public class NotConnectedException extends IOException {
    public NotConnectedException() {
    }

    public NotConnectedException(String message) {
        super(message);
    }
}
