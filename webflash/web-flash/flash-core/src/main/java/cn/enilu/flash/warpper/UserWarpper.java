package cn.enilu.flash.warpper;

import cn.enilu.flash.service.system.impl.ConstantFactory;
import cn.enilu.flash.utils.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * 用户管理的包装类
 *
 * @author fengshuonan
 * @date 2017年2月13日 下午10:47:03
 */
public class UserWarpper extends BaseControllerWarpper {

    public UserWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        if(StringUtil.isNotNullOrEmpty(map.get("projectLeader"))){
            map.put("leaderName", ConstantFactory.me().getUserNameById((Long) map.get("projectLeader")));
        }else if(StringUtil.isNotNullOrEmpty(map.get("tester"))) {
            map.put("testerName", ConstantFactory.me().getUserNameById((Long) map.get("tester")));
        }else {
            map.put("sexName", ConstantFactory.me().getSexName((Integer) map.get("sex")));
            if (StringUtil.isNotNullOrEmpty(map.get("roleid"))) {
                map.put("roleName", ConstantFactory.me().getRoleName((String) map.get("roleid")));
            }
            if (StringUtil.isNotNullOrEmpty(map.get("deptid"))) {
                map.put("deptName", ConstantFactory.me().getDeptName((Long) map.get("deptid")));
            }
            map.put("statusName", ConstantFactory.me().getStatusName((Integer) map.get("status")));
        }

    }

}
