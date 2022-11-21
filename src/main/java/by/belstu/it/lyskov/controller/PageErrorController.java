package by.belstu.it.lyskov.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
public class PageErrorController implements ErrorController {

    @GetMapping("/error")
    public ModelAndView showErrorPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        modelAndView.setViewName(statusCode != null
                && Integer.parseInt(statusCode.toString()) == HttpStatus.NOT_FOUND.value() ? "404" : "error");
        modelAndView.addObject("timestamp", LocalDateTime.now());
        return modelAndView;
    }
}
