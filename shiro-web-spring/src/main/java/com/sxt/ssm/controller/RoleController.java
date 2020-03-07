package com.sxt.ssm.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/role")
@Controller
public class RoleController {
	
	@RequiresPermissions("role:list")
	@RequestMapping("/list")
	public String list() {
		
		return "role_list";
	}

}
