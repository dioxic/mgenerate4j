package uk.dioxic.mgenerate.core.operator.chrono;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.operator.type.DateDisplayType;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

class DayOfWeekTest {

    private LocalDateTime ldt = LocalDateTime.now();

    @Test
    void resolve_RandomNumeric() {
        DayOfWeek dow = new DayOfWeekBuilder(ReflectiveTransformerRegistry.getInstance()).build();

        assertThat(dow.resolveInternal()).isInstanceOf(Integer.class);
        assertThat((Integer)dow.resolveInternal()).isBetween(1,7);
    }

    @Test
    void resolve_RandomLongText() {
        DayOfWeek dow = new DayOfWeekBuilder(ReflectiveTransformerRegistry.getInstance())
                .type(DateDisplayType.LONG_TEXT)
                .build();

        assertThat(dow.resolveInternal()).isInstanceOf(String.class);
        assertThat((String)dow.resolveInternal()).isIn(Arrays.asList(DateFormatSymbols.getInstance().getWeekdays()));
    }

    @Test
    void resolve_RandomShortText() {
        DayOfWeek dow = new DayOfWeekBuilder(ReflectiveTransformerRegistry.getInstance())
                .type(DateDisplayType.SHORT_TEXT)
                .build();

        assertThat(dow.resolveInternal()).isInstanceOf(String.class);
        assertThat((String)dow.resolveInternal()).isIn(Arrays.asList(DateFormatSymbols.getInstance().getShortWeekdays()));
    }

    @Test
    void resolve_FromDateNumeric() {
        DayOfWeek dow = new DayOfWeekBuilder(ReflectiveTransformerRegistry.getInstance())
                .date(ldt)
                .build();

        assertThat(dow.resolveInternal()).isEqualTo(ldt.getDayOfWeek().getValue());
    }

    @Test
    void resolve_FromDateLongText() {
        DayOfWeek dow = new DayOfWeekBuilder(ReflectiveTransformerRegistry.getInstance())
                .date(ldt)
                .type(DateDisplayType.LONG_TEXT)
                .build();

        assertThat(dow.resolveInternal()).isEqualTo(ldt.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
    }

    @Test
    void resolve_FromDateShortText() {
        DayOfWeek dow = new DayOfWeekBuilder(ReflectiveTransformerRegistry.getInstance())
                .date(ldt)
                .type(DateDisplayType.SHORT_TEXT)
                .build();

        assertThat(dow.resolveInternal()).isEqualTo(ldt.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
    }

    @Test
    void resolve_FromDateWithLocaleLongText() {
        DayOfWeek dow = new DayOfWeekBuilder(ReflectiveTransformerRegistry.getInstance())
                .date(ldt)
                .locale(Locale.FRENCH)
                .type(DateDisplayType.LONG_TEXT)
                .build();

        assertThat(dow.resolveInternal()).isEqualTo(ldt.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRENCH));
    }
}
