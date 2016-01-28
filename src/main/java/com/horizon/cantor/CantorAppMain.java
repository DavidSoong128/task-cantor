package com.horizon.cantor;

import com.horizon.cantor.config.CantorConfig;
import com.horizon.cantor.node.core.NodeManager;
import com.horizon.cantor.node.core.TaskNodeManager;
import com.horizon.cantor.zookeeper.TaskLatchListener;
import com.horizon.cantor.zookeeper.ZookeeperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 11:41
 * @see
 * @since : 1.0.0
 */
public class CantorAppMain {

    private static final Logger log	= LoggerFactory.getLogger(CantorAppMain.class);

    private static CantorConfig config = CantorConfig.configHolder();

    public static void main(String[] args) {
        try {
            //1: 通过Node管理器启动
            final NodeManager nodeManager = new TaskNodeManager();

            //2: 启动zk leader选取，如果为leader则处理数据，否则不处理
            final ZookeeperClient client = new ZookeeperClient(config.getZkRegisterAddress());
            client.leaderSelector(new TaskLatchListener(nodeManager), config.getZkMasterPath());

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    client.close();
                    nodeManager.stopNode();
                    log.info("System exit !!!");
                }
            }));
        }catch (Exception e) {
            log.error("start failed !!", e);
        }
    }
}
