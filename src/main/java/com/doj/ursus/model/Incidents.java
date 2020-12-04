package com.doj.ursus.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "incidents")
@XmlAccessorType(XmlAccessType.FIELD)
public class Incidents {

    private List<Incident> incident;

    public List<Incident> getIncident() {
        return incident;
    }

    public void setIncident(List<Incident> incident) {
        this.incident = incident;
    }

    @Override
    public String toString() {
        return "Incidents{" +
                "incident=" + incident +
                '}';
    }
}
