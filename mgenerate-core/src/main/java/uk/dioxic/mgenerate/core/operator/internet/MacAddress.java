package uk.dioxic.mgenerate.core.operator.internet;

import uk.dioxic.mgenerate.core.util.FakerUtil;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class MacAddress implements Resolvable<String> {

    @Override
    public String resolve() {
        return Stream.generate(() -> Integer.toHexString(FakerUtil.random().nextInt(16)))
                .limit(6)
                .collect(Collectors.joining(":"));
    }

}
