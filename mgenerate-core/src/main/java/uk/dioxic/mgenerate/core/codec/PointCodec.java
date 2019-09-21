package uk.dioxic.mgenerate.core.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import uk.dioxic.mgenerate.core.operator.type.Coordinates;

public class PointCodec implements Codec<Coordinates> {
    @Override
    public Coordinates decode(BsonReader reader, DecoderContext decoderContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void encode(BsonWriter writer, Coordinates value, EncoderContext encoderContext) {
        writer.writeStartArray();
        writer.writeDouble(value.getX());
        writer.writeDouble(value.getY());
        writer.writeEndArray();
    }

    @Override
    public Class<Coordinates> getEncoderClass() {
        return Coordinates.class;
    }
}
