package example.security.architecture.manger;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;

public class CustomerProviderManager extends ProviderManager {
    /**
     * Construct a {@link ProviderManager} using the given {@link AuthenticationProvider}s
     *
     * @param providers the {@link AuthenticationProvider}s to use
     */
    public CustomerProviderManager(AuthenticationProvider... providers) {
        super(providers);
        setEraseCredentialsAfterAuthentication(false);
    }
}
