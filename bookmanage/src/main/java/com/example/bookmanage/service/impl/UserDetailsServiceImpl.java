package com.example.bookmanage.service.impl;

import com.example.bookmanage.entity.user;
import com.example.bookmanage.entity.impl.LoginUser;
import com.example.bookmanage.service.SysPermissionService;
import com.example.bookmanage.utils.StringUtils;
import exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.bookmanage.service.userService;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户验证处理
 *
 * @author jawa
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private userService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user user = userService.findUserByName(username);
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new ServiceException("登录用户：" + username + " 不存在");
        }
        return new LoginUser((long) user.getId(),user, permissionService.getMenuPermission(user));
    }
}