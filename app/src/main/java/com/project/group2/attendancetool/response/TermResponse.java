package com.project.group2.attendancetool.response;

import com.project.group2.attendancetool.model.Term;

import java.util.List;

/**
 * Response Model for a list of terms
 */
public class TermResponse {
    private List<Term> Terms;

    public List<Term> getTerms() {
        return Terms;
    }
}
