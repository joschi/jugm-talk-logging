package com.example.demo.view;

import io.dropwizard.views.View;

public class StatusView extends View {
    private final int code;

    public StatusView(int code) {
        super("status.mustache");
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
