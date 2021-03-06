package com.laungcisin.security.user;

import com.laungcisin.security.rbac.mybatis.entity.SysRole;
import com.laungcisin.security.rbac.mybatis.entity.SysUser;
import com.laungcisin.security.rbac.mybatis.mapper.SysRoleMapper;
import com.laungcisin.security.rbac.mybatis.mapper.SysUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laungcisin
 */
@Component
public class LaungcisinUserDetailsService implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    //密码加密器-用于加密,密码匹配
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * @param usernameOrMobile
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrMobile) throws UsernameNotFoundException {
        logger.info("登录用户名:" + usernameOrMobile);
        return buildUser(usernameOrMobile);
    }

    private UserDetails buildUser(String userIdOrUsernameOrMobile) {
        // 1.查找用户信息
        SysUser user = sysUserMapper.getUserByUserIdOrUsernameOrMobile(userIdOrUsernameOrMobile);

        if (user == null || user.getUserId() < 0) {
            throw new UsernameNotFoundException("用户不存在");
        }

        long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        //2.判断账户是否被删除
        boolean enabled = user.getIsDeleted() == null || user.getIsDeleted() == 0;

        //3.判断账户是否过期
        boolean accountNonExpired = user.getAccountExpiredTime() == null || now < user.getAccountExpiredTime().getTime();

        //3.判断密码是否过期
        boolean credentialsNonExpired = user.getPasswordExpiredTime() == null || now < user.getPasswordExpiredTime().getTime();

        //4.判断账户是否被锁定
        boolean accountNonLocked = user.getIsLocked() == null || user.getIsLocked() == 0;

        //获取所有请求的url
        List<SysRole> roleList = sysRoleMapper.getRoleListByUserId(user.getUserId());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roleList)) {
            for (SysRole role : roleList) {
                //封装用户信息和角色信息 到 SecurityContextHolder全局缓存中
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleCode()));
            }
        }

        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));//ROLE_USER-OAuth协议用到
        UserDetails userDetails = new User(user.getUsername(), user.getPassword(),
                enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
                grantedAuthorities);
        return userDetails;
    }

}
