package com.ing.developer.common.exceptions.http;

public class OpenBankingHttpForbiddenException extends OpenBankingHttpException {

    public <T> OpenBankingHttpForbiddenException(String typeName, String message) {
        super("Forbidden for resource: " + typeName + " not found: "+message);
    }
}