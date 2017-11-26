package org.elias.entity;

/**
 * Param name - value pair for storing various processing params for each file
 *
 */
public class ResultTuple {
    String name;
    Object value;

    public ResultTuple(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public ResultTuple() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultTuple that = (ResultTuple) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        return getValue() != null ? getValue().equals(that.getValue()) : that.getValue() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }
}
