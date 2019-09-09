package uk.dioxic.mgenerate.core.operator.internet;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Operator
public class Ip implements Resolvable<String> {

    @Override
    public String resolve() {
        return Stream.generate(() -> String.valueOf(FakerUtil.random().nextInt(254) + 2))
                .limit(4)
                .collect(Collectors.joining("."));
    }

}
