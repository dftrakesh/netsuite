package com.dft.netsuite.model.invoice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdFiled {

    private String id;
    private String refName;
}