/*
 * Copyright (c) 2016 Martin Pfeffer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
