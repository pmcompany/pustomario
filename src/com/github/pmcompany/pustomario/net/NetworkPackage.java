package com.github.pmcompany.pustomario.net;

import javax.xml.bind.annotation.XmlElementRef;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author dector (dector9@gmail.com)
 */
public class NetworkPackage {
    private static final NetworkPackage DEFAULT_PACKAGE =
            new NetworkPackage(PackageType.OK, null);

    private PackageType type;
    private String value;

    public NetworkPackage(String representation) {
        if (representation != null) {
            int typeIndex = representation.indexOf(' ');
            type = PackageType.valueOf(representation.substring(0, typeIndex));

            int length = representation.length();
            if (length > typeIndex) {
                value = representation.substring(typeIndex + 1, length);
            }
        }
    }

    public NetworkPackage(PackageType type, Object value) {
        this.type = type;
        if (value != null) {
            this.value = value.toString();
        }
    }

    public PackageType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type + " " + value;
    }

    public static NetworkPackage defaultPackage() {
        return DEFAULT_PACKAGE;
    }
}
