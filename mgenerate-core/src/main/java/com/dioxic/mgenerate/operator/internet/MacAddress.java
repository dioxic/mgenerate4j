package com.dioxic.mgenerate.operator.internet;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.annotation.Operator;
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
