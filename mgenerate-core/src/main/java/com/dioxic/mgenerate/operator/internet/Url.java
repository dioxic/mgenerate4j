package com.dioxic.mgenerate.operator.internet;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.OperatorFactory;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;
import com.dioxic.mgenerate.operator.text.Word;
import uk.dioxic.faker.resolvable.Resolvable;

@Operator
public class Url implements Resolvable<String> {

    @OperatorProperty
    Resolvable<String> domain = new Domain();

    @OperatorProperty
    Resolvable<String> path = new Word();

    @OperatorProperty
    Resolvable<Boolean> extension = OperatorFactory.wrap(Boolean.FALSE);

    @Override
    public String resolve() {
        StringBuilder sb = new StringBuilder("http://");
        sb.append(domain.resolve());
        sb.append('/');
        sb.append(path.resolve());

        if (extension != null) {
            sb.append('/');
            sb.append(FakerUtil.getFake("lorem.words"));
            sb.append('.');
            sb.append(FakerUtil.getFake("file.extension"));
        }

        return sb.toString();
    }

}
