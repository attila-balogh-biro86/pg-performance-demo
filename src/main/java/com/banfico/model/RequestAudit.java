package com.banfico.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cop_request_audit")
@Getter
@Setter
@DynamicUpdate
@ToString
public class RequestAudit {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cop_request_audit_seq")
    private String id;

    @Column(name = "request_time_received")
    private OffsetDateTime requestTimeReceived;

    @Column(name = "request_time_sent")
    private OffsetDateTime requestTimeSent;

    @Column(name = "name")
    private String name;

    @Column(name = "response_name")
    private String responseName;

    @Column(name = "organisation_id_type")
    private String organisationIdType;

    @Column(name = "organisation_id_value")
    private String organisationIdValue;

    @Column(name = "request_account_type")
    private String requestAccountType;

    @Column(name = "secondary_id")
    private String secondaryId;

    @Column(name = "requester_message_identification")
    private String RequesterMessageIdentification;

    @Column(name = "responder_message_identification")
    private String ResponderMessageIdentification;

    @Column(name = "request_type")
    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    @Column(name = "assignee_bic")
    private String assigneeBic;

    @Column(name = "assigner_bic")
    private String assignerBic;

    @Column(name = "iban")
    private String iban;

    @Column(name = "identification_type")
    private String identificationType;

    @Column(name = "identification_value")
    private String identificationValue;

    @Column(name = "response_time_sent")
    private OffsetDateTime responseTimeSent;

    @Column(name = "response_matched")
    private Boolean responseMatched;

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "response_reason_code")
    private String responseReasonCode;

    @Column(name = "requested_org_id")
    private String requestedOrgId;

    @Column(name = "requested_org_name")
    private String requestedOrgName;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "requested_cop_ep_url")
    private String requestedCopEpUrl;

    @Column(name = "request_status")
    @Enumerated(EnumType.STRING)
    private ImportRequestStatus requestStatus;

    @Column(name = "scheme_response", columnDefinition = "TEXT")
    private String schemeResponse;

    @Column(name = "scheme_response_code", columnDefinition = "TEXT")
    private String schemeReasonCode;

    @Column(name = "batch_id")
    private String batchId;

    @Column(name = "request_source")
    @Enumerated(EnumType.STRING)
    private com.banfico.model.RequestSource requestSource;

    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "transaction_ref_id")
    private String transactionRefId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_id",referencedColumnName = "id",updatable = false, insertable = false)
    private List<TransactionNote> transactionNotes = new ArrayList<>();

    @Column(name = "validation_error_message")
    private String validationErrorMessage;

    @Column(name = "priority")
    private int priority;

    @Column(name = "thumbprint")
    private String thumbprint;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "request_headers", columnDefinition = "jsonb", updatable = false)
    private JsonNode requestHeaders;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "response_headers", columnDefinition = "jsonb", updatable = false)
    private JsonNode responseHeaders;

    @Column(name = "x_requestor_id")
    private String xRequestorId;

    @Column(name = "organisation_name")
    private String OrganisationName;

    public void addTransactionNote(TransactionNote note) {
        transactionNotes.add(note);
    }
}