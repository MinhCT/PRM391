package com.project.group2.attendancetool.enums;

/**
 * List out request codes to be used by activities
 */
public enum ERequestCode {
    RC_SIGN_IN(10000);

    private int code;
    ERequestCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
