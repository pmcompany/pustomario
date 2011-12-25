package com.github.pmcompany.pustomario.net;

/**
 * @author dector (dector9@gmail.com)
 */
public interface Connection {
    public boolean isConnected();
    public void setConnected(boolean connected);
    public boolean isJoined();
    public void setJoined(boolean joined);
    public String getUserName();
}
