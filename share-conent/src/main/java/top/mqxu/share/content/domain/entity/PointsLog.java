package top.mqxu.share.content.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("points_log")
public class PointsLog {
    @TableId
    private Long id;
    // ⽤户ID
    private Long userId;
    // 获得的积分
    private Integer points;
    // 时间戳
    private LocalDateTime timestamp;
}
