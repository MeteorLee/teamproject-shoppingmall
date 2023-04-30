package project.finalproject1backend.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import project.finalproject1backend.dto.ErrorDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AccessDeniedHandler
 *
 * AuthenticationEntryPoint와 달리 AccessDeniedHandler는
 * 유저 정보는 있으나, 엑세스 권한이 없는 경우 동작하는 친구이다.
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

//    System.out.println(request.getRequestURI());
//    log.error("Not enough privileges -- message : " + e.getMessage()); // 로그를 남기고

    // status를 403 에러로 지정
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    // json 리턴 및 한글깨짐 수정.
    response.setContentType("application/json;charset=utf-8");
    ErrorDTO errorDTO = new ErrorDTO("403","권한이 없습니다.");
    ObjectMapper objectMapper = new ObjectMapper();
    String result = objectMapper.writeValueAsString(errorDTO);
    response.getWriter().write(result);

    //위코드를 단순히 아래와 같이 해도 됨
//    response.sendError(HttpServletResponse.SC_FORBIDDEN);

  }
}
