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
public class WorklogDto
{
    @XmlElement
    private long issueId;

    @XmlElement
    private long worklogId;

    @XmlElement
    private String issueKey;

    @XmlElement
    private String issueSummary;

    @XmlElement
    private String start;

    @XmlElement
    private String end;

    @XmlElement
    private String worklogComment;
}
