package org.home.MoneyTransfer.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Builder
public class ApiRequest {

    private HttpStatus statusTemp;
    private String errMessageTemp;
    private String operationId;

    public ResponseEntity getResponseEntity() {
        Map<String, String> bodyMap = new HashMap<>(1);
        if (errMessageTemp != null)
            bodyMap.put("error", errMessageTemp);
        else
            bodyMap.put("operationID", operationId);
        return ResponseEntity.status(statusTemp).body(bodyMap);
    }

}
