package edu.co.ustavillavicencio.coachboard.security;

import edu.co.ustavillavicencio.coachboard.entity.Usuario;
import edu.co.ustavillavicencio.coachboard.enums.Rol;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails {

    private final Long userId;
    private final String username;
    private final String password;
    private final String email;
    private final Rol rol;
    private final Long organizacionId;
    private final boolean activo;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long userId, String username, String password, String email,
                         Rol rol, Long organizacionId, boolean activo) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.rol = rol;
        this.organizacionId = organizacionId;
        this.activo = activo;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    public static UserPrincipal build(Usuario usuario) {
        return new UserPrincipal(
            usuario.getId(),
            usuario.getUsername(),
            usuario.getPassword(),
            usuario.getEmail(),
            usuario.getRol(),
            usuario.getOrganizacion() != null ? usuario.getOrganizacion().getId() : null,
            usuario.isActivo()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }
}