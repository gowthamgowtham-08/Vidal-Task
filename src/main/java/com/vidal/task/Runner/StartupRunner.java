package com.vidal.task.Runner;

import com.vidal.task.Service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private WebhookService webhookService;

    @Override
    public void run(String... args) {
        webhookService.executeTask();
    }
}
