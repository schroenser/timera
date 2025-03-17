package de.schroenser.timera;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.templaterenderer.TemplateRenderer;

@RequiredArgsConstructor
public class TimeraServlet extends HttpServlet
{
    private static final String TEMPLATE = "/templates/timera.vm";

    @ComponentImport
    private final TemplateRenderer templateRenderer;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        Map<String, Object> context = Collections.emptyMap();
        templateRenderer.render(TEMPLATE, context, resp.getWriter());
    }
}
