package uk.dioxic.mgenerate.core;

import org.assertj.core.util.Lists;
import org.bson.codecs.DocumentCodec;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.mgenerate.core.codec.TemplateCodec;
import uk.dioxic.mgenerate.core.operator.core.*;
import uk.dioxic.mgenerate.core.operator.geo.*;
import uk.dioxic.mgenerate.core.operator.internet.Url;
import uk.dioxic.mgenerate.core.operator.internet.UrlBuilder;
import uk.dioxic.mgenerate.core.operator.geo.Coordinates;
import uk.dioxic.mgenerate.core.operator.location.CoordinatesBuilder;
import uk.dioxic.mgenerate.core.operator.numeric.NumberDecimal;
import uk.dioxic.mgenerate.core.operator.numeric.NumberDecimalBuilder;
import uk.dioxic.mgenerate.core.operator.sequence.DateSequence;
import uk.dioxic.mgenerate.core.operator.sequence.DateSequenceBuilder;
import uk.dioxic.mgenerate.core.operator.sequence.IntSequence;
import uk.dioxic.mgenerate.core.operator.sequence.IntSequenceBuilder;
import uk.dioxic.mgenerate.core.operator.text.Character;
import uk.dioxic.mgenerate.core.operator.text.*;
import uk.dioxic.mgenerate.core.operator.time.*;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;
import uk.dioxic.mgenerate.core.util.FlsUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class OperatorTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    void objectId() {
        assertThat(new ObjectIdBuilder(ReflectiveTransformerRegistry.getInstance()).build().resolve()).isInstanceOf(ObjectId.class);
    }

    @Test
    void seq() {
        int start = 3;
        int step = 2;

        IntSequence seq = new IntSequenceBuilder(ReflectiveTransformerRegistry.getInstance())
                .start(start)
                .step(step)
                .build();

        assertThat(seq.resolve()).as("check starting value").isEqualTo(start);
        assertThat(seq.resolve()).as("check next value").isEqualTo(start + step);
        assertThat(seq.resolve()).as("check next value").isEqualTo(start + step + step);
    }

    @Test
    void dateInc() {
        LocalDateTime start = LocalDateTime.parse("1900-01-01T00:00:00");
        long step = 5;
        ChronoUnit chrono = ChronoUnit.MINUTES;

        DateSequence inc = new DateSequenceBuilder(ReflectiveTransformerRegistry.getInstance())
                .start(start)
                .chronoUnit(chrono)
                .step(step)
                .build();

        assertThat(inc.resolve()).as("check starting value").isEqualTo(start);
        assertThat(inc.resolve()).as("check next value").isEqualTo(start.plus(step, chrono));
        assertThat(inc.resolve()).as("check next value").isEqualTo(start.plus(step, chrono).plus(step, chrono));
    }

    @Test
    void join() {
        String sep = "|";
        List<String> array = Lists.newArrayList("fish", "gofer", "beaver");

        Join join = new JoinBuilder(ReflectiveTransformerRegistry.getInstance()).array(array).sep(sep).build();

        assertThat(join.resolve()).as("check concaternation").isEqualTo("fish|gofer|beaver");
    }

    @Test
    void nonDocumentOperatorValue() {
        List<String> array = Lists.newArrayList("fish", "gofer", "beaver");

        Join join = new JoinBuilder(ReflectiveTransformerRegistry.getInstance()).array(array).build();

        assertThat(join.resolve()).as("check concaternation").isEqualTo("fishgoferbeaver");
    }

    @Test
    void timestamp() {
        int i = 333;
        Timestamp timestamp = new TimestampBuilder(ReflectiveTransformerRegistry.getInstance()).i(i).build();

        assertThat(timestamp.resolve().getInc()).as("check increment value").isEqualTo(i);
    }

    @Test
    void numberDecimal() {
        long min = -20L;
        long max = 25L;
        int fixed = 2;

        NumberDecimal decimal = new NumberDecimalBuilder(ReflectiveTransformerRegistry.getInstance()).fixed(fixed).min(min).max(max).build();

        assertThat(decimal.resolve()).as("check value").isBetween(BigDecimal.valueOf(min), BigDecimal.valueOf(max));
        assertThat(decimal.resolve().scale()).as("check scale").isEqualTo(fixed);
    }

    @Test
    void pick() {
        List<String> array = Lists.newArrayList("fish", "turtle", "badger");
        int element = 2;

        Pick pick = new PickBuilder(ReflectiveTransformerRegistry.getInstance()).array(array).element(element).build();

        assertThat(pick.resolve()).as("correct element").isEqualTo(array.get(element));
    }

    @Test
    void character() {
        String pool = "ABCDE04[]";

        Character character = new CharacterBuilder(ReflectiveTransformerRegistry.getInstance()).pool(pool).build();
        assertThat(character.resolve()).as("check pool").isIn(asCharacterList(pool));

        character = new CharacterBuilder(ReflectiveTransformerRegistry.getInstance()).numeric(true).build();
        assertThat(character.resolve()).as("check numeric").matches(java.lang.Character::isDigit);

        character = new CharacterBuilder(ReflectiveTransformerRegistry.getInstance()).casing("upper").build();
        assertThat(character.resolve()).as("check upper case").matches(java.lang.Character::isUpperCase);

        character = new CharacterBuilder(ReflectiveTransformerRegistry.getInstance()).casing("lower").build();
        assertThat(character.resolve()).as("check lower case").matches(java.lang.Character::isLowerCase);

        character = new CharacterBuilder(ReflectiveTransformerRegistry.getInstance()).alpha(true).build();
        assertThat(character.resolve()).as("check alpha").matches(java.lang.Character::isLetter);
    }

    @Test
    void string() {
        String pool = "ABCDE04[]";
        int length = 7;

        StringOp stringOp = new StringOpBuilder(ReflectiveTransformerRegistry.getInstance()).pool(pool).length(length).build();

        assertThat(stringOp.resolve()).as("size").hasSize(length);

        for (char c : stringOp.resolve().toCharArray()) {
            assertThat(c).isIn(asCharacterList(pool));
        }

        logger.debug(stringOp.resolve());
    }

    @Test
    void month() {
        Month month = new MonthBuilder(ReflectiveTransformerRegistry.getInstance()).build();
        assertThat(month.resolve()).as("full month").isNotNull();
        logger.debug(month.resolve());

        month = new MonthBuilder(ReflectiveTransformerRegistry.getInstance()).full(false).build();
        assertThat(month.resolve()).as("short month").isNotNull();
        logger.debug(month.resolve());
    }

    @Test
    void weekday() {
        Weekday weekday = new WeekdayBuilder(ReflectiveTransformerRegistry.getInstance()).build();
        assertThat(weekday.resolve()).as("full weekday").isNotNull();
        logger.debug(weekday.resolve());

        weekday = new WeekdayBuilder(ReflectiveTransformerRegistry.getInstance()).weekday_only(true).build();
        assertThat(weekday.resolve()).as("short weekday").isNotNull();
        logger.debug(weekday.resolve());
    }

    @Test
    void url() {
        String domain = "www.socialradar.com";
        String path = "images";

        Url url = new UrlBuilder(ReflectiveTransformerRegistry.getInstance()).domain(domain).path(path).extension(true).build();
        assertThat(url.resolve()).as("all args").startsWith("http://" + domain).contains(path);
        logger.debug(url.resolve());

        url = new UrlBuilder(ReflectiveTransformerRegistry.getInstance()).path(path).extension(true).build();
        assertThat(url.resolve()).as("all args").contains(path);
        logger.debug(url.resolve());

        url = new UrlBuilder(ReflectiveTransformerRegistry.getInstance()).extension(true).build();
        assertThat(url.resolve()).as("all args").isNotNull();
        logger.debug(url.resolve());

        url = new UrlBuilder(ReflectiveTransformerRegistry.getInstance()).build();
        assertThat(url.resolve()).as("all args").isNotNull();
        logger.debug(url.resolve());
    }

    @Test
    void hash() {
        Hash hash = new HashBuilder(ReflectiveTransformerRegistry.getInstance()).input("canibal halibuts").build();
        assertThat(hash.resolve()).as("INT32").isEqualTo(-855357176);

        hash = new HashBuilder(ReflectiveTransformerRegistry.getInstance()).input("canibal halibuts").output(Hash.HashOutput.INT64).build();
        assertThat(hash.resolve()).as("INT64").isEqualTo(-3673731096897361255L);

        hash = new HashBuilder(ReflectiveTransformerRegistry.getInstance()).input("canibal halibuts").output(Hash.HashOutput.HEX).build();
        assertThat(hash.resolve()).as("HEX").isEqualTo("cd04490819206a990ed5b165a35598a4");
    }

    @Test
    @SuppressWarnings("unchecked")
    void polygon() {
        int corners = 5;
        List<Number> long_lim = asList(0d, 100d);
        List<Number> lat_lim = asList(-200, 0);
        Polygon polygon = new PolygonBuilder(ReflectiveTransformerRegistry.getInstance())
                .corners(corners)
                .long_lim(long_lim)
                .lat_lim(lat_lim)
                .build();

        logger.info(polygon.resolve().toJson(new DocumentCodec(TemplateCodec.getCodeRegistry())));

        assertThat(polygon.resolve().get("type")).as("geo type").isEqualTo("Polygon");
        assertThat(polygon.resolve().get("coordinates")).as("coordinates class").isInstanceOf(List.class);
        assertThat((List) polygon.resolve().get("coordinates")).hasSize(1);

        List<FlsUtil.Point> polygonPoints = (List<FlsUtil.Point>) ((List) polygon.resolve().get("coordinates")).get(0);

        assertThat(polygonPoints).hasSize(corners+1);
        assertThat(polygonPoints.get(0)).as("first and last points equivalent").isEqualTo(polygonPoints.get(polygonPoints.size()-1));
        assertBounds(polygonPoints, long_lim, lat_lim);
    }

    @Test
    void coordinates() {
        List<Number> long_lim = asList(0d, 10d);
        List<Number> lat_lim = asList(-20, 0);

        Coordinates coordinates = new CoordinatesBuilder(ReflectiveTransformerRegistry.getInstance()).long_lim(long_lim).lat_lim(lat_lim).build();

        assertThat(coordinates.resolve()).isNotNull();
        assertBounds(coordinates.resolve(), long_lim, lat_lim);
    }

    @Test
    void point() {
        List<Number> long_lim = asList(0d, 10d);
        List<Number> lat_lim = asList(-20, 0);

        Point point = new PointBuilder(ReflectiveTransformerRegistry.getInstance()).long_lim(long_lim).lat_lim(lat_lim).build();

        assertThat(point.resolve()).isNotNull();
        assertThat(point.resolve().get("type")).as("geo type").isEqualTo("Point");
        assertThat(point.resolve().get("coordinates")).as("coordinates class type").isInstanceOf(FlsUtil.Point.class);

        assertBounds((FlsUtil.Point)point.resolve().get("coordinates"), long_lim, lat_lim);
    }

    @Test
    @SuppressWarnings("unchecked")
    void lineString() {
        List<Number> long_lim = asList(0d, 10d);
        List<Number> lat_lim = asList(-20, 0);

        LineString lineString = new LineStringBuilder(ReflectiveTransformerRegistry.getInstance()).long_lim(long_lim).lat_lim(lat_lim).build();

        assertThat(lineString.resolve()).isNotNull();
        assertThat(lineString.resolve().get("type")).as("geo type").isEqualTo("LineString");
        assertThat(lineString.resolve().get("coordinates")).as("coordinates class type").isInstanceOf(List.class);

        assertBounds((List)lineString.resolve().get("coordinates"), long_lim, lat_lim);
    }

    @Test
    void sentence() {
        Sentence sentence = new SentenceBuilder(ReflectiveTransformerRegistry.getInstance()).build();

        assertThat(sentence.resolve()).isNotNull();
        assertThat(sentence.resolve()).isInstanceOf(String.class);
    }

    private static void assertBounds(List<FlsUtil.Point> points, List<Number> longBounds, List<Number> latBounds) {
        points.forEach(point -> assertBounds(point, longBounds, latBounds));
    }

    private static void assertBounds(FlsUtil.Point point, List<Number> longBounds, List<Number> latBounds) {
        assertThat(point.getX()).as("longitude").isBetween(longBounds.get(0).doubleValue(), longBounds.get(1).doubleValue());
        assertThat(point.getY()).as("latitude").isBetween(latBounds.get(0).doubleValue(), latBounds.get(1).doubleValue());
    }

    private static List<java.lang.Character> asCharacterList(String s) {
        List<java.lang.Character> chars = new ArrayList<>();
        for (char c : s.toCharArray()) {
            chars.add(c);
        }
        return chars;
    }
}
