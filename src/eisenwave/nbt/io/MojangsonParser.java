// 
// Decompiled by Procyon v0.5.36
// 

package eisenwave.nbt.io;

import java.util.List;
import java.util.ArrayList;
import eisenwave.nbt.NBTIntArray;
import eisenwave.nbt.NBTLongArray;
import eisenwave.nbt.NBTByteArray;
import eisenwave.nbt.NBTType;
import eisenwave.nbt.NBTList;
import eisenwave.nbt.NBTDouble;
import eisenwave.nbt.NBTInt;
import eisenwave.nbt.NBTShort;
import eisenwave.nbt.NBTLong;
import eisenwave.nbt.NBTByte;
import eisenwave.nbt.NBTFloat;
import eisenwave.nbt.NBTString;
import eisenwave.nbt.NBTCompound;
import eisenwave.nbt.NBTTag;
import eisenwave.nbt.NBTNamedTag;
import java.util.regex.Pattern;

public final class MojangsonParser
{
    private static final Pattern DOUBLE_NS;
    private static final Pattern DOUBLE_S;
    private static final Pattern FLOAT;
    private static final Pattern BYTE;
    private static final Pattern LONG;
    private static final Pattern SHORT;
    private static final Pattern INT;
    private final String str;
    private int index;
    
    static {
        DOUBLE_NS = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
        DOUBLE_S = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
        FLOAT = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
        BYTE = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
        LONG = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
        SHORT = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
        INT = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");
    }
    
    public static NBTNamedTag parse(final String mson) throws MojangsonParseException {
        return new MojangsonParser(mson).parseRootCompound();
    }
    
    private MojangsonParser(final String str) {
        this.str = str;
    }
    
    private NBTNamedTag parseRootCompound() throws MojangsonParseException {
        this.skipWhitespace();
        if (!this.hasNext()) {
            throw this.parseException("Expected key");
        }
        String name;
        NBTCompound result;
        if (this.currentChar() == '{') {
            name = "";
            result = this.parseCompound();
        }
        else {
            name = ((this.currentChar() == '\"') ? this.parseQuotedString() : this.parseSimpleString());
            this.expectChar(':');
            result = this.parseCompound();
        }
        this.expectNoTrail();
        return new NBTNamedTag(name, result);
    }
    
    private String parseCompoundKey() throws MojangsonParseException {
        this.skipWhitespace();
        if (!this.hasNext()) {
            throw this.parseException("Expected key");
        }
        return (this.currentChar() == '\"') ? this.parseQuotedString() : this.parseSimpleString();
    }
    
    private NBTTag parseStringOrLiteral() throws MojangsonParseException {
        this.skipWhitespace();
        if (this.currentChar() == '\"') {
            return new NBTString(this.parseQuotedString());
        }
        final String str = this.parseSimpleString();
        if (str.isEmpty()) {
            throw this.parseException("Expected value");
        }
        return this.parseLiteral(str);
    }
    
    private NBTTag parseLiteral(final String str) {
        try {
            if (MojangsonParser.FLOAT.matcher(str).matches()) {
                return new NBTFloat(Float.parseFloat(str.substring(0, str.length() - 1)));
            }
            if (MojangsonParser.BYTE.matcher(str).matches()) {
                return new NBTByte(Byte.parseByte(str.substring(0, str.length() - 1)));
            }
            if (MojangsonParser.LONG.matcher(str).matches()) {
                return new NBTLong(Long.parseLong(str.substring(0, str.length() - 1)));
            }
            if (MojangsonParser.SHORT.matcher(str).matches()) {
                return new NBTShort(Short.parseShort(str.substring(0, str.length() - 1)));
            }
            if (MojangsonParser.INT.matcher(str).matches()) {
                return new NBTInt(Integer.parseInt(str));
            }
            if (MojangsonParser.DOUBLE_S.matcher(str).matches()) {
                return new NBTDouble(Double.parseDouble(str.substring(0, str.length() - 1)));
            }
            if (MojangsonParser.DOUBLE_NS.matcher(str).matches()) {
                return new NBTDouble(Double.parseDouble(str));
            }
            if ("true".equalsIgnoreCase(str)) {
                return new NBTByte((byte)1);
            }
            if ("false".equalsIgnoreCase(str)) {
                return new NBTByte((byte)0);
            }
        }
        catch (NumberFormatException ex) {
            return new NBTString(str);
        }
        return new NBTString(str);
    }
    
    private String parseQuotedString() throws MojangsonParseException {
        final int j = ++this.index;
        StringBuilder builder = null;
        boolean escape = false;
        while (this.hasNext()) {
            final char c = this.nextChar();
            if (escape) {
                if (c != '\\' && c != '\"') {
                    throw this.parseException("Invalid escape of '" + c + "'");
                }
                escape = false;
            }
            else if (c == '\\') {
                escape = true;
                if (builder != null) {
                    continue;
                }
                builder = new StringBuilder(this.str.substring(j, this.index - 1));
                continue;
            }
            else if (c == '\"') {
                return (builder == null) ? this.str.substring(j, this.index - 1) : builder.toString();
            }
            if (builder != null) {
                builder.append(c);
            }
        }
        throw this.parseException("Missing termination quote");
    }
    
    private String parseSimpleString() {
        final int j = this.index;
        while (this.hasNext() && isSimpleChar(this.currentChar())) {
            ++this.index;
        }
        return this.str.substring(j, this.index);
    }
    
    private NBTTag parseAnything() throws MojangsonParseException {
        this.skipWhitespace();
        if (!this.hasNext()) {
            throw this.parseException("Expected value");
        }
        final int c = this.currentChar();
        if (c == 123) {
            return this.parseCompound();
        }
        if (c == 91) {
            return this.parseDetectedArray();
        }
        return this.parseStringOrLiteral();
    }
    
    private NBTTag parseDetectedArray() throws MojangsonParseException {
        if (this.hasCharsLeft(2) && this.getChar(1) != '\"' && this.getChar(2) == ';') {
            return this.parseNumArray();
        }
        return this.parseList();
    }
    
    private NBTCompound parseCompound() throws MojangsonParseException {
        this.expectChar('{');
        final NBTCompound compound = new NBTCompound();
        this.skipWhitespace();
        while (this.hasNext() && this.currentChar() != '}') {
            final String str = this.parseCompoundKey();
            if (str.isEmpty()) {
                throw this.parseException("Expected non-empty key");
            }
            this.expectChar(':');
            compound.put(str, this.parseAnything());
            if (!this.advanceToNextArrayElement()) {
                break;
            }
            if (!this.hasNext()) {
                throw this.parseException("Expected key");
            }
        }
        this.expectChar('}');
        return compound;
    }
    
    private NBTList parseList() throws MojangsonParseException {
        this.expectChar('[');
        this.skipWhitespace();
        if (!this.hasNext()) {
            throw this.parseException("Expected value");
        }
        final NBTList list = new NBTList();
        NBTType listType = null;
        while (this.currentChar() != ']') {
            final NBTTag element = this.parseAnything();
            final NBTType elementType = element.getType();
            if (listType == null) {
                listType = elementType;
            }
            else if (elementType != listType) {
                throw this.parseException("Unable to insert " + elementType + " into ListTag of type " + listType);
            }
            list.add(element);
            if (!this.advanceToNextArrayElement()) {
                break;
            }
            if (!this.hasNext()) {
                throw this.parseException("Expected value");
            }
        }
        this.expectChar(']');
        return list;
    }
    
    private NBTTag parseNumArray() throws MojangsonParseException {
        this.expectChar('[');
        final char arrayType = this.nextChar();
        this.expectChar(';');
        this.skipWhitespace();
        if (!this.hasNext()) {
            throw this.parseException("Expected value");
        }
        if (arrayType == 'B') {
            return new NBTByteArray(this.parseNumArray(NBTType.BYTE_ARRAY, NBTType.BYTE));
        }
        if (arrayType == 'L') {
            return new NBTLongArray(this.parseNumArray(NBTType.LONG_ARRAY, NBTType.LONG));
        }
        if (arrayType == 'I') {
            return new NBTIntArray(this.parseNumArray(NBTType.INT_ARRAY, NBTType.INT));
        }
        throw this.parseException("Invalid array type '" + arrayType + "' found");
    }
    
    private Number[] parseNumArray(final NBTType arrayType, final NBTType primType) throws MojangsonParseException {
        final List<Number> result = new ArrayList<Number>();
        while (this.currentChar() != ']') {
            final NBTTag element = this.parseAnything();
            final NBTType elementType = element.getType();
            if (elementType != primType) {
                throw this.parseException("Unable to insert " + elementType + " into " + arrayType);
            }
            if (primType == NBTType.BYTE) {
                result.add(((NBTByte)element).getValue());
            }
            else if (primType == NBTType.LONG) {
                result.add(((NBTLong)element).getValue());
            }
            else {
                result.add(((NBTInt)element).getValue());
            }
            if (!this.advanceToNextArrayElement()) {
                break;
            }
            if (!this.hasNext()) {
                throw this.parseException("Expected value");
            }
        }
        this.expectChar(']');
        return result.toArray(new Number[result.size()]);
    }
    
    private boolean advanceToNextArrayElement() {
        this.skipWhitespace();
        if (this.hasNext() && this.currentChar() == ',') {
            ++this.index;
            this.skipWhitespace();
            return true;
        }
        return false;
    }
    
    private void skipWhitespace() {
        while (this.hasNext() && Character.isWhitespace(this.currentChar())) {
            ++this.index;
        }
    }
    
    private boolean hasCharsLeft(final int paramInt) {
        return this.index + paramInt < this.str.length();
    }
    
    private boolean hasNext() {
        return this.hasCharsLeft(0);
    }
    
    private char getChar(final int offset) {
        return this.str.charAt(this.index + offset);
    }
    
    private char currentChar() {
        return this.getChar(0);
    }
    
    private char nextChar() {
        return this.str.charAt(this.index++);
    }
    
    private void expectChar(final char c) throws MojangsonParseException {
        this.skipWhitespace();
        final boolean hasNext = this.hasNext();
        if (hasNext && this.currentChar() == c) {
            ++this.index;
            return;
        }
        throw new MojangsonParseException("Expected '" + c + "' but got '" + (hasNext ? Character.valueOf(this.currentChar()) : "<EOF>") + "'", this.str, this.index + 1);
    }
    
    private void expectNoTrail() throws MojangsonParseException {
        this.skipWhitespace();
        if (this.hasNext()) {
            ++this.index;
            throw this.parseException("Trailing data " + this.currentChar() + " found");
        }
    }
    
    private MojangsonParseException parseException(final String paramString) {
        return new MojangsonParseException(paramString, this.str, this.index);
    }
    
    private static boolean isSimpleChar(final char paramChar) {
        return (paramChar >= '0' && paramChar <= '9') || (paramChar >= 'A' && paramChar <= 'Z') || (paramChar >= 'a' && paramChar <= 'z') || paramChar == '_' || paramChar == '-' || paramChar == '.' || paramChar == '+';
    }
}
