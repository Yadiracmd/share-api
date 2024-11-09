package top.mqxu.share.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.mqxu.share.user.domain.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
