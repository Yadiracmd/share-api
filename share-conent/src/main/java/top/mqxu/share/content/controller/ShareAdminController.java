package top.mqxu.share.content.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.mqxu.share.common.resp.CommonResp;
import top.mqxu.share.content.domain.entity.Share;
import top.mqxu.share.content.service.PointsProducer;
import top.mqxu.share.content.service.ShareService;

import java.util.List;

@RestController
@RequestMapping("/share/admin")
@Slf4j
@AllArgsConstructor
public class ShareAdminController {

    private final ShareService shareService;
    private final PointsProducer pointsProducer;

    @GetMapping("/list")
    public CommonResp<List<Share>> getSharesNotYet() {
        CommonResp<List<Share>> resp = new CommonResp<>();
        resp.setData(shareService.queryShareNotYet());
        return resp;
    }

    // 审核⽂章接⼝
    @PutMapping("/approve/{id}")
    public String approveArticle(@PathVariable Long id) {
        Long userId = shareService.approveShare(id);
        //假设审核通过加50分
        pointsProducer.sendPointsMessage(userId, 50);
        return "⽂章审核通过，积分奖励已发放";
    }

}