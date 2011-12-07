package com.github.pmcompany.pustomario.core;

/**
 * @author dector (dector9@gmail.com)
 */
public interface EventServer {
    public void addEventHandler(EventHandler handler);
    public void removeEventHandler(EventHandler handler);
}
