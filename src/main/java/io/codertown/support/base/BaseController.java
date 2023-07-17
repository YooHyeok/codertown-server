package io.codertown.support.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * 전역 컨트롤러 <p>
 * [구성] <p>
 * SLF4J Logger 기능
 * 해당 컨트롤러 클래스 상속시 logger 객체 기능 사용 가능.
 */
@Controller
public class BaseController {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

}
