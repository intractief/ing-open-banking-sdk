package com.ing.developer.codegen;

import static java.util.function.Predicate.not;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.parameters.Parameter;

import java.util.*;
import org.openapitools.codegen.languages.JavaClientCodegen;

public class JavaOpenBankingCodegen extends JavaClientCodegen {
  private final static List<String> EXCLUDED_PARAMS = List.of("Signature", "Digest", "Date");
  private final static List<String> NON_REQUIRED_PARAMS = List.of("Authorization", "X-JWS-Signature");

  @Override
  public String getName() {
    return "Java";
  }

  @Override
  public String getHelp() {
    return "Generates a Java client for ING Open Banking APIs";
  }

  @Override
  public void preprocessOpenAPI(OpenAPI openAPI) {
    Optional.ofNullable(openAPI)
            .stream()
            .map(OpenAPI::getPaths)
            .map(Map::values)
            .flatMap(Collection::stream)
            .map(PathItem::readOperations)
            .flatMap(Collection::stream)
            .forEach(JavaOpenBankingCodegen::updateOperationParameters);
    super.preprocessOpenAPI(openAPI);
  }

  private static void updateOperationParameters(Operation operation) {
    Optional.ofNullable(operation.getParameters())
            .map(JavaOpenBankingCodegen::updateParameters)
            .ifPresent(operation::setParameters);
  }

  private static List<Parameter> updateParameters(List<Parameter> input) {
    List<Parameter> includedParams = input.stream()
            .filter(not(p -> EXCLUDED_PARAMS.contains(p.getName())))
            .toList();
    includedParams.stream()
            .filter(p -> NON_REQUIRED_PARAMS.contains(p.getName()))
            .forEach(p -> p.required(false));
    return includedParams;
  }
}
