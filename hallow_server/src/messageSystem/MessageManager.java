package messageSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageManager {
	Map<Abonent, Queue<Message>> messageQueue;
	
	public MessageManager() {
		messageQueue = new HashMap<Abonent, Queue<Message>>();
	}
	
	public void registerAbonent(Abonent ab) {
		messageQueue.put(ab, new ConcurrentLinkedQueue<Message>());
	}
	
	public void registerMessage(Message msg) {
		Queue<Message> abonentMessages = messageQueue.get(msg.getTo());
		if (abonentMessages == null) {
			return;
		}
		
		abonentMessages.offer(msg);
	}
	
	public void processMessages(Abonent ab) {
		Queue<Message> abonentMessages = messageQueue.get(ab);
		if (abonentMessages == null | abonentMessages.isEmpty()) {
			return;
		}

		Message msg = null;
		while ((msg = abonentMessages.poll()) != null) {
			msg.Exec(ab);
		}
	}
}
