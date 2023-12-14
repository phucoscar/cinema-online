package com.vukimphuc.dto.response.google;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleData {
    private List<Name> names;
    private List<EmailAddress> emailAddresses;

    @Data
    public class Name {
        private String displayName;
        private String givenName;
        private String displayNameLastFirst;
        private String unstructuredName;

    }

    @Data
    public class EmailAddress {
        private String value;
    }
}
