package messageSystem;

public abstract class Message {
	private final MessageAddress addrFrom;
	private final MessageAddress addrTo;
	
	public Message(MessageAddress addrFrom, MessageAddress addrTo) {
		this.addrFrom = addrFrom;
		this.addrTo = addrTo;
	}
	
	public MessageAddress getFrom() {
		return addrFrom;
	}
	
	public MessageAddress getTo() {
		return addrTo;
	}
	
	public abstract void Exec(Abonent abonent);
}
