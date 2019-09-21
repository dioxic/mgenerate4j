package uk.dioxic.mgenerate.core.operator.faker.internet;

import org.junit.jupiter.api.Test;
import uk.dioxic.mgenerate.core.transformer.ReflectiveTransformerRegistry;

import static org.assertj.core.api.Assertions.assertThat;

class UrlTest {
    @Test
    void url() {
        java.lang.String domain = "www.socialradar.com";
        java.lang.String path = "images";

        Url url = new UrlBuilder(ReflectiveTransformerRegistry.getInstance()).domain(domain).path(path).extension(true).build();
        assertThat(url.resolve()).as("all args").startsWith("http://" + domain).contains(path);

        url = new UrlBuilder(ReflectiveTransformerRegistry.getInstance()).path(path).extension(true).build();
        assertThat(url.resolve()).as("all args").contains(path);

        url = new UrlBuilder(ReflectiveTransformerRegistry.getInstance()).extension(true).build();
        assertThat(url.resolve()).as("all args").isNotNull();

        url = new UrlBuilder(ReflectiveTransformerRegistry.getInstance()).build();
        assertThat(url.resolve()).as("all args").isNotNull();
    }
}
