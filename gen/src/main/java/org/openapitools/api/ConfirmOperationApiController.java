package org.openapitools.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-03-02T18:16:41.844+03:00[Europe/Moscow]")

@Controller
@RequestMapping("${openapi.transferMoney.base-path:/transfer}")
public class ConfirmOperationApiController implements ConfirmOperationApi {

    private final NativeWebRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ConfirmOperationApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
