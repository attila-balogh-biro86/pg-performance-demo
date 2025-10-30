package com.banfico.cache;

import com.banfico.model.TransactionFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class FilterHash {
    private final ObjectMapper mapper = new ObjectMapper();

    public String keyFor(TransactionFilter f) {
        // Normalize: sort properties via a record/map, omit nulls, serialize consistently
        ObjectNode node = mapper.createObjectNode();
        if (f.iban() != null) node.put("iban", f.iban());
        if (f.currency() != null) node.put("currency", f.currency());
        if (f.assigneeBic() != null) node.put("assigneeBic", f.assigneeBic());
        if (f.amount() != null) node.put("amount", f.amount().toPlainString());
        if (f.clientId() != null) node.put("clientId", f.clientId());

        try {
            String json = mapper.writeValueAsString(node);
            String sha256 = DigestUtils.sha256Hex(json);
            return "tx:count:" + sha256;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
