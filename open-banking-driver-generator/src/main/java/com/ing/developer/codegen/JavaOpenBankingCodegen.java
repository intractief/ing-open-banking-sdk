package com.ing.developer.codegen;

import static java.util.function.Predicate.not;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.parameters.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.openapitools.codegen.languages.JavaClientCodegen;

public class JavaOpenBankingCodegen extends JavaClientCodegen {
  private static final List<String> EXCLUDED_PARAMS = Arrays.asList("Signature", "Digest", "Date");
  private static final List<String> NON_REQUIRED_PARAMS = Arrays.asList("Authorization", "X-JWS-Signature");

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
    if (openAPI != null && openAPI.getPaths() != null) {
      for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
        for (Operation operation : entry.getValue().readOperations()) {
          if (operation.getParameters() != null) {
            List<Parameter> includedParams = operation.getParameters().stream()
                .filter(not(p -> EXCLUDED_PARAMS.contains(p.getName())))
                .collect(Collectors.toList());
            includedParams.stream()
                .filter(p -> NON_REQUIRED_PARAMS.contains(p.getName()))
                .forEach(p -> p.required(false));
            operation.setParameters(includedParams);
          }
        }
      }
    }
    super.preprocessOpenAPI(openAPI);
  }
}
