package example.security.architecture.controller;

import example.security.architecture.filter.LogoutFilterAfter1;
import example.security.architecture.filter.LogoutFilterAfter2;
import example.security.architecture.filter.TenantFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试过滤器
 *
 * @author bunny
 */
@RequestMapping("/api/filter")
@RestController
public class FilterController {

	@GetMapping("list")
	public List<String> getFilterList() {
		return List.of(LogoutFilterAfter1.class.getName(), LogoutFilterAfter2.class.getName(), TenantFilter.class.getName());
	}
}
