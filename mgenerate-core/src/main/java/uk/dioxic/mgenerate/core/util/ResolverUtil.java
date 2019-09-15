package uk.dioxic.mgenerate.core.util;

import java.util.ArrayList;
import java.util.List;

public class ResolverUtil {

    public static List<?> getWeightedArray(List<?> from, List<Integer> weights) {
        if (weights != null) {
            if (from.size() != weights.size()) {
                throw new IllegalArgumentException("length of array and weights must match");
            }
            List<Object> fromList = new ArrayList<>();
            for (int i = 0; i < from.size(); i++) {
                for (int j = 0; j < weights.get(i); j++) {
                    fromList.add(from.get(i));
                }
            }
            from = fromList;
        }
        return from;
    }
}
