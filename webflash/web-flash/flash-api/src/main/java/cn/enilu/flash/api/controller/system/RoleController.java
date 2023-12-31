package cn.enilu.flash.api.controller.system;

import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.constant.Const;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.dictmap.RoleDict;
import cn.enilu.flash.bean.entity.system.Role;
import cn.enilu.flash.bean.entity.system.User;
import cn.enilu.flash.bean.enumeration.BizExceptionEnum;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.exception.ApplicationException;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.node.Node;
import cn.enilu.flash.bean.vo.node.ZTreeNode;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.system.LogObjectHolder;
import cn.enilu.flash.service.system.RoleService;
import cn.enilu.flash.service.system.UserService;
import cn.enilu.flash.service.system.impl.ConstantFactory;
import cn.enilu.flash.utils.*;
import cn.enilu.flash.warpper.RoleWarpper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.ROLE})
    public Object list(String name){
        List roles = null;
        if(Strings.isNullOrEmpty(name)) {
            roles =  roleService.queryAll();
        }else{
            String decode_name = URLDecoder.decode(name);
            roles = roleService.queryAll(SearchFilter.build("name", SearchFilter.Operator.LIKE, decode_name));
//            roles = roleService.findByName(name);
        }
        return Rets.success(new RoleWarpper(BeanUtil.objectsToMaps(roles)).warp());
    }

//    @RequestMapping(method = RequestMethod.POST)
//    @BussinessLog(value = "编辑角色", key = "name", dict = RoleDict.class)
//    @RequiresPermissions(value = {Permission.ROLE_EDIT})
//    public Object save(@Valid Role role){
//        if(role.getId()==null) {
//            roleService.insert(role);
//        }else{
//            roleService.update(role);
//        }
//        return Rets.success();
//    }

    @RequestMapping(value="/addRole",method = RequestMethod.POST)
    @BussinessLog(value = "添加角色", key = "name", dict = RoleDict.class)
    @RequiresPermissions(value = {Permission.ROLE_ADD})
    public Object add(@Valid Role role){
        roleService.insert(role);
        return Rets.success();
    }

    @RequestMapping(value = "/editRole",method = RequestMethod.POST)
    @BussinessLog(value = "编辑角色", key = "name", dict = RoleDict.class)
    @RequiresPermissions(value = {Permission.ROLE_EDIT})
    public Object edit(@Valid Role role){
        roleService.update(role);
        return Rets.success();
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @BussinessLog(value = "删除角色", key = "roleId", dict = RoleDict.class)
    @RequiresPermissions(value = {Permission.ROLE_DEL})
    public Object remove(@RequestParam Long roleId){
        logger.info("id:{}",roleId);
        if (roleId==null) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        if(roleId.intValue()<2){
            return Rets.failure("不能删除初始角色");
        }
        List<User> userList = userService.queryAll(SearchFilter.build("roleid", SearchFilter.Operator.LIKE,String.valueOf(roleId)));
        System.out.println(userList);
        String[] roleids;
        for(int i=0;i<userList.size();i++){
            roleids = userList.get(i).getRoleid().split(",");
            System.out.println(roleids[0]);
            for(int j=0;j<roleids.length;j++){
                if(roleids[j].equals(String.valueOf(roleId))){
                    return Rets.failure("有用户使用该角色，禁止删除");
                }
            }
        }
//        if(!userList.isEmpty()){
//            return Rets.failure("有用户使用该角色，禁止删除");
//        }
        //不能删除超级管理员角色
        if(roleId.intValue() ==Const.ADMIN_ROLE_ID){
            return Rets.failure("禁止删除超级管理员角色");
        }
        //缓存被删除的角色名称
        LogObjectHolder.me().set(ConstantFactory.me().getSingleRoleName(roleId));
        roleService.delRoleById(roleId);
        return Rets.success();
    }

    @RequestMapping(value = "/savePermisson",method = RequestMethod.POST)
    @BussinessLog(value = "配置角色权限", key = "roleId", dict = RoleDict.class)
    @RequiresPermissions(value = {Permission.ROLE_EDIT})
    public Object setAuthority(Long roleId, String
            permissions) {
        if (BeanUtil.isOneEmpty(roleId)) {
            throw new ApplicationException(BizExceptionEnum.REQUEST_NULL);
        }
        roleService.setAuthority(roleId, permissions);
        return Rets.success();
    }


    /**
     * 获取角色树
     */
    @RequestMapping(value = "/roleTreeListByIdUser", method = RequestMethod.GET)
    @RequiresPermissions(value = {Permission.ROLE})
    public Object roleTreeListByIdUser(Long idUser) {
        User user = userService.get(idUser);
        String roleIds = user.getRoleid();
        List<ZTreeNode> roleTreeList = null;
        if (StringUtil.isEmpty(roleIds)) {
            roleTreeList = roleService.roleTreeList();
        } else {
            Long[] roleArray = Convert.toLongArray(",", roleIds);
            roleTreeList = roleService.roleTreeListByRoleId(roleArray);

        }
        List<Node> list = roleService.generateRoleTree(roleTreeList);
        List<Long> checkedIds = Lists.newArrayList();
        for (ZTreeNode zTreeNode : roleTreeList) {
            if (zTreeNode.getChecked() != null && zTreeNode.getChecked()) {
                checkedIds.add(zTreeNode.getId());
            }
        }
        return Rets.success(Maps.newHashMap("treeData", list, "checkedIds", checkedIds));
    }
}
