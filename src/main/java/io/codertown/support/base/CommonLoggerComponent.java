package io.codertown.support.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 공통 로거 컴포넌트 <p>
 * [구성] <p>
 * SLF4J Logger 기능
 * 컨트롤러, 서비스 클래스에서 상속시 logger 객체 기능 사용 가능.
 */
@Component
public class CommonLoggerComponent {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

}
