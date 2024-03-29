package ru.deturpant.forum.api.exceptions;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomErrorController implements ErrorController {
    private static final String PATH = "/error";
    ErrorAttributes errorAttributes;

    @RequestMapping(CustomErrorController.PATH)
    public ResponseEntity<ErrorDto> error(WebRequest webRequest) {
        Map<String, Object> attr = errorAttributes.getErrorAttributes(webRequest,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION, ErrorAttributeOptions.Include.MESSAGE));
        return ResponseEntity.status((Integer) attr.get("status")).body(
                ErrorDto.builder()
                        .error((String) attr.get("error"))
                        .errorDescription((String) attr.get("message"))
                        .build()
        );
    }
}
