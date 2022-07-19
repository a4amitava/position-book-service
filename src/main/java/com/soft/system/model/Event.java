package com.soft.system.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
@ToString
public class Event {
    private final Integer id;
    private final Action action;
    private final String account;
    private final String security;
    private final Integer quantity;
}
