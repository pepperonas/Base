package com.pepperonas.andbasx.datatype;

import android.content.pm.ApplicationInfo;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class InstalledApp {

    private ApplicationInfo applicationInfo;

    private String applicationName;


    public InstalledApp(ApplicationInfo applicationInfo, String applicationName) {
        this.applicationInfo = applicationInfo;
        this.applicationName = applicationName;
    }


    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }


    public void setApplicationInfo(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
    }


    public String getApplicationName() {
        return applicationName;
    }


    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
