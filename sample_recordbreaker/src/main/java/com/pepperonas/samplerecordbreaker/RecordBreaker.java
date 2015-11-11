package com.pepperonas.samplerecordbreaker;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class RecordBreaker {

    enum Status {
        INVALID_DATA(-203), PARSING_FAILED(-202), NO_DATA_FOUND(-201), EXPIRED(-200),
        SENDER_FAILED(-100),

        SENDER_SUCCESS(100),
        RESOLVE_SUCCESS(200);


        Status(int i) {

        }
    }
}
