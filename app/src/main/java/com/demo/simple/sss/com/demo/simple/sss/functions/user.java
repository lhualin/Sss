package com.demo.simple.sss.com.demo.simple.sss.functions;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;

/**
 * Created by Administrator on 2016/4/18.
 */
public class user {
    AVQuery<AVUser> query = AVUser.getQuery();

    public  AVUser getuser(AVUser user){
        try{
            user=query.get(user.getObjectId());
        }catch (AVException e){
        }
        return user;
    }
    public  void updateuser(AVUser user){

    }
}
