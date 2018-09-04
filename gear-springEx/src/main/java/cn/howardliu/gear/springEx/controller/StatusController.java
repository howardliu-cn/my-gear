package cn.howardliu.gear.springEx.controller;

import cn.howardliu.gear.commons.utils.SystemInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <br>created at 16-10-13
 *
 * @author liuxh
 * @since 1.0.0
 */
@Controller
public class StatusController {
    @RequestMapping({"status", "status.json"})
    public String status(Model model) {
        model.addAttribute("system", SystemInfo.getSystemInfo());
        model.addAttribute("jvm", SystemInfo.getJvmInfo());
        return "jsonView";
    }
}
