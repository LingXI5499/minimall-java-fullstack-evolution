package com.lingxi.minimall.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class HelloControllerTest {
    private final HelloController controller = new HelloController();

    @Test void requestBodyIsEchoed() {
        assertThat(controller.echo(new HelloController.EchoRequest("hi")).message()).isEqualTo("hi");
    }
}
