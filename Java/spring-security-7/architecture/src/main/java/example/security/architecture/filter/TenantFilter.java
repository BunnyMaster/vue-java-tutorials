package example.security.architecture.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

/**
 * 租户过滤器
 *
 * @author bunny
 */
public class TenantFilter implements Filter {
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String tenantId = request.getHeader("X-Tenant-Id");
		boolean hasAccess = isUserAllowed(tenantId);
		if (hasAccess) {
			filterChain.doFilter(request, response);
			return;
		}
		throw new AccessDeniedException("Access denied");
	}

	/**
	 * 模拟用户权限校验
	 *
	 * @param tenantId 租户ID
	 * @return 是否有权限
	 */
	private boolean isUserAllowed(String tenantId) {
		return Objects.equals(tenantId, "1");
	}
}
