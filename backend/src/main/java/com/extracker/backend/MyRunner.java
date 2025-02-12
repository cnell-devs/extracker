package com.extracker.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MyRunner.class);
    FetchTransactions fetchTransactions;

    public MyRunner(FetchTransactions fetchTransactions) {
        this.fetchTransactions = fetchTransactions;
    }

    @Override
    public void run(String... args) throws Exception {

        fetchTransactions.getTransactions();
    }

}