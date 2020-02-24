package uk.dioxic.mgenerate.core.operator.faker.internet;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Ipv6 extends AbstractOperator<String> {

    @Override
    public String resolveInternal() {
        return Stream.generate(() -> Integer.toHexString(FakerUtil.random().nextInt(0xFFFF)))
                .limit(8)
                .collect(Collectors.joining(":"));
    }

}
