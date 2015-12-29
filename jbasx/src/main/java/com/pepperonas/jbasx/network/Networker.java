package com.pepperonas.jbasx.network;

import com.pepperonas.jbasx.Jbasx;
import com.pepperonas.jbasx.log.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class Networker implements Callable<List<String>> {

    private static final String TAG = "Networker";


    public enum Mode {
        IP_LOOKUP, HOST_ADDRESSES
    }


    private Mode mode;
    private String networkAddress;
    private int timeout;


    /**
     * Instantiates a new Networker.
     *
     * @param mode           the mode
     * @param networkAddress the network address
     * @param timeout        the timeout
     */
    public Networker(Mode mode, String networkAddress, int timeout) {
        this.mode = mode;
        this.networkAddress = networkAddress;
        this.timeout = timeout;
    }


    public List<String> call() {
        ArrayList hosts = new ArrayList();

        if (networkAddress.contains("127.0.0.")) {
            if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                Log.w(TAG, "call - Localhost, returning empty list.");
            }
            return hosts;
        }

        if (mode == Mode.IP_LOOKUP) {
            try {
                InetAddress ia = InetAddress.getLocalHost();
                for (int i = 1; i < 255; i++) {
                    String host = networkAddress + i;
                    InetAddress ip = InetAddress.getByName(host);

                    if (ip.isReachable(timeout)) {
                        hosts.add(host);
                        if (Jbasx.mLog == Jbasx.LogMode.ALL) {
                            Log.i(TAG, "call - Reachable IP: " + host);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return hosts;
        } else {
            return hosts;
        }
    }

}
