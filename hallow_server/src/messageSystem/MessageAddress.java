package messageSystem;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageAddress {
	private static AtomicInteger addressGenerator = new AtomicInteger();
	private int address;
	
	public MessageAddress() {
		address = MessageAddress.addressGenerator.incrementAndGet();
	}
	
	public int getAddress() {
		return address;
	}
}
