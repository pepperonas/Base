package com.pepperonas.samplerecordbreaker;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public interface OnRecordResolvedListener {

    public void onRecordBreakerSuccess(RecordBreaker.Status success, String... params);

    public void onRecordBreakerFailed(RecordBreaker.Status error);

}
