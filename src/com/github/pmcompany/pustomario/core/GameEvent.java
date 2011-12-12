package com.github.pmcompany.pustomario.core;

/**
 * @author dector (dector9@gmail.com)
 */
public class GameEvent implements Event {
    private EventType type;
    private Object value;

    public GameEvent(EventType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getStringValue() {
        return String.valueOf(value);
    }

    public int getIntValue() {
        return (Integer)value;
    }

    public float getFloatValue() {
        return (Float)value;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
