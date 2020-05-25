package com.jai.mystarter.security.filters;


import com.jai.mystarter.models.auth.User;
import com.jai.mystarter.security.UserSessionCache;
import com.jai.mystarter.services.UserDetailImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import static com.jai.mystarter.security.SecurityConstants.*;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

   private UserDetailImpl userDetailImpl;

    public JWTAuthorizationFilter(AuthenticationManager authManager, UserDetailImpl userDetailImpl) {
        super(authManager);
        this.userDetailImpl = userDetailImpl;
    }
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            try {
                String user = Jwts.parser()
                        .setSigningKey(SECRET.getBytes())
                        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                        .getBody()
                        .getSubject();
                if (user != null) {
                    User userInfo= UserSessionCache.getInstance().getUserSession(user);
                    if(null==userInfo) {
                        userInfo = userDetailImpl.findByUsername(user);
                        logger.info("User not present in cache taking from DB");
                        UserSessionCache.getInstance().addUserSession(user,userInfo);
                    }
                    if(null!=userInfo){
                        Collection<GrantedAuthority> authorities=userInfo.getAuthorities();
                        return new UsernamePasswordAuthenticationToken(user, null, authorities);
                    }

                }

            }catch (ExpiredJwtException e){
                logger.error("JWT expired",e);
            }catch(Exception e){
                logger.error("Exception at JWT parsing",e);
            }

            return null;
        }
        return null;
    }
}
