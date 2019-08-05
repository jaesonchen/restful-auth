package com.asiainfo.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asiainfo.auth.model.User;

/**   
 * @Description: TODO
 * 
 * @author chenzq  
 * @date 2019年6月25日 下午3:23:14
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);
}
