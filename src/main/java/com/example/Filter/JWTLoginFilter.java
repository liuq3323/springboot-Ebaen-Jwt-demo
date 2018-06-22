package com.example.Filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.service.MyUserRepositoryService;
import com.example.util.Result;
import com.example.vo.MyUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * 登录filter
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter{

    private AuthenticationManager authenticationManager;

    private MyUserRepositoryService myUserRepositoryService;

    public JWTLoginFilter(AuthenticationManager authenticationManager , MyUserRepositoryService myUserRepositoryService) {
        this.authenticationManager = authenticationManager;
        this.myUserRepositoryService = myUserRepositoryService;
    }

    /**
     * 接收并解析用户凭证
     * @param req
     * @param res
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
//            MyUser user = new ObjectMapper()
//                    .readValue(req.getInputStream(), MyUser.class);
            BufferedReader streamReader = new BufferedReader( new InputStreamReader(req.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            JSONObject jsonObject = JSONObject.parseObject(responseStrBuilder.toString());

            MyUser user = new MyUser();
            user.setUsername(jsonObject.getString("username"));
            user.setPassword(jsonObject.getString("password"));
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
     * @param req
     * @param res
     * @param chain
     * @param auth
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        List<MyUser> user = myUserRepositoryService.findByUsername(((User) auth.getPrincipal()).getUsername());

        MyUser loginUser = user.get(0);

        Map userInfo = new HashMap();
        userInfo.put("username" ,loginUser.getUsername());
        userInfo.put("testInfo" ,"testInfo");
        userInfo.put("id" ,loginUser.id);
        String token = Jwts.builder()
                .setSubject(((User) auth.getPrincipal()).getUsername())
                .setClaims(userInfo)
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
                .signWith(SignatureAlgorithm.HS512, "MyJwtSecret")
                .compact();

        res.addHeader("Authorization", token);
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpServletResponse.SC_OK);
        //System.out.println(JSON.toJSONString(Result.ok("success login!")));
        res.getWriter().write(JSON.toJSONString(Result.ok("success login!")));
    }
}
