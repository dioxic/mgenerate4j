package uk.dioxic.mgenerate.operator.internet;

import uk.dioxic.mgenerate.FakerUtil;
import uk.dioxic.mgenerate.OperatorFactory;
import uk.dioxic.mgenerate.annotation.Operator;
import uk.dioxic.mgenerate.annotation.OperatorProperty;
import uk.dioxic.mgenerate.operator.text.Word;
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
            sb.append(FakerUtil.getValue("lorem.words"));
            sb.append('.');
            sb.append(FakerUtil.getValue("file.extension"));
        }

        return sb.toString();
    }

}
