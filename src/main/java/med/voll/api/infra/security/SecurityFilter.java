package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenCorrecto= "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlc3RldmVuIiwiaXNzIjoidm9sbCBtZWQiLCJpZCI6MSwiZXhwIjoxNzIxMDAyODE0fQ.ypVTXqDsIEEWo9Xxfm0XO_ja31svsCalLiThxk9nMss";
        System.out.println("tamanio del correcto "+tokenCorrecto.length());
        var token = request.getHeader("Authorization");
        if(token ==null || token==""){
            throw new RuntimeException("el token esta vacio");
        }
        token = token.replace("Bearer","").trim();
        System.out.println("este es el tokoen interceptado"+token);
        System.out.println("este es el tokoen interceptado"+token.length());
        System.out.println("comparacion");
        System.out.println(tokenCorrecto);
        System.out.println(token);
        System.out.println("Este debe ser el subjetc "+tokenService.getSubject(token));
        filterChain.doFilter(request,response);
    }
}