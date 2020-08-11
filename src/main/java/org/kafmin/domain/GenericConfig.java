package org.kafmin.domain;

public class GenericConfig {
    private String name;
    private String value;
    private boolean isReadOnly;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
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
