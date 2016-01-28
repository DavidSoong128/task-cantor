package com.horizon.cantor.zookeeper;

import com.horizon.cantor.util.FstSerializer;
import com.horizon.cantor.util.Serializer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author zuqiang on 2015-12-23 16:11:56
 */

public class ZookeeperClient {
	
	private final CuratorFramework client;
	private Serializer serializer = new FstSerializer();
	private LeaderLatch leaderLatch;

	public ZookeeperClient(String registryAddress) {
		try {
			client = CuratorFrameworkFactory.builder()
					.connectString(registryAddress)
					.retryPolicy(new ExponentialBackoffRetry(1000, 3))
					.connectionTimeoutMs(5000)
					.build();
			client.start();

		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public boolean exists(String path) {
		try {
			return client.checkExists().forPath(path) != null;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public String create(String path, boolean ephemeral, boolean sequential) {
		int i = path.lastIndexOf('/');
		if (i > 0) {
			create(path.substring(0, i), false, false);
		}
		if (ephemeral) {
			try {
				if (sequential) {
					return client.create()
							.withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
							.forPath(path);
				} else {
					return client.create().withMode(CreateMode.EPHEMERAL)
							.forPath(path);
				}
			} catch (KeeperException.NodeExistsException e) {
				return path;
			} catch (Exception e) {
				throw new IllegalStateException(e.getMessage(), e);
			}
		} else {
			try {
				if (sequential) {
					return client.create()
							.withMode(CreateMode.PERSISTENT_SEQUENTIAL)
							.forPath(path);
				} else {
					return client.create().withMode(CreateMode.PERSISTENT)
							.forPath(path);
				}
			} catch (KeeperException.NodeExistsException e) {
				return path;
			} catch (Exception e) {
				throw new IllegalStateException(e.getMessage(), e);
			}
		}
	}

	public String create(String path, Object data, boolean ephemeral,
			boolean sequential) {
		int i = path.lastIndexOf('/');
		if (i > 0) {
			create(path.substring(0, i), data, false, false);
		}
		if (ephemeral) {
			try {
				if (sequential) {
					byte[] zkDataBytes;
					if (data instanceof Serializable) {
						zkDataBytes = serializer.serialize(data);
					} else {
						zkDataBytes = (byte[]) data;
					}
					return client.create()
							.withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
							.forPath(path, zkDataBytes);
				} else {
					return client.create().withMode(CreateMode.EPHEMERAL)
							.forPath(path);
				}
			} catch (KeeperException.NodeExistsException e) {
				return path;
			} catch (Exception e) {
				throw new IllegalStateException(e.getMessage(), e);
			}
		} else {
			try {
				if (sequential) {
					byte[] zkDataBytes;
					if (data instanceof Serializable) {
						zkDataBytes = serializer.serialize(data);
					} else {
						zkDataBytes = (byte[]) data;
					}
					return client.create()
							.withMode(CreateMode.PERSISTENT_SEQUENTIAL)
							.forPath(path, zkDataBytes);
				} else {
					return client.create().withMode(CreateMode.PERSISTENT)
							.forPath(path);
				}
			} catch (KeeperException.NodeExistsException e) {
				return path;
			} catch (Exception e) {
				throw new IllegalStateException(e.getMessage(), e);
			}
		}
	}
	
	
	public boolean leaderSelector(LeaderLatchListener leaderLatchListener,String masterPath){
		leaderLatch = new LeaderLatch(client, "/"+masterPath);
		leaderLatch.addListener(leaderLatchListener);
		try {
			leaderLatch.start();
		} catch (Exception e) {
			throw new RuntimeException("LeaderLatch start failed !!", e);
		}
		return leaderLatch.hasLeadership();
	}
	
	public void close(){
		try {
			leaderLatch.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
