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

package com.pepperonas.jbasx.network;

import com.pepperonas.jbasx.Jbasx;
import com.pepperonas.jbasx.log.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class NetworkBaseUtils {

    private static final String TAG = "NetworkBaseUtils";


    /**
     * Collect network addresses.
     * Requires android.Manifest.permission#INTERNET
     *
     * @param onlyIps Whenever only IP-addresses should be shown.
     * @return {@link List<String>} containing network addresses.
     */
    public static List<String> getNetworkAddresses(boolean onlyIps) {
        List<String> ipList = new ArrayList<>();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface nwInterface = en.nextElement();
                for (Enumeration<InetAddress> ipAddress = nwInterface.getInetAddresses(); ipAddress.hasMoreElements(); ) {
                    String ip;
                    if (onlyIps) {
                        ip = ipAddress.nextElement().toString().replace("/", "");
                        if (ip.contains(".")) {
                            ipList.add(ip);
                        }
                    } else {
                        ip = ipAddress.nextElement().toString();
                        ipList.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.e(TAG, "getNetworkAddresses - Retrieving network interfaces failed.");
                e.printStackTrace();
            }
        }
        return ipList;
    }

}
