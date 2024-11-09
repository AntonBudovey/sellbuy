package ee.taltech.iti03022024backend.entity._enum;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER_ROLE,
    ADMIN_ROLE;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
