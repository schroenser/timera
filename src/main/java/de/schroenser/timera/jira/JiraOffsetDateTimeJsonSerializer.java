package de.schroenser.timera.jira;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

/**
 * Yes, there is the {@link OffsetDateTimeSerializer}. Howver, that one does
 * not use {@code .SSSZ} but omits the millis if possible and uses {@code Z}(ulu) instead of {@code +0000} in the
 * resulting string, both of which Jira does not accept.
 */
class JiraOffsetDateTimeJsonSerializer extends JsonSerializer<OffsetDateTime>
{
    private static final DateTimeFormatter JIRA_OFFSET_DATE_TIME_FORMATER = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Override
    public Class<OffsetDateTime> handledType()
    {
        return OffsetDateTime.class;
    }

    @Override
    public void serialize(OffsetDateTime value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException
    {
        jsonGenerator.writeString(JIRA_OFFSET_DATE_TIME_FORMATER.format(value));
    }
}
