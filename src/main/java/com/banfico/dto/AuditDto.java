package com.banfico.dto;

import com.banfico.model.ImportRequestStatus;
import com.banfico.model.RequestAudit;
import com.banfico.model.TransactionNote;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AuditDto {
    private String id;
    private OffsetDateTime requestTimeReceived;
    private OffsetDateTime requestTimeSent;
    private String requestAccountType;
    private String requestType;
    private String requesterMessageIdentification;
    private String bic;
    private String iban;
    private String identificationType;
    private String identificationValue;
    private String secondaryId;
    private OffsetDateTime responseTimeSent;
    private Boolean responseMatched;
    private String responseReasonCode;
    private String requestedOrgName;
    private String requestName;
    private String requestedOrgId;
    private String userId;
    private String userType;
    private String responseUrl;
    private String clientId;
    private String requestStatus;
    private Map<String, String> schemeReasonCode;
    private List<TransactionNote> transactionNotes;
    private String responseName;
    private String errorMessage;
    private String batchId;
    private String thumbPrint;
    private String transactionRefId;
    private String xRequestorId;
    private String organisationName;

    public AuditDto(RequestAudit requestAudit) {
        super();
        this.id = requestAudit.getId();
        this.userId = requestAudit.getUserId();
        this.userType = requestAudit.getUserType();
        this.clientId = requestAudit.getClientId();
        this.requestTimeReceived = requestAudit.getRequestTimeReceived();
        this.requestType = requestAudit.getRequestType() != null ? requestAudit.getRequestType().getValue() : null;
        this.requesterMessageIdentification = requestAudit.getRequesterMessageIdentification();
        this.requestAccountType = requestAudit.getRequestAccountType();
        this.bic = requestAudit.getAssigneeBic();
        this.iban = requestAudit.getIban();
        this.identificationType = requestAudit.getIdentificationType();
        this.identificationValue = requestAudit.getIdentificationValue();
        this.secondaryId = requestAudit.getSecondaryId();
        this.requestStatus = requestAudit.getRequestStatus() != null ? requestAudit.getRequestStatus().getValue() : ImportRequestStatus.FAILURE.name();
        this.responseTimeSent = requestAudit.getResponseTimeSent();
        this.responseMatched = requestAudit.getResponseMatched();
        this.responseReasonCode = (requestAudit.getResponseReasonCode() == null && requestAudit.getRequestStatus() != null && ImportRequestStatus.FAILURE.getValue().equals(requestAudit.getRequestStatus().getValue()))
                ? "INVALID"
                : requestAudit.getResponseReasonCode();
        this.requestedOrgName = requestAudit.getRequestedOrgName();
        this.requestedOrgId = requestAudit.getRequestedOrgId();
        this.responseUrl = requestAudit.getRequestedCopEpUrl();
        this.transactionNotes = requestAudit.getTransactionNotes();
        this.requestName = requestAudit.getName();
        this.schemeReasonCode = convertSchemeReasonCodeToMap(requestAudit.getSchemeReasonCode());
        this.responseName=requestAudit.getResponseName();
        this.errorMessage = requestAudit.getValidationErrorMessage();
        this.batchId = requestAudit.getBatchId() != null ? requestAudit.getBatchId() : null;
        this.thumbPrint = requestAudit.getThumbprint();
        this.transactionRefId = requestAudit.getTransactionRefId();
        this.xRequestorId = requestAudit.getXRequestorId();
        this.requestTimeSent = requestAudit.getRequestTimeSent();
        this.organisationName = requestAudit.getOrganisationName();
    }

    private Map<String, String> convertSchemeReasonCodeToMap(String schemeReasonCode) {
        Map<String, String> schemeReasonCodeMap = new HashMap<>();
        if (schemeReasonCode != null && !schemeReasonCode.isEmpty()) {
            String[] pairs = schemeReasonCode.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    schemeReasonCodeMap.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return schemeReasonCodeMap;
    }
}

