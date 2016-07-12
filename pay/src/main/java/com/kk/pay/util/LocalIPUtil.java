package com.kk.pay.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class LocalIPUtil {
    public LocalIPUtil() {
    }

    public static String getLocalAddr() {
        Enumeration interfaces = null;

        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException var4) {
            var4.printStackTrace();
            return null;
        }

        while (interfaces.hasMoreElements()) {
            NetworkInterface ifc = (NetworkInterface) interfaces.nextElement();
            Enumeration addressesOfAnInterface = ifc.getInetAddresses();

            while (addressesOfAnInterface.hasMoreElements()) {
                InetAddress address = (InetAddress) addressesOfAnInterface.nextElement();
                if (address.isSiteLocalAddress()) {
                    return address.getHostAddress();
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println(getLocalAddr());
    }
}