package com.dft.katana.model.invoice;

import lombok.Data;

import java.util.List;

@Data
public class NsResource {

    private String externalId;
    private String id;
    private List<Link> links;
    private String refName;
}
