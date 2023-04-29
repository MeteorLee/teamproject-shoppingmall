package project.finalproject1backend.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import project.finalproject1backend.dto.ErrorDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationEntryPoint
 *
 * 인증 과정에서 실패하거나 인증을 위한 헤더정보를 보내지 않은 경우
 * 401(UnAuthorized) 에러가 발생하게 된다.
 *
 * Spring Security에서 인증되지 않은 사용자에 대한 접근 처리는 AuthenticationEntryPoint가 담당하는데,
 * commence 메소드가 실행되어 처리된다.
 */

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException e
  ) throws IOException {
    System.out.println(request.getRequestURI());
    log.error("UnAuthorized -- message : " + e.getMessage()); // 로그를 남기고
    //response.sendRedirect("/auth/signIn"); // 로그인 페이지로 리다이렉트되도록 하였다.

    // status를 401 에러로 지정
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    // json 리턴 및 한글깨짐 수정.
    response.setContentType("application/json;charset=utf-8");
    ErrorDTO errorDTO = new ErrorDTO("401", "인증되지 않은 접근이거나, Token 유효기간이 만료되었습니다.");
    ObjectMapper objectMapper = new ObjectMapper();
    String result = objectMapper.writeValueAsString(errorDTO);
    response.getWriter().write(result);

    //위코드를 단순히 아래와 같이 해도 됨
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

  }
}