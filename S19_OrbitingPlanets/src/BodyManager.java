import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
/**
 * Class to manage body physics and threading.
 * Contains a reference to the body this thread is assigned to.
 * @author Matthew Kemp
 *
 */
public class BodyManager {
	private BadBody body;
	private ScheduledExecutorService executor;
	/**
	 * Constructor that takes a body reference and creates a 
	 * Scheduled Executor Service
	 * @param body
	 */
	public BodyManager(BadBody body){
		this.body = body;
		executor = Executors.newScheduledThreadPool(1);
	}
	/**
	 * Getter method for our body.
	 * @return
	 */
	public BadBody getBody() {
		return body;
	}
	/**
	 * method to start the calculating the physics
	 * on the body 60 times per second.
	 */
	public void start() {
		executor.scheduleAtFixedRate(new Runnable() {
		    public void run() {
		        body.increment(10000);
		    }
		}, 0, 16, TimeUnit.MILLISECONDS);
	}
	/**
	 * method to shut down the physics on the body.
	 */
	public void stop() {
		executor.shutdown();
	}
}
