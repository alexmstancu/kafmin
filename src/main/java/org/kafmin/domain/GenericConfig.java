package org.kafmin.domain;

public class GenericConfig {
    private final String name;
    private final String value;
    private final boolean isReadOnly;

    public GenericConfig(String name, String value, boolean isReadOnly) {
        this.name = name;
        this.value = value;
        this.isReadOnly = isReadOnly;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    @Override
    public String toString() {
        return "GenericConfig{" +
            "name='" + name + '\'' +
            ", value='" + value + '\'' +
            ", isReadOnly=" + isReadOnly +
            '}';
    }
}
