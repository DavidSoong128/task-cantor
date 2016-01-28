package com.horizon.cantor.zookeeper;

import com.horizon.cantor.node.core.NodeManager;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskLatchListener implements LeaderLatchListener{

	private static final Logger	log	= LoggerFactory.getLogger(TaskLatchListener.class);
	
	private NodeManager nodeManager;
	
	public TaskLatchListener(NodeManager nodeManager){
		this.nodeManager = nodeManager;
	}
	
	@Override
	public void notLeader() {
		log.info("I become the salve , Stopping node... !!!!");
		nodeManager.stopNode();
	}
	@Override
	public void isLeader() {
		log.info("I become the leader ,Starting node... !!!!");
		nodeManager.startNode();
	}

}
