package com.github.pmcompany.pustomario.net;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author dector (dector9@gmail.com)
 */
public class NetworkPackage implements Externalizable {
    private PackageType type;
    private Object value;

    public NetworkPackage(PackageType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(type);
        out.writeObject(value);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        type = (PackageType)in.readObject();
        value = in.readObject();
    }

    @Override
    public String toString() {
        return type + " " + value;
    }
}
