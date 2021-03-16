/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (4.3.1).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package org.openapitools.api;

import org.openapitools.model.InlineObject1;
import org.openapitools.model.InlineResponse200;
import org.openapitools.model.InlineResponse400;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-02T18:16:41.844+03:00[Europe/Moscow]")

@Validated
@Api(value = "confirmOperation", description = "the confirmOperation API")
public interface ConfirmOperationApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /confirmOperation : Confirm operation
     * Confirming operataion with code
     *
     * @param inlineObject1  (required)
     * @return Success confirmation (status code 200)
     *         or Error input data (status code 400)
     *         or Error confirmation (status code 500)
     */
    @ApiOperation(value = "Confirm operation", nickname = "confirmOperationPost", notes = "Confirming operataion with code", response = InlineResponse200.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success confirmation", response = InlineResponse200.class),
        @ApiResponse(code = 400, message = "Error input data", response = InlineResponse400.class),
        @ApiResponse(code = 500, message = "Error confirmation", response = InlineResponse400.class) })
    @RequestMapping(value = "/confirmOperation",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    default ResponseEntity<InlineResponse200> confirmOperationPost(@ApiParam(value = "" ,required=true )  @Valid @RequestBody InlineObject1 inlineObject1) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"operationId\" : \"operationId\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
