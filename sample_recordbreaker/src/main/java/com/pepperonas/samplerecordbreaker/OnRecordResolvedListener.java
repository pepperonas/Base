package com.pepperonas.samplerecordbreaker;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public interface OnRecordResolvedListener {

    void onRecordBreakerSuccess(RecordBreaker.Status success, String... params);

    void onRecordBreakerFailed(RecordBreaker.Status error);

}
