package top.mqxu.share.content.service;

import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

@Service
public class PointsProducer {
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    public void sendPointsMessage(Long userId, int points) {
        rocketMQTemplate.convertAndSend("PointsTopicZyd:ArticleApproved", userId + ":" + points);
    }
}
