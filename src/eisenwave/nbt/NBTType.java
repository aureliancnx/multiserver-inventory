// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt;

public enum NBTType
{
    END("END", 0, "TAG_End", false, false, false), 
    BYTE("BYTE", 1, "TAG_Byte", true, true, false), 
    SHORT("SHORT", 2, "TAG_Short", true, true, false), 
    INT("INT", 3, "TAG_Int", true, true, false), 
    LONG("LONG", 4, "TAG_Long", true, true, false), 
    FLOAT("FLOAT", 5, "TAG_Float", true, true, false), 
    DOUBLE("DOUBLE", 6, "TAG_Double", true, true, false), 
    BYTE_ARRAY("BYTE_ARRAY", 7, "TAG_Byte_Array", false, false, true), 
    STRING("STRING", 8, "TAG_String", true, false, false), 
    LIST("LIST", 9, "TAG_List", false, false, false), 
    COMPOUND("COMPOUND", 10, "TAG_Compound", false, false, false), 
    INT_ARRAY("INT_ARRAY", 11, "TAG_Int_Array", false, false, true), 
    LONG_ARRAY("LONG_ARRAY", 12, "TAG_Long_Array", false, false, true);
    
    private final String name;
    private final boolean numeric;
    private final boolean primitive;
    private final boolean array;
    private final byte id;
    
    private NBTType(final String name2, final int ordinal, final String name, final boolean primitive, final boolean numeric, final boolean array) {
        this.name = name;
        this.id = (byte)this.ordinal();
        this.numeric = numeric;
        this.primitive = primitive;
        this.array = array;
    }
    
    public static NBTType getById(final byte id) {
        return values()[id];
    }
    
    public byte getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isNumeric() {
        return this.numeric;
    }
    
    public boolean isPrimitive() {
        return this.primitive;
    }
    
    public boolean isArray() {
        return this.array;
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
}
