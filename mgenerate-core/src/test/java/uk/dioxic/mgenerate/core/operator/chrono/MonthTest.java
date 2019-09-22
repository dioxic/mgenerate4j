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

class MonthTest {

    private LocalDateTime ldt = LocalDateTime.now();

    @Test
    void resolve_RandomNumeric() {
        Month month = new MonthBuilder(ReflectiveTransformerRegistry.getInstance()).build();

        assertThat(month.resolveInternal()).isInstanceOf(Integer.class);
        assertThat((Integer)month.resolveInternal()).isBetween(1,12);
    }

    @Test
    void resolve_RandomLongText() {
        Month month = new MonthBuilder(ReflectiveTransformerRegistry.getInstance())
                .type(DateDisplayType.LONG_TEXT)
                .build();

        assertThat(month.resolveInternal()).isInstanceOf(String.class);
        assertThat((String)month.resolveInternal()).isIn(Arrays.asList(DateFormatSymbols.getInstance().getMonths()));
    }

    @Test
    void resolve_RandomShortText() {
        Month month = new MonthBuilder(ReflectiveTransformerRegistry.getInstance())
                .type(DateDisplayType.SHORT_TEXT)
                .build();

        assertThat(month.resolveInternal()).isInstanceOf(String.class);
        assertThat((String)month.resolveInternal()).isIn(Arrays.asList(DateFormatSymbols.getInstance().getShortMonths()));
    }

    @Test
    void resolve_FromDateNumeric() {
        Month month = new MonthBuilder(ReflectiveTransformerRegistry.getInstance())
                .date(ldt)
                .build();

        assertThat(month.resolveInternal()).isEqualTo(ldt.getMonth().getValue());
    }

    @Test
    void resolve_FromDateLongText() {
        Month month = new MonthBuilder(ReflectiveTransformerRegistry.getInstance())
                .date(ldt)
                .type(DateDisplayType.LONG_TEXT)
                .build();

        assertThat(month.resolveInternal()).isEqualTo(ldt.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
    }

    @Test
    void resolve_FromDateShortText() {
        Month month = new MonthBuilder(ReflectiveTransformerRegistry.getInstance())
                .date(ldt)
                .type(DateDisplayType.SHORT_TEXT)
                .build();

        assertThat(month.resolveInternal()).isEqualTo(ldt.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
    }

    @Test
    void resolve_FromDateWithLocaleLongText() {
        Month month = new MonthBuilder(ReflectiveTransformerRegistry.getInstance())
                .date(ldt)
                .locale(Locale.ITALIAN)
                .type(DateDisplayType.LONG_TEXT)
                .build();

        assertThat(month.resolveInternal()).isEqualTo(ldt.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN));
    }
}
