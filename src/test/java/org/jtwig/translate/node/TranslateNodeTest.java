package org.jtwig.translate.node;

import junit.framework.TestCase;
import org.jtwig.context.RenderContext;
import org.jtwig.context.model.EscapeMode;
import org.jtwig.model.position.Position;
import org.jtwig.model.tree.Node;
import org.jtwig.render.Renderable;
import org.jtwig.render.StringBuilderRenderResult;
import org.jtwig.render.impl.StringRenderable;
import org.jtwig.value.configuration.DefaultValueConfiguration;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class TranslateNodeTest extends TestCase {
    private final Position position = mock(Position.class);
    private final Node content = mock(Node.class);
    private final RenderContext renderContext = mock(RenderContext.class, RETURNS_DEEP_STUBS);
    private TranslateNode underTest;

    @Before
    public void setContent() throws Exception {
        when(renderContext.environment().valueConfiguration()).thenReturn(new DefaultValueConfiguration());
        when(renderContext.nodeRenderer().render(content)).thenReturn(new StringRenderable("content", EscapeMode.NONE));
    }

    @Test
    public void renderWithContentOnly() throws Exception {
        underTest = new TranslateNode(position, content, null, null);
//        when(renderContext.environment().renderConfiguration().currentLocaleSupplier().get()).thenReturn(Locale.ENGLISH);
//        when(renderContext.environment().messageResolver()
//                .resolve(eq(Locale.ENGLISH), eq("content"), any(MessageDecorator.class)))
//                .thenReturn(Optional.<String>absent());

        Renderable render = underTest.render(renderContext);

        assertThat(render.appendTo(new StringBuilderRenderResult()).content(), is("content"));
    }

    @Test
    public void renderWithContentAndTranslationOnly() throws Exception {
        underTest = new TranslateNode(position, content, null, null);
//        when(renderContext.environment().renderConfiguration().currentLocaleSupplier().get()).thenReturn(Locale.ENGLISH);
//        when(renderContext.environment().messageResolver()
//                .resolve(eq(Locale.ENGLISH), eq("content"), any(MessageDecorator.class)))
//                .thenReturn(Optional.of(""));

        Renderable render = underTest.render(renderContext);

        assertThat(render.appendTo(new StringBuilderRenderResult()).content(), is(""));
    }

}