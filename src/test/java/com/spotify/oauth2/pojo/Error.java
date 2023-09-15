package com.spotify.oauth2.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;


//@Getter
//@Setter
@Data
@Jacksonized
@Builder
public class Error {

        @JsonProperty("error")
        private InnerError error;
}
