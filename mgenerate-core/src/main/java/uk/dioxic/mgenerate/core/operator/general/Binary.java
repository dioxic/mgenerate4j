package uk.dioxic.mgenerate.core.operator.general;

import org.bson.BsonBinary;
import uk.dioxic.mgenerate.common.Resolvable;
import uk.dioxic.mgenerate.common.Wrapper;
import uk.dioxic.mgenerate.common.annotation.Operator;
import uk.dioxic.mgenerate.common.annotation.OperatorProperty;
import uk.dioxic.mgenerate.core.operator.AbstractOperator;
import uk.dioxic.mgenerate.core.util.FakerUtil;

@Operator({"bin", "mgenBinary"})
public class Binary extends AbstractOperator<BsonBinary> {

    @OperatorProperty
    Resolvable<Integer> size = Wrapper.wrap(1024);

    @Override
    protected BsonBinary resolveInternal() {
        byte[] b = new byte[size.resolve()];
        FakerUtil.random().nextBytes(b);

        return new BsonBinary(b);
    }
}
