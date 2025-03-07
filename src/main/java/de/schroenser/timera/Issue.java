package de.schroenser.timera;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@XmlRootElement
public class Issue
{
    @XmlElement
    private long id;

    @XmlElement
    private String key;

    @XmlElement
    private String summary;
}
