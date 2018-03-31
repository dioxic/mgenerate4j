package com.dioxic.mgenerate.operator.internet;

import com.dioxic.mgenerate.FakerUtil;
import com.dioxic.mgenerate.Resolvable;
import com.dioxic.mgenerate.annotation.Operator;
import com.dioxic.mgenerate.annotation.OperatorProperty;

@Operator
public class Url implements Resolvable<String> {

    @OperatorProperty
    Resolvable<String> domain;

    @OperatorProperty
    Resolvable<String> path;

    @OperatorProperty
    Resolvable<Object> extension;

    @Override
    public String resolve() {
        StringBuilder sb = new StringBuilder("http://");

        if (domain != null){
            sb.append(domain.resolve());
        }
        else {
            sb.append(FakerUtil.instance().internet().url());
        }

        sb.append('/');

        if (path != null) {
            sb.append(path.resolve());
        }
        else{
            sb.append(FakerUtil.instance().lorem().word());
        }

        if (extension != null) {
            sb.append('/');
            sb.append(FakerUtil.instance().lorem().word());
            sb.append('.');
            sb.append(extension.resolve());
        }

        return sb.toString();
    }

}
