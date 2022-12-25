package com.tyagi.demoapplication.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tyagi.demoapplication.Exception.ErrorResponse;
import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    final String authorizationHeader = request.getHeader("Authorization");

    String userName = null;
    String jwt = null;

    /* Filter 1 */
    if (
      authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
    ) {
      jwt = authorizationHeader.substring(7);
      userName = jwtUtils.extractUsername(jwt);
    } else {
      log.warn("JWT Token does not begin with Bearer String");
      // handleInvalidCorrelationId(
      //   response,
      //   "JWT Token does not begin with Bearer String"
      // );
      // return;
    }

    /* Filter 2 */
    if (
      userName != null &&
      SecurityContextHolder.getContext().getAuthentication() == null
    ) {
      CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(
        userName
      );

      if (jwtUtils.validateToken(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
        );
        usernamePasswordAuthenticationToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder
          .getContext()
          .setAuthentication(usernamePasswordAuthenticationToken);
      } else {
        log.warn("Unauthorized Access");
        handleInvalidCorrelationId(response, "Unauthorized Access");
        return;
      }
    } else {
      log.warn("Something wrong with JWT Token");
      // handleInvalidCorrelationId(response, "Something wrong with JWT Token");
      // return;
    }

    filterChain.doFilter(request, response);
  }

  /* Exception Handling in Filter */
  private void handleInvalidCorrelationId(
    HttpServletResponse response,
    String message
  ) throws IOException {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
    errorResponse.setMessage(message);
    errorResponse.setTimestamp(new Date());

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
