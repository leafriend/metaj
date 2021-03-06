package net.folab.metaj.bytecode;

import java.util.Arrays;

import org.objectweb.asm.Opcodes;

public class JavaType implements Opcodes {

    public static final JavaType NULL = new JavaType(false, 1, ARETURN, ALOAD,
            "null");

    public static final JavaType VOID = new JavaType(true, 0, RETURN, 0, "void");

    public static final JavaType BOOLEAN = new JavaType(true, 1, IRETURN,
            ILOAD, "boolean");

    public static final JavaType BYTE = new JavaType(true, 1, IRETURN, ILOAD,
            "byte");

    public static final JavaType CHAR = new JavaType(true, 1, IRETURN, ILOAD,
            "char");

    public static final JavaType SHORT = new JavaType(true, 1, IRETURN, ILOAD,
            "short");

    public static final JavaType INT = new JavaType(true, 1, IRETURN, ILOAD,
            "int");

    public static final JavaType LONG = new JavaType(true, 2, LRETURN, LLOAD,
            "long");

    public static final JavaType FLOAT = new JavaType(true, 1, FRETURN, FLOAD,
            "float");

    public static final JavaType DOUBLE = new JavaType(true, 2, DRETURN, DLOAD,
            "double");

    public static final JavaType OBJECT = new JavaType("java/lang/Object");

    public static final JavaType STRING = new JavaType("java/lang/String");

    private final boolean isPrimitive;

    private final int size;

    private final String name;

    private final JavaType[] typeParameters;

    public final int returnOpcode;

    public final int loadOpcode;

    public JavaType(String name, JavaType... typeParameters) {
        this.isPrimitive = false;
        this.size = 1;
        this.name = name;
        this.typeParameters = typeParameters;
        this.returnOpcode = ARETURN;
        this.loadOpcode = ALOAD;
    }

    private JavaType(boolean isPrimitive, int size, int returnOpcode,
            int loadOpcode, String name, JavaType... typeParameters) {
        this.isPrimitive = isPrimitive;
        this.size = size;
        this.name = name;
        this.typeParameters = typeParameters;
        this.returnOpcode = returnOpcode;
        this.loadOpcode = loadOpcode;
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public JavaType[] getTypeParameters() {
        return typeParameters;
    }

    public String getDescName() {
        if (equals(VOID)) {
            return "V";
        }
        if (equals(BOOLEAN)) {
            return "Z";
        }
        if (equals(BYTE)) {
            return "B";
        }
        if (equals(CHAR)) {
            return "C";
        }
        if (equals(SHORT)) {
            return "S";
        }
        if (equals(INT)) {
            return "I";
        }
        if (equals(LONG)) {
            return "J";
        }
        if (equals(FLOAT)) {
            return "F";
        }
        if (equals(DOUBLE)) {
            return "D";
        }
        return "L" + getName() + ";";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isPrimitive ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + size;
        result = prime * result + Arrays.hashCode(typeParameters);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JavaType other = (JavaType) obj;
        if (isPrimitive != other.isPrimitive)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (size != other.size)
            return false;
        if (!Arrays.equals(typeParameters, other.typeParameters))
            return false;
        return true;
    }

}
