package com.next.api.controller.basic;

import com.next.constant.CommonConstant;
import com.next.pojo.Users;
import com.next.pojo.vo.UserVO;
import com.next.redis.RedisOperator;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class BasicController {

    @Autowired
    public RedisOperator redis;

    /**
     * 组装一个长度为5的数组，数组内容为count以内随机数，不重复
     * @param count
     * @return
     */
    protected int[]  guessULikeIndex(int count){
        int[] guessULikeArray = new int[5];
        for (int i = 0; i < guessULikeArray.length; i++){
            int numIndex = (int)(Math.random() * count);
            if(ArrayUtils.contains(guessULikeArray,numIndex)){
                i--;
                continue;
            }
            guessULikeArray[i] = numIndex;
        }
        return guessULikeArray;
    }

    protected UserVO creatSession(Users users){
        if(users == null){
            return null;
        }
        String userId = users.getId();
        String token = UUID.randomUUID().toString().trim();
        redis.set(CommonConstant.REDIS_USER_TOKEN+userId,token);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(users,userVO);
        userVO.setUserUniqueToken(token);
        return userVO;
    }

    protected UserVO setUserVOToken(Users users){
        if(users == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(users,userVO);

        String userId = users.getId();
        String token = redis.get(CommonConstant.REDIS_USER_TOKEN + userId);

        userVO.setUserUniqueToken(token);

        return userVO;
    }
}
