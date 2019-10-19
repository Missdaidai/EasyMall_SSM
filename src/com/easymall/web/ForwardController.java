package com.easymall.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/forward")
public class ForwardController {
	@RequestMapping("/{path}.action")
	public String forward(@PathVariable("path") String path) {
		return path;
	}
}
