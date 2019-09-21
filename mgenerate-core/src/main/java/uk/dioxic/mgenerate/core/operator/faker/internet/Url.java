package uk.dioxic.mgenerate.core.operator.faker.internet;

import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.text.Word;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator
public class Url implements Resolvable<String> {

    @OperatorProperty
    Resolvable<String> domain = new Domain();

    @OperatorProperty
    Resolvable<String> path = new Word();

    @OperatorProperty
    Resolvable<Boolean> extension = Wrapper.wrap(Boolean.FALSE);

    @Override
    public String resolve() {
        StringBuilder sb = new StringBuilder("http://");
        sb.append(domain.resolve());
        sb.append('/');
        sb.append(path.resolve());

        if (extension != null) {
            sb.append('/');
            sb.append(FakerUtil.getValue("lorem.words"));
            sb.append('.');
            sb.append(FakerUtil.getValue("file.extension"));
        }

        return sb.toString();
    }

}
