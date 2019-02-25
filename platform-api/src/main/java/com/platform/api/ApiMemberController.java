package com.platform.api;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.platform.thirdrechard.utils.IpUtil;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.entity.MemberEntity;
import com.platform.service.ApiMemberService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;

import javax.servlet.http.HttpServletRequest;

/**
 * 会员表Controller
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-19 11:37:51
 */
@RestController
@RequestMapping("api/member")
public class ApiMemberController extends ApiBaseAction {
    @Autowired
    private ApiMemberService memberService;

    /**
     * 查看列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("member:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);

        List<MemberEntity> memberList = memberService.queryList(query);
        int total = memberService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(memberList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 查看信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("member:info")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.queryObject(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("member:save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("member:update")
    public R update(@RequestBody MemberEntity member) {
        memberService.update(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("member:delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.deleteBatch(ids);

        return R.ok();
    }

    /**
     * 查看所有列表
     */
    @RequestMapping("/queryAll")
    public R queryAll(@RequestParam Map<String, Object> params) {

        List<MemberEntity> list = memberService.queryList(params);

        return R.ok().put("list", list);
    }

    /**
     * 会员开通或者续费
     * @param money
     * @param member
     * @return
     */
    @ApiOperation(value = "会员开通或者续费")
    @RequestMapping("/openOrRenewVplus")
    public Object openOrRenewVplus(HttpServletRequest request, Integer money, Integer userId) {


        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = memberService.openOrRenewVplus(new BigDecimal(money), userId, IpUtil.getIpAddr(request), null);
        } catch (Exception e) {
           return toResponsFail(e.getMessage());

        }
           return toResponsSuccess(map);
    }
}
