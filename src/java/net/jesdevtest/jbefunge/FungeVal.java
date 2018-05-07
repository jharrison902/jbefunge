package net.jesdevtest.jbefunge;

/**
 * Stores a Funge Stack Value.
 */
public class FungeVal {
    private String classType;
    private Object value;

    public FungeVal(Integer i) {
        value = i;
        classType = i.getClass().getTypeName();
    }

    public FungeVal(Character c) {
        value = c;
        classType = c.getClass().getTypeName();
    }

    public FungeVal(FungeVal v) {
        value = v.value;
        classType = v.classType;
    }

    public Object getValue() {
        return value;
    }

    public Integer getIntValue() {
        if (classType.equals(Integer.class.getTypeName())) {
            return (Integer) value;
        }
        return Character.getNumericValue((Character) value);
    }

    public Character getCharValue() {
        if (classType.equals(Character.class.getTypeName())) {
            return (Character) value;
        }
        return (char) (((Integer) value).intValue());
    }

    public String getClassType() {
        return classType;
    }

}
