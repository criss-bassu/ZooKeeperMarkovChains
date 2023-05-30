package prZooKeeper;

import java.io.Closeable;
import java.io.IOException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ElectionNode  extends LeaderSelectorListenerAdapter implements Closeable {
	
	LeaderSelector leaderSelector;
	
	private static final String PATH_ZOOKEEPER = "/zookeeper";
	public static final String ZOOKEEPER_HOST = System.getenv().getOrDefault("ZOOKEEPER_HOST", "localhost:2181");
	
	public ElectionNode() {
		CuratorFramework client = CuratorFrameworkFactory.newClient(ZOOKEEPER_HOST, new ExponentialBackoffRetry(1000, 3));
		client.start();

		leaderSelector = new LeaderSelector(client, PATH_ZOOKEEPER, this);
		leaderSelector.autoRequeue();		
	}

	public void start() throws IOException {
		leaderSelector.start();
	}

	public void close() throws IOException {
		leaderSelector.close();
	}

	public boolean getImLeading() throws Exception {
		return leaderSelector.hasLeadership();
	}

	@Override
	public void takeLeadership(CuratorFramework client) throws Exception {
		while(true) {
			System.out.println("Soy lider");
			Operacion.lider();
			Thread.sleep(5000);
		}
	}
	
}
