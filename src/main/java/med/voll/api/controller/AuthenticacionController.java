package med.voll.api.controller;

import med.voll.api.domain.usuarios.DatosAutenticacionUsuario;
import med.voll.api.domain.usuarios.Usuario;
import med.voll.api.infra.security.DTOJWTtoken;
import med.voll.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticacionController {
    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity autenticacionUsuario(@RequestBody DatosAutenticacionUsuario datosAutenticacionUsuario){
        //Le das tus credenciales y procede a generar el token
        Authentication authToken = new UsernamePasswordAuthenticationToken(datosAutenticacionUsuario.login(),datosAutenticacionUsuario.clave());
        //Verifica el token si esta bien. Lanza una excepsion si esta mal
        var userAuthenticated = authenticationManager.authenticate(authToken);
        System.out.println("Has sido autheticado");

        //si todo ok
        var JWTtoken = tokenService.generarToken((Usuario) userAuthenticated.getPrincipal());
        System.out.println("se genero tu token");
        return  ResponseEntity.ok(new DTOJWTtoken(JWTtoken));
    }
}
