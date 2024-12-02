package de.schroenser.timera.jira;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.schroenser.timera.util.LoggingClientHttpRequestInterceptor;

@Configuration
@EnableCaching
class JiraConfiguration
{
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer()
    {
        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL)
            .serializers(new JiraOffsetDateTimeJsonSerializer());
    }

    @Bean
    public RestClient restClient(
        @Value("${jira.base-url}") String baseUrl, @Value("${jira.token}") String token, ObjectMapper objectMapper)
    {
        return RestClient.builder()
            .baseUrl(baseUrl)
            .messageConverters(c -> {
                c.removeIf(MappingJackson2HttpMessageConverter.class::isInstance);
                c.add(new MappingJackson2HttpMessageConverter(objectMapper));
            })
            .defaultHeader("User-Agent", "Timera/1.0 (Sven Haberer - DDS)")
            .defaultHeader("Authorization", "Bearer " + token)
            .build();
    }
}
