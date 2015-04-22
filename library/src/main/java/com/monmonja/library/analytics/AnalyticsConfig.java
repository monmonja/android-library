package com.monmonja.library.analytics;

/**
 * Created by almondjoseph on 23/10/14.
 */
public class AnalyticsConfig {
    private final String mAccountId;

    public AnalyticsConfig(String accountId) {
        mAccountId = accountId;
    }

    public String getAccountId() {
        return mAccountId;
    }
}
