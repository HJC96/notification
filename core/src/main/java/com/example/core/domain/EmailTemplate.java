package com.example.core.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "email_template")
@Getter
@Setter
@NoArgsConstructor
public class EmailTemplate {

    @Id
    private String templateKey;  // ex) email-template:default

    private String templateContent;

    public EmailTemplate(String templateKey, String templateContent) {
        this.templateKey = templateKey;
        this.templateContent = templateContent;
    }
}