/*
 * Copyright 2008-2017 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bson.json;


import org.bson.AbstractBsonReader;
import org.bson.BSONException;
import org.bson.BsonBinary;
import org.bson.BsonContextType;
import org.bson.BsonDbPointer;
import org.bson.BsonInvalidOperationException;
import org.bson.BsonReaderMark;
import org.bson.BsonRegularExpression;
import org.bson.BsonTimestamp;
import org.bson.BsonType;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

import static java.lang.String.format;


/**
 * Reads a JSON in Strict mode to avoid "extended" JavaScript format placeholder clashing with mgenerate4j Operator placeholders
 *
 */
public class StrictJsonReader extends AbstractBsonReader {

    private final JsonScanner scanner;
    private JsonToken pushedToken;
    private Object currentValue;
    private Mark mark;

    /**
     * Constructs a new instance with the given JSON string.
     *
     * @param json A string representation of a JSON.
     */
    public StrictJsonReader(final String json) {
        super();
        scanner = new JsonScanner(json);
        setContext(new Context(null, BsonContextType.TOP_LEVEL));
    }

    @Override
    protected BsonBinary doReadBinaryData() {
        return (BsonBinary) currentValue;
    }

    @Override
    protected byte doPeekBinarySubType() {
        return doReadBinaryData().getType();
    }

    @Override
    protected int doPeekBinarySize() {
        return doReadBinaryData().getData().length;
    }

    @Override
    protected boolean doReadBoolean() {
        return (Boolean) currentValue;
    }

    //CHECKSTYLE:OFF
    @Override
    public BsonType readBsonType() {
        if (isClosed()) {
            throw new IllegalStateException("This instance has been closed");
        }
        if (getState() == State.INITIAL || getState() == State.DONE || getState() == State.SCOPE_DOCUMENT) {
            // in JSON the top level value can be of any type so fall through
            setState(State.TYPE);
        }
        if (getState() != State.TYPE) {
            throwInvalidState("readBSONType", State.TYPE);
        }

        if (getContext().getContextType() == BsonContextType.DOCUMENT) {
            JsonToken nameToken = popToken();
            switch (nameToken.getType()) {
                case STRING:
                case UNQUOTED_STRING:
                    setCurrentName(nameToken.getValue(String.class));
                    break;
                case END_OBJECT:
                    setState(State.END_OF_DOCUMENT);
                    return BsonType.END_OF_DOCUMENT;
                default:
                    throw new JsonParseException("JSON reader was expecting a name but found '%s'.", nameToken.getValue());
            }

            JsonToken colonToken = popToken();
            if (colonToken.getType() != JsonTokenType.COLON) {
                throw new JsonParseException("JSON reader was expecting ':' but found '%s'.", colonToken.getValue());
            }
        }

        JsonToken token = popToken();
        if (getContext().getContextType() == BsonContextType.ARRAY && token.getType() == JsonTokenType.END_ARRAY) {
            setState(State.END_OF_ARRAY);
            return BsonType.END_OF_DOCUMENT;
        }

        boolean noValueFound = false;
        switch (token.getType()) {
            case BEGIN_ARRAY:
                setCurrentBsonType(BsonType.ARRAY);
                break;
            case BEGIN_OBJECT:
                setCurrentBsonType(BsonType.DOCUMENT);
                currentValue = token.getValue();
                break;
            case DOUBLE:
                setCurrentBsonType(BsonType.DOUBLE);
                currentValue = token.getValue();
                break;
            case END_OF_FILE:
                setCurrentBsonType(BsonType.END_OF_DOCUMENT);
                break;
            case INT32:
                setCurrentBsonType(BsonType.INT32);
                currentValue = token.getValue();
                break;
            case INT64:
                setCurrentBsonType(BsonType.INT64);
                currentValue = token.getValue();
                break;
            case REGULAR_EXPRESSION:
                setCurrentBsonType(BsonType.REGULAR_EXPRESSION);
                currentValue = token.getValue();
                break;
            case STRING:
                setCurrentBsonType(BsonType.STRING);
                currentValue = token.getValue();
                break;
            default:
                noValueFound = true;
                break;
        }
        if (noValueFound) {
            throw new JsonParseException("JSON reader was expecting a value but found '%s'.", token.getValue());
        }

        if (getContext().getContextType() == BsonContextType.ARRAY || getContext().getContextType() == BsonContextType.DOCUMENT) {
            JsonToken commaToken = popToken();
            if (commaToken.getType() != JsonTokenType.COMMA) {
                pushToken(commaToken);
            }
        }

        switch (getContext().getContextType()) {
            case DOCUMENT:
            case SCOPE_DOCUMENT:
            default:
                setState(State.NAME);
                break;
            case ARRAY:
            case JAVASCRIPT_WITH_SCOPE:
            case TOP_LEVEL:
                setState(State.VALUE);
                break;
        }
        return getCurrentBsonType();
    }
    //CHECKSTYLE:ON

    @Override
    public Decimal128 doReadDecimal128() {
        return (Decimal128) currentValue;
    }

    @Override
    protected long doReadDateTime() {
        return (Long) currentValue;
    }

    @Override
    protected double doReadDouble() {
        return (Double) currentValue;
    }

    @Override
    protected void doReadEndArray() {
        setContext(getContext().getParentContext());

        if (getContext().getContextType() == BsonContextType.ARRAY || getContext().getContextType() == BsonContextType.DOCUMENT) {
            JsonToken commaToken = popToken();
            if (commaToken.getType() != JsonTokenType.COMMA) {
                pushToken(commaToken);
            }
        }
    }

    @Override
    protected void doReadEndDocument() {
        setContext(getContext().getParentContext());
        if (getContext() != null && getContext().getContextType() == BsonContextType.SCOPE_DOCUMENT) {
            setContext(getContext().getParentContext()); // JavaScriptWithScope
            verifyToken(JsonTokenType.END_OBJECT); // outermost closing bracket for JavaScriptWithScope
        }

        if (getContext() == null) {
            throw new JsonParseException("Unexpected end of document.");
        }

        if (getContext().getContextType() == BsonContextType.ARRAY || getContext().getContextType() == BsonContextType.DOCUMENT) {
            JsonToken commaToken = popToken();
            if (commaToken.getType() != JsonTokenType.COMMA) {
                pushToken(commaToken);
            }
        }
    }

    @Override
    protected int doReadInt32() {
        return (Integer) currentValue;
    }

    @Override
    protected long doReadInt64() {
        return (Long) currentValue;
    }

    @Override
    protected String doReadJavaScript() {
        return (String) currentValue;
    }

    @Override
    protected String doReadJavaScriptWithScope() {
        return (String) currentValue;
    }

    @Override
    protected void doReadMaxKey() {
    }

    @Override
    protected void doReadMinKey() {
    }

    @Override
    protected void doReadNull() {
    }

    @Override
    protected ObjectId doReadObjectId() {
        return (ObjectId) currentValue;
    }

    @Override
    protected BsonRegularExpression doReadRegularExpression() {
        return (BsonRegularExpression) currentValue;
    }

    @Override
    protected BsonDbPointer doReadDBPointer() {
        return (BsonDbPointer) currentValue;
    }

    @Override
    protected void doReadStartArray() {
        setContext(new Context(getContext(), BsonContextType.ARRAY));
    }

    @Override
    protected void doReadStartDocument() {
        setContext(new Context(getContext(), BsonContextType.DOCUMENT));
    }

    @Override
    protected String doReadString() {
        return (String) currentValue;
    }

    @Override
    protected String doReadSymbol() {
        return (String) currentValue;
    }

    @Override
    protected BsonTimestamp doReadTimestamp() {
        return (BsonTimestamp) currentValue;
    }

    @Override
    protected void doReadUndefined() {
    }

    @Override
    protected void doSkipName() {
    }

    @Override
    protected void doSkipValue() {
        switch (getCurrentBsonType()) {
            case ARRAY:
                readStartArray();
                while (readBsonType() != BsonType.END_OF_DOCUMENT) {
                    skipValue();
                }
                readEndArray();
                break;
            case BINARY:
                readBinaryData();
                break;
            case BOOLEAN:
                readBoolean();
                break;
            case DATE_TIME:
                readDateTime();
                break;
            case DOCUMENT:
                readStartDocument();
                while (readBsonType() != BsonType.END_OF_DOCUMENT) {
                    skipName();
                    skipValue();
                }
                readEndDocument();
                break;
            case DOUBLE:
                readDouble();
                break;
            case INT32:
                readInt32();
                break;
            case INT64:
                readInt64();
                break;
            case DECIMAL128:
                readDecimal128();
                break;
            case JAVASCRIPT:
                readJavaScript();
                break;
            case JAVASCRIPT_WITH_SCOPE:
                readJavaScriptWithScope();
                readStartDocument();
                while (readBsonType() != BsonType.END_OF_DOCUMENT) {
                    skipName();
                    skipValue();
                }
                readEndDocument();
                break;
            case MAX_KEY:
                readMaxKey();
                break;
            case MIN_KEY:
                readMinKey();
                break;
            case NULL:
                readNull();
                break;
            case OBJECT_ID:
                readObjectId();
                break;
            case REGULAR_EXPRESSION:
                readRegularExpression();
                break;
            case STRING:
                readString();
                break;
            case SYMBOL:
                readSymbol();
                break;
            case TIMESTAMP:
                readTimestamp();
                break;
            case UNDEFINED:
                readUndefined();
                break;
            default:
        }
    }

    private JsonToken popToken() {
        if (pushedToken != null) {
            JsonToken token = pushedToken;
            pushedToken = null;
            return token;
        }
        else {
            return scanner.nextToken();
        }
    }

    private void pushToken(final JsonToken token) {
        if (pushedToken == null) {
            pushedToken = token;
        }
        else {
            throw new BsonInvalidOperationException("There is already a pending token.");
        }
    }

    private void verifyToken(final JsonTokenType expectedType) {
        JsonToken token = popToken();
        if (expectedType != token.getType()) {
            throw new JsonParseException("JSON reader expected token type '%s' but found '%s'.", expectedType, token.getValue());
        }
    }

    @Deprecated
    @Override
    public void mark() {
        if (mark != null) {
            throw new BSONException("A mark already exists; it needs to be reset before creating a new one");
        }
        mark = new Mark();
    }

    @Override
    public BsonReaderMark getMark() {
        return new Mark();
    }

    @Override
    public void reset() {
        if (mark == null) {
            throw new BSONException("trying to reset a mark before creating it");
        }
        mark.reset();
        mark = null;
    }

    @Override
    protected Context getContext() {
        return (Context) super.getContext();
    }

    protected class Mark extends AbstractBsonReader.Mark {
        private final JsonToken pushedToken;
        private final Object currentValue;
        private final int position;

        protected Mark() {
            super();
            pushedToken = StrictJsonReader.this.pushedToken;
            currentValue = StrictJsonReader.this.currentValue;
            position = StrictJsonReader.this.scanner.getBufferPosition();
        }

        public void reset() {
            super.reset();
            StrictJsonReader.this.pushedToken = pushedToken;
            StrictJsonReader.this.currentValue = currentValue;
            StrictJsonReader.this.scanner.setBufferPosition(position);
            StrictJsonReader.this.setContext(new Context(getParentContext(), getContextType()));
        }
    }


    protected class Context extends AbstractBsonReader.Context {
        protected Context(final AbstractBsonReader.Context parentContext, final BsonContextType contextType) {
            super(parentContext, contextType);
        }

        protected Context getParentContext() {
            return (Context) super.getParentContext();
        }

        protected BsonContextType getContextType() {
            return super.getContextType();
        }
    }

    private static byte[] decodeHex(final String hex) {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("A hex string must contain an even number of characters: " + hex);
        }

        byte[] out = new byte[hex.length() / 2];

        for (int i = 0; i < hex.length(); i += 2) {
            int high = Character.digit(hex.charAt(i), 16);
            int low = Character.digit(hex.charAt(i + 1), 16);
            if (high == -1 || low == -1) {
                throw new IllegalArgumentException("A hex string can only contain the characters 0-9, A-F, a-f: " + hex);
            }

            out[i / 2] = (byte) (high * 16 + low);
        }

        return out;
    }
}

