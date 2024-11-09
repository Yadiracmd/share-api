package top.mqxu.share.user.service;


import lombok.extern.slf4j.Slf4j;
import top.mqxu.share.common.util.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.mqxu.share.common.exception.BusinessException;
import top.mqxu.share.common.exception.BusinessExceptionEnum;
import top.mqxu.share.common.util.SnowUtil;
import top.mqxu.share.user.domain.dto.LoginDTO;
import top.mqxu.share.user.domain.dto.UserAddBonusMsgDTO;
import top.mqxu.share.user.domain.entity.BonusEventLog;
import top.mqxu.share.user.domain.entity.User;
import top.mqxu.share.user.domain.entity.UserLoginResp;
import top.mqxu.share.user.mapper.BonusEventLogMapper;
import top.mqxu.share.user.mapper.UserMapper;

import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private BonusEventLogMapper bonusEventLogMapper;


    public Long count() {
        return userMapper.selectCount(null);
    }

    public UserLoginResp login(LoginDTO loginDTO) {
        User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getPhone, loginDTO.getPhone()));
        //没找到用户，抛出运行时异常
        if (user == null) {
            throw new BusinessException(BusinessExceptionEnum.PHONE_NOT_EXIST);
        }
        //密码错误，，抛出运行时异常
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            throw new BusinessException(BusinessExceptionEnum.PASSWORD_ERROR);
        }
        //都正确，返回
        UserLoginResp userLoginResp = UserLoginResp.builder().user(user).build();
        String token = JwtUtil.createToken(userLoginResp.getUser().getId(),userLoginResp.getUser().getPhone());
        userLoginResp.setToken(token);
        return userLoginResp;
    }

    public Long register(LoginDTO loginDTO) {
        User userDb = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getPhone, loginDTO.getPhone()));
        if (userDb != null) {
            throw new BusinessException(BusinessExceptionEnum.PHONE_EXIST);
        }
        User saveUser = User.builder().id(SnowUtil.getSnowflakeNextId()).phone(loginDTO.getPhone()).password(loginDTO.getPassword()).nickname("新用户").avatarUrl("https://niit-soft.oss-cn-hangzhou.aliyuncs.com/avatar/8.jpg").bonus(100).createTime(new Date()).updateTime(new Date()).build();
        userMapper.insert(saveUser);
        return saveUser.getId();
    }

    public User findById(Long id) {
        return userMapper.selectById(id);
    }

    public void updateBonus(UserAddBonusMsgDTO userAddBonusMsgDTO) {
        // 1. 为用户修改积分
        Long userId = userAddBonusMsgDTO.getUserId();
        Integer bonus = userAddBonusMsgDTO.getBonus();

        User user = userMapper.selectById(userId);
        user.setBonus(user.getBonus() + bonus);
        userMapper.update(user, new QueryWrapper<User>().lambda().eq(User::getId, userId));

        // 2. 记录日志到 bonus_event_log 表
        bonusEventLogMapper.insert(BonusEventLog.builder()
                .userId(userId)
                .value(bonus)
                .description(userAddBonusMsgDTO.getDescription())
                .event(userAddBonusMsgDTO.getEvent())
                .createTime(new Date())
                .build());

        log.info("积分添加完毕...");
    }


}
