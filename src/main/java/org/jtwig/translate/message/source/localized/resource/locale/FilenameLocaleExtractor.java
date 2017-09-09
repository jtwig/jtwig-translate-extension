package org.jtwig.translate.message.source.localized.resource.locale;

import com.google.common.base.Optional;
import org.jtwig.environment.Environment;
import org.jtwig.resource.metadata.ResourceMetadata;
import org.jtwig.translate.TranslateExtension;
import org.jtwig.translate.locale.LocaleResolver;

import java.io.File;
import java.net.URL;
import java.util.Locale;

/**
 * Filenames with the pattern name.locale.extension.
 * For example:
 *
 * - messages.en.properties
 * - test.pt.xml
 * - origin.fr.xliff
 */
public class FilenameLocaleExtractor {
    private static final String FILE_PROTOCOL = "file";
    private final RetrieveLocaleExpressionFromFilename retrieveLocaleExpressionFromFilename;

    public FilenameLocaleExtractor(RetrieveLocaleExpressionFromFilename retrieveLocaleExpressionFromFilename) {
        this.retrieveLocaleExpressionFromFilename = retrieveLocaleExpressionFromFilename;
    }

    public Optional<Locale> extractLocale(Environment environment, ResourceMetadata metadata) {
        Optional<URL> urlOptional = metadata.toUrl();
        if (urlOptional.isPresent()) {
            URL url = urlOptional.get();
            if (FILE_PROTOCOL.equals(url.getProtocol())) {
                File file = new File(url.getFile());
                Optional<String> expression = retrieveLocaleExpressionFromFilename.retrieveLocaleExpression(file.getName());
                if (expression.isPresent()) {
                    LocaleResolver localeResolver = TranslateExtension.enviroment(environment).getLocaleResolver();
                    return Optional.of(localeResolver.resolve(expression.get()));
                }
            }
        }
        return Optional.absent();
    }

}
