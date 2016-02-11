package com.revinate.sendgrid.model;

public interface SendGridModelVisitor {

    void visit(Email email);
    void visit(SendGridModel model);
}
