package com.example.Filter;

import com.example.service.MyUserRepositoryService;
import com.example.vo.MyUser;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * token校验filter
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter{

    private MyUserRepositoryService myUserRepositoryService;

    public JwtAuthenticationFilter(AuthenticationManager authManager , MyUserRepositoryService myUserRepositoryService) {
        super(authManager);
        this.myUserRepositoryService = myUserRepositoryService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader("Authorization");

        if (header == null) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            //parse the token 获取token中用户信息
            String user = (String) Jwts.parser()
                    .setSigningKey("MyJwtSecret")
                    .parseClaimsJws(token)
                    .getBody()
                    .get("username");

            if(user == null)
                return null;

            List<MyUser> myUser = myUserRepositoryService.findByUsername(user);

            if (StringUtils.isEmpty(myUser))
                return null;

            return new UsernamePasswordAuthenticationToken(myUser.get(0).getUsername(), myUser.get(0).getPassword(), new ArrayList<>());
        }
        return null;
    }

}
