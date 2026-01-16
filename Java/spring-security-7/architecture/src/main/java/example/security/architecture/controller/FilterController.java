package example.security.architecture.controller;

import example.security.architecture.filter.LogoutFilterAfter1;
import example.security.architecture.filter.LogoutFilterAfter2;
import example.security.architecture.filter.LogoutFilterAfter3;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试一般接口，比如：测试过滤器加载的顺序
 *
 * @author bunny
 */
@RequestMapping("/api/filter")
@RestController
public class FilterController {

	/**
	 * <p>
	 * 在 {@link LogoutFilterAfter1}、{@link LogoutFilterAfter2}、{@link LogoutFilterAfter3｝ 中获取过滤器顺序
	 * </p>
	 *
	 * @return 过滤器列表
	 */
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping("list")
	public List<String> getFilterList() {
		return List.of(LogoutFilterAfter1.class.getName(), LogoutFilterAfter2.class.getName(), LogoutFilterAfter3.class.getName());
	}
}
