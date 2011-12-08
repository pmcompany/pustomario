package com.github.pmcompany.pustomario.core;

/**
 * @author dector (dector9@gmail.com)
 */
public interface Event {
    public EventType getType();
    public Object getValue();

    public String getStringValue();
    public int getIntValue();
    public float getFloatValue();
}
