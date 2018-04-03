package com.dioxic.mgenerate.operator.internet;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.faker.resolvable.Resolvable;

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
