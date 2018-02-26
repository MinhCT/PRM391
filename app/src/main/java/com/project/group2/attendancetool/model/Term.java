package com.project.group2.attendancetool.model;

/**
 * Model class representing a term
 */
public class Term {
    private String TermId;
    private String TermName;

    public Term(String termId, String termName) {
        TermId = termId;
        TermName = termName;
    }

    public String getTermId() {
        return TermId;
    }

    public String getTermName() {
        return TermName;
    }
}
