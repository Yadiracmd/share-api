package top.mqxu.share.content.service;


import lombok.AllArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;
import top.mqxu.share.content.domain.entity.PointsLog;
import top.mqxu.share.content.mapper.PointsLogMapper;


@Service
@RocketMQMessageListener(topic = "PointsTopicZyd", selectorExpression = "ArticleApproved", consumerGroup = "points_group")
@AllArgsConstructor
public class PointsConsumer implements RocketMQListener<String> {
    private final ShareService service;
    private final PointsLogMapper pointsLogMapper;

    @Override
    public void onMessage(String message) {
        String[] parts = message.split(":");
        Long userId = Long.valueOf(parts[0]);
        int points = Integer.parseInt(parts[1]);
        // 为⽤户增加积分
        service.addBouns(userId, points);
        //记录积分⽇志
        PointsLog log = new PointsLog();
        log.setUserId(userId);
        log.setPoints(points);
        pointsLogMapper.insert(log);
    }
}