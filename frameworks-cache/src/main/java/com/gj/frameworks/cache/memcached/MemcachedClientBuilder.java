package com.gj.frameworks.cache.memcached;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import net.rubyeye.xmemcached.XMemcachedClientBuilder;

public class MemcachedClientBuilder extends XMemcachedClientBuilder {
	
	public MemcachedClientBuilder(String addressList) {
		super(getAddresses(addressList));
	}

	/**
	 * Split a string in the form of "host:port;host2:port" into a List of
	 * InetSocketAddress instances suitable for instantiating a MemcachedClient.
	 * Note that colon-delimited IPv6 is also supported. For example: ::1:11211
	 */
	public static List<InetSocketAddress> getAddresses(String s) {
		if (s == null) {
			throw new NullPointerException("Null host list");
		}
		if (s.trim().equals("")) {
			throw new IllegalArgumentException("No hosts in list:  ``" + s + "''");
		}
		s = s.trim();
		ArrayList<InetSocketAddress> addrs = new ArrayList<InetSocketAddress>();

		for (String hoststuff : s.split(";")) {
			int finalColon = hoststuff.lastIndexOf(':');
			if (finalColon < 1) {
				throw new IllegalArgumentException("Invalid server ``" + hoststuff + "'' in list:  " + s);

			}
			String hostPart = hoststuff.substring(0, finalColon).trim();
			String portNum = hoststuff.substring(finalColon + 1).trim();

			addrs.add(new InetSocketAddress(hostPart, Integer.parseInt(portNum)));
		}
		assert !addrs.isEmpty() : "No addrs found";
		return addrs;
	}
}
