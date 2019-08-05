package com.asiainfo.auth.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asiainfo.auth.annotation.Authorization;
import com.asiainfo.auth.annotation.Principal;
import com.asiainfo.auth.model.Response;
import com.asiainfo.auth.model.User;
import com.asiainfo.auth.repository.UserRepository;
import com.asiainfo.auth.service.TokenService;
import com.asiainfo.auth.util.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**   
 * @Description: token登陆、验证控制器，与swagger2结合提供apidoc
 * 
 * @author chenzq  
 * @date 2019年6月25日 下午4:08:29
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
@Api(value = "/tokens", tags = "token接口模块")
@RestController
@RequestMapping("/tokens")
public class TokenController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "登录", notes = "username/password登陆")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "string", paramType = "query"), 
        @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string", paramType = "query")})
    public ResponseEntity<Response> login(@RequestParam String username, @RequestParam String password) {
        
        Assert.notNull(username, "username can not be empty");
        Assert.notNull(password, "password can not be empty");
        // 从数据库中查询登陆用户
        User user = userRepository.findByUsername(username);
        // 用户不存在或者密码错误
        if (user == null || !user.getPassword().equals(password)) {
            return new ResponseEntity<>(Response.loginError(), HttpStatus.NOT_FOUND);
        }
        // 生成token
        String token = tokenService.create(user.getUsername());
        // 返回token给客户端
        return new ResponseEntity<>(Response.loginSuccess(token), HttpStatus.OK);
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ApiOperation(value = "首页", notes = "不需要token校验")
    public ResponseEntity<Response> index() {
        return new ResponseEntity<>(Response.index(), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    @Authorization
    @ApiOperation(value = "查询", notes = "查询当前用户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "token字符串", required = true, dataType = "string", paramType = "header"), 
        @ApiImplicitParam(name = "id", value = "userId", required = true, dataType = "string", paramType = "path")})
    public ResponseEntity<Response> getUserById(@Principal User user, @PathVariable long id) {
        
        Optional<User> op = userRepository.findById(id);
        return new ResponseEntity<>(Response.success(op.isPresent() ? op.get() : user), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    @Authorization
    @ApiOperation(value = "刷新", notes = "刷新令牌")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "token字符串", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Response> refresh(HttpServletRequest request) {
        
        String authorization = request.getHeader(Constants.TOKEN_HEADER);
        String newToken = tokenService.refresh(authorization);
        return new ResponseEntity<>(Response.success(newToken), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @Authorization
    @ApiOperation(value = "退出", notes = "登出并清除token")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "token字符串", required = true, dataType = "string", paramType = "header")})
    public ResponseEntity<Response> logout(HttpServletRequest request) {
        
        String authorization = request.getHeader(Constants.TOKEN_HEADER);
        tokenService.discard(authorization);
        return new ResponseEntity<>(Response.logoutSuccess(), HttpStatus.OK);
    }
}
