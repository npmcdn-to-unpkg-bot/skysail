package io.skysail.domain.html;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import io.skysail.domain.html.HtmlPolicy;

public class HtmlPolicyTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testNoHtmlPolicy() {
        HtmlPolicy noHtml = HtmlPolicy.NO_HTML;
        assertThat(noHtml.getAllowedElements().size(), is(0));
    }

    @Test
    public void testDefaultHtmlPolicy() {
        HtmlPolicy noHtml = HtmlPolicy.DEFAULT_HTML;
        assertThat(noHtml.getAllowedElements().size(), is(greaterThan(10)));
        assertThat(noHtml.getAllowedAttributes().size(), is(greaterThan(0)));
    }

}
