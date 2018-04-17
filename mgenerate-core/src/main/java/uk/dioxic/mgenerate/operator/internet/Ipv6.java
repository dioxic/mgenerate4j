package uk.dioxic.mgenerate.operator.internet;

import uk.dioxic.mgenerate.util.FakerUtil;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Ipv6 implements Resolvable<String> {

    @Override
    public String resolve() {
        return Stream.generate(() -> Integer.toHexString(FakerUtil.random().nextInt(16)))
                .limit(8)
                .collect(Collectors.joining(":"));
    }

}
