package com.ing.developer.common.exceptions.http;

public class OpenBankingHttpNotFoundException extends OpenBankingHttpException {

    public <T> OpenBankingHttpNotFoundException(String typeName, String message) {
        super("Resource: " + typeName + " not found: "+message);
    }
}