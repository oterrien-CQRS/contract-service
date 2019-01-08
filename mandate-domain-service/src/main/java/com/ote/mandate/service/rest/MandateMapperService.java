package com.ote.mandate.service.rest;

import com.ote.mandate.business.model.aggregate.Contractor;
import com.ote.mandate.business.model.aggregate.Heir;
import com.ote.mandate.business.model.aggregate.Mandate;
import com.ote.mandate.business.model.aggregate.Notary;
import com.ote.mandate.service.rest.payload.ContractorPayload;
import com.ote.mandate.service.rest.payload.HeirPayload;
import com.ote.mandate.service.rest.payload.MandatePayload;
import com.ote.mandate.service.rest.payload.NotaryPayload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MandateMapperService {

    public Mandate convert(MandatePayload payload) {
        if (payload == null) {
            return null;
        }
        Mandate business = new Mandate();
        business.setId(payload.getId());
        business.setBankName(payload.getBankName());
        business.setContractor(convert(payload.getContractor()));
        business.setMainHeir(convert(payload.getMainHeir()));
        business.setNotary(convert(payload.getNotary()));
        business.setOtherHeirs(payload.getOtherHeirs().stream().map(this::convert).collect(Collectors.toList()));
        return business;
    }

    public Set<Heir> convert(List<HeirPayload> payload) {
        if (payload == null) {
            return null;
        }
        Set<Heir> business = payload.stream().map(this::convert).collect(Collectors.toSet());
        return business;
    }

    public Heir convert(HeirPayload payload) {
        if (payload == null) {
            return null;
        }
        Heir business = new Heir();
        business.setName(payload.getName());
        return business;
    }

    public Notary convert(NotaryPayload payload) {
        if (payload == null) {
            return null;
        }
        Notary business = new Notary();
        business.setName(payload.getName());
        return business;
    }

    public Contractor convert(ContractorPayload payload) {
        if (payload == null) {
            return null;
        }
        Contractor business = new Contractor();
        business.setName(payload.getName());
        return business;
    }
}
