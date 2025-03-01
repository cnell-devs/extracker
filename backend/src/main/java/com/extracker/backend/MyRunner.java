package com.extracker.backend;

import com.extracker.backend.item.Item;
import com.extracker.backend.link.CreateLinkToken;
import com.extracker.backend.transactions.FetchTransactions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements CommandLineRunner {

    // private Item item;

    private static final Logger log = LoggerFactory.getLogger(MyRunner.class);
    // FetchTransactions fetchTransactions;
    // CreateLinkToken createLinkToken;

    public MyRunner(/* FetchTransactions fetchTransactions, CreateLinkToken createLinkToken, Item item */) {
        // this.fetchTransactions = fetchTransactions;
        // this.createLinkToken = createLinkToken;
        // this.item = item;
    }

    @Override
    public void run(String... args) throws Exception {
    }

}