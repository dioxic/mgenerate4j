package uk.dioxic.mgenerate.core.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ByteUtilTest {

    @Test
    void bitShift_Integer0Bits_throwsArguementException() {
        Assertions.assertThatThrownBy(() -> ByteUtil.bitShift(123, 0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void bitShift_Integer1Bits_Correct() {
        int res = ByteUtil.bitShift(1, 1);
        Assertions.assertThat(res).as("bitshift by 1").isEqualTo(2);
    }

    @Test
    void bitShift_Integer8Bits_Correct() {
        int res = ByteUtil.bitShift(255, 8);
        Assertions.assertThat(res).as("bitshift by 8").isEqualTo(65280);
    }

    @Test
    void bitShift_Integer16Bits_Correct() {
        int res = ByteUtil.bitShift(256, 10);
        Assertions.assertThat(res).as("bitshift by 8").isEqualTo(262144);
    }

}
