package uk.dioxic.mgenerate.operator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.dioxic.faker.resolvable.FakerStringResolver;
import uk.dioxic.mgenerate.FakerUtil;

public class FakerStringTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void fakerStringTest() {
        FakerStringResolver resolver = new FakerStringResolver("#{name.first_name} von #{name.last_name}", FakerUtil.instance());

        Assertions.assertThat(resolver.resolve()).as("not null").isNotNull();
        Assertions.assertThat(resolver.resolve()).as("placeholders replaced").doesNotContain("#{");

        logger.debug(resolver.resolve());

    }
}
