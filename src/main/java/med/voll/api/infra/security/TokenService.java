package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.Valid;
import med.voll.api.domain.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.secret}")
    private String apiSecret;
    public String generarToken(Usuario usuario){
        System.out.println("Esta es mi api"+apiSecret);
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("voll med")
                    .withSubject(usuario.getLogin())
                    .withClaim("id",usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new  RuntimeException();
            // Invalid Signing configuration / Couldn't convert Claims.
        }
    }
    public Instant generarFechaExpiracion(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }

    public String getSubject(String token) {
        if(token==null){
            throw  new RuntimeException();
        }
        DecodedJWT decodedJWT=null;
        try {
            System.out.println("entro a try");
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            System.out.println("apiSecre "+apiSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    //.withIssuer("voll med")
                    .build();
            System.out.println("verifier "+verifier);
            System.out.println("justo antes de la verificacoin"+token);
            decodedJWT = verifier.verify(token);
            System.out.println("verifier"+decodedJWT.getSubject());
        } catch (JWTVerificationException exception) {
            System.out.println("hubo una excepcion "+exception.getMessage());
            exception.printStackTrace();
            // Invalid signature/claims
        }
        /*if(decodedJWT.getSubject()==null){
            throw new RuntimeException("verifiera invalido");
        }*/
        return decodedJWT.getSubject();
    }
}
