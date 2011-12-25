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
    public static final NetworkPackage DEFAULT_PACKAGE =
            new NetworkPackage(PackageType.OK, null, null);
    public static final NetworkPackage SPECTATE_PACKAGE =
            new NetworkPackage(PackageType.SPECTATE, null, null);
    public static final NetworkPackage REJECTED_PACKAGE =
            new NetworkPackage(PackageType.REJECTED, null, null);

    private PackageType type;
    private String sender;
    private String value;

    public NetworkPackage(String representation) {
        if (representation != null) {
            int length = representation.length();

            int senderIndex = representation.indexOf(' ');
            sender = representation.substring(0, senderIndex);
            String noSenderString = representation.substring(senderIndex + 1, length);

            int typeIndex = noSenderString.indexOf(' ');
            type = PackageType.valueOf(noSenderString.substring(0, typeIndex));

            if (noSenderString.length() > senderIndex) {
                value = noSenderString.substring(typeIndex + 1, noSenderString.length());
            }
        }
    }

    public NetworkPackage(PackageType type, String sender, Object value) {
        this.type = type;
        this.sender = sender;

        if (value != null) {
            this.value = value.toString();
        }
    }

    public String getSender() {
        return sender;
    }

    public PackageType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return sender + " " + type + " " + value;
    }
}
