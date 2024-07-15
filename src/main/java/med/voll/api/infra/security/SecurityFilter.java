package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenCorrecto= "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlc3RldmVuIiwiaXNzIjoidm9sbCBtZWQiLCJpZCI6MSwiZXhwIjoxNzIxMDAyODE0fQ.ypVTXqDsIEEWo9Xxfm0XO_ja31svsCalLiThxk9nMss";
        System.out.println("tamanio del correcto "+tokenCorrecto.length());
        var authHeader = request.getHeader("Authorization");
        if(authHeader !=null ){
            var token = authHeader.replace("Bearer","").trim();
            /*System.out.println("comparacion");
            System.out.println(tokenCorrecto);
            System.out.println(token);
            System.out.println("Este debe ser el subjetc "+tokenService.getSubject(token));*/
            var subject = tokenService.getSubject(token);
            if(subject!=null){
                //Toekn valido
                //forzar inicio de sesion
                var usuario = usuarioRepository.findByLogin(subject);
                var authenticaction = new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticaction);
            }
        }
        filterChain.doFilter(request,response);
    }
}