package com.llw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.llw.common.Result;
import com.llw.pojo.User;
import com.llw.service.UserService;
import com.llw.utils.SMSUtils;
import com.llw.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {
            //生成随机4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码为: {}", code);

            //调用阿里云SMS发送短信
//            SMSUtils.sendMessage("llwDemo", "SMS_277225276", phone, code);

            //将生成的验证码保存到Session
//            session.setAttribute(phone, code);

            //将将生成的验证码缓存到Redis中，并且设置有效期为5分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            return Result.success("手机验证码发送成功");
        }
        return Result.error("手机验证码发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result<User> sendMsg(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());

        //获取手机号
        String phone = map.get("phone").toString();

        //获取验证码
        String code = map.get("code").toString();

        //从Session中获取保存的验证码
//        String codeInSession = session.getAttribute(phone).toString();

        //从Redis中获取缓存的验证码
        String codeInSession = (String) redisTemplate.opsForValue().get(phone);

        //进行验证码比对(页面提交的验证码和Session中保存的验证码比对)
        if (codeInSession != null && codeInSession.equals(code)) {
            //如果比对正确则登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);

            //判断当前手机号对应的用户是否是新用户，如果是新用户则自动完成注册
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());

            //如果用户登录成功，删除redis中缓存的验证码
            redisTemplate.delete(phone);

            return Result.success(user);
        }
        return Result.error("登录失败");
    }
}
