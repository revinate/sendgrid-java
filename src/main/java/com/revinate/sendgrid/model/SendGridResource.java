package com.revinate.sendgrid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;

import static com.revinate.sendgrid.operations.AbstractOperations.OBJECT_MAPPER;

public abstract class SendGridResource {

    public static final ObjectMapper PRETTY_PRINT_OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    @JsonIgnore
    public abstract String getPathId();

    public String toJson() {
        try {
            return OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public HttpEntity toHttpEntity() {
        return EntityBuilder.create().setText(toJson()).build();
    }

    @Override
    public String toString() {
        try {
            return String.format("<%s> JSON: %s", super.toString(),
                    PRETTY_PRINT_OBJECT_MAPPER.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }
}
