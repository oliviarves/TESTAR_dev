package nl.ou.testar.temporal.structure;

import java.util.List;

public class TemporalTrace implements Cloneable{


    public TemporalTrace() {
    }
    private List<TemporalTraceEvent> traceEvents;
    private String sequenceID;
    private  String TestSequenceNode;
    private String runDate;
    private Long transitionCount;

    public String getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(String sequenceID) {
        this.sequenceID = sequenceID;
    }

    public String getTestSequenceNode() {
        return TestSequenceNode;
    }

    public void setTestSequenceNode(String testSequenceNode) {
        this.TestSequenceNode = testSequenceNode;
    }

    public List<TemporalTraceEvent> getTraceEvents() {
        return traceEvents;
    }

    public void setTraceEvents(List<TemporalTraceEvent> traceEvents) {
        this.traceEvents = traceEvents;
    }
    public String getRunDate() {
        return runDate;
    }

    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }
    public Long getTransitionCount() {
        return transitionCount;
    }

    public void setTransitionCount(Long transitionCount) {
        this.transitionCount = transitionCount;
    }
    public Object clone() throws            CloneNotSupportedException
    {
        return super.clone();
    }

}