package uk.dioxic.mgenerate.core.operator.faker.internet;

import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Ip extends AbstractOperator<String> {

    @Override
    public String resolveInternal() {
        return Stream.generate(() -> String.valueOf(FakerUtil.random().nextInt(254) + 2))
                .limit(4)
                .collect(Collectors.joining("."));
    }

}
