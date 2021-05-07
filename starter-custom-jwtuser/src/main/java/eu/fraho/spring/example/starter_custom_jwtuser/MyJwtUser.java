package eu.fraho.spring.example.starter_custom_jwtuser;

import com.nimbusds.jwt.JWTClaimsSet;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MyJwtUser extends JwtUser {
    private UUID uuid = UUID.randomUUID();

    @Override
    public void applyClaims(JWTClaimsSet claims) throws ParseException {
        setUsername(claims.getSubject());
        setUuid(UUID.fromString(claims.getStringClaim("uid")));
        Optional.ofNullable(claims.getStringListClaim("authorities"))
                .ifPresent(a -> setAuthorities(a.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())));
    }

    public JWTClaimsSet.Builder toClaims() {
        JWTClaimsSet.Builder builder = super.toClaims();
        builder.claim("uid", getUuid().toString());
        return builder;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}